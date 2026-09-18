package net.codexarchonic.infinity.mixin.fixes;

import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if <1.21 {
/*import net.codexarchonic.infinity.block.entity.InfinityPortalBlockEntity;
import net.codexarchonic.infinity.util.teleport.InfinityPortal;
import net.codexarchonic.infinity.util.teleport.PortalCreator;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import org.spongepowered.asm.mixin.Shadow;
*///?}

@Mixin(Entity.class)
public abstract class EntityMixin {
    //? if <1.21 {
    /*@Shadow
    protected BlockPos portalEntrancePos;

    @Shadow public abstract Level level();

    @ModifyArg(method = "handleNetherPortal()V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/Entity;changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;"))
    private ServerLevel injected(ServerLevel originalWorldTo) {
        if (level() instanceof ServerLevel worldFrom) {
            BlockPos pos = this.portalEntrancePos;
            if (worldFrom.getBlockEntity(pos) instanceof InfinityPortalBlockEntity portal) {
                ResourceKey<Level> keyTo = ResourceKey.create(Registries.DIMENSION,
                        portal.getDimension()); //redirect teleportation to infdims
                ServerLevel worldTo = worldFrom.getServer().getLevel(keyTo);
                return (InfinityMethods.dimExists(worldTo)
                        && portal.isOpen()) ? worldTo : worldFrom;
            }
            else if (worldFrom.getBlockState(pos).is(Blocks.NETHER_PORTAL)
                    && InfinityMethods.isInfinity(worldFrom)) { //redirect returning from infdims and make portals in them infinity (for sync reasons)
                PortalCreator.modifyPortalRecursive(worldFrom, pos, Level.OVERWORLD.location(), true);
                return worldFrom.getServer().overworld();
            }
            else return originalWorldTo;
        }
        return originalWorldTo;
    }

    @Inject(method = "findDimensionEntryPoint", at = @At(value = "HEAD"), cancellable = true)
    private void injected(ServerLevel destination, CallbackInfoReturnable<PortalInfo> cir) {
        if (level() instanceof ServerLevel worldFrom) {
            BlockPos posFrom = portalEntrancePos;
            if (worldFrom.getBlockEntity(posFrom) instanceof InfinityPortalBlockEntity ipbe) {
                cir.setReturnValue((new InfinityPortal(ipbe, worldFrom, posFrom)).getTeleportTarget((Entity)(Object)this));
            }
        }
    }
    *///?}

    /* (Neo)forge-exclusive mixins working around its code (which causes mobs to be unable to swim in iridescence) */
    //? neoforge {
    /*@Inject(method="updateFluidHeightAndDoFluidPushing", at = @At(value = "RETURN"), cancellable = true)
    void neoMobsSwim(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (tag.equals(FluidTags.WATER))
            if (InfinityPlatform.INSTANCE.acidTest((Entity)(Object)this, false))
                cir.setReturnValue(true);
    }
    *///?}

    @Inject(method = "isEyeInFluid", at = @At("RETURN"), cancellable = true)
    void neoMobsSwim2(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
        if (fluidTag.equals(FluidTags.WATER))
            if (InfinityPlatform.INSTANCE.acidTest((Entity)(Object)this, true))
                cir.setReturnValue(true);
    }

    @Inject(method = "getFluidHeight", at = @At("RETURN"), cancellable = true)
    void neoMobsSwim3(TagKey<Fluid> fluid, CallbackInfoReturnable<Double> cir) {
        if (fluid.equals(FluidTags.WATER))
            cir.setReturnValue(Math.max(cir.getReturnValue(), InfinityPlatform.INSTANCE.acidHeightTest((Entity)(Object)this)));
    }

    //? if forge {
    /*@Shadow
    private Level level;

    @Redirect(method = "findDimensionEntryPoint",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;dimension()Lnet/minecraft/resources/ResourceKey;"),
            slice = @Slice(from = @At(
                    value = "INVOKE", target = "Lnet/minecraft/world/level/Level;dimension()Lnet/minecraft/resources/ResourceKey;"
            ), to = @At("TAIL")))
    ResourceKey<Level> smuggle(Level w) {
        if (w == this.level) return Level.NETHER;
        return w.dimension();
    }
    *///?}
}

