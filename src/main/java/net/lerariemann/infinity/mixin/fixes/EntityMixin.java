package net.lerariemann.infinity.mixin.fixes;

import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if <1.21 {
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.util.teleport.InfinityPortal;
import net.lerariemann.infinity.util.teleport.PortalCreator;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}

@Mixin(Entity.class)
public abstract class EntityMixin {
    //? if <1.21 {
    @Shadow
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
    //?}

    /* Neoforge-exclusive mixins working around its code (which causes mobs to be unable to swim in iridescence) */
    @Inject(method="updateFluidHeightAndDoFluidPushing", at = @At(value = "RETURN"), cancellable = true)
    void inj(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (tag.equals(FluidTags.WATER))
            if (PlatformMethods.acidTest((Entity)(Object)this, false))
                cir.setReturnValue(true);
    }

    @Inject(method = "isEyeInFluid", at = @At("RETURN"), cancellable = true)
    void inj(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
        if (fluidTag.equals(FluidTags.WATER))
            if (PlatformMethods.acidTest((Entity)(Object)this, true))
                cir.setReturnValue(true);
    }

    @Inject(method = "getFluidHeight", at = @At("RETURN"), cancellable = true)
    void inj2(TagKey<Fluid> fluid, CallbackInfoReturnable<Double> cir) {
        if (fluid.equals(FluidTags.WATER))
            cir.setReturnValue(Math.max(cir.getReturnValue(), PlatformMethods.acidHeightTest((Entity)(Object)this)));
    }
}

