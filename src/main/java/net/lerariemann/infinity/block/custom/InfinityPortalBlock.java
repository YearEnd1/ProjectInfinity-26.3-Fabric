package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.dimensions.RandomDimension;
import net.lerariemann.infinity.item.PortalDataHolder;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.InfinityPortal;
import net.lerariemann.infinity.util.teleport.PortalCreator;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
//? if =1.21.10 {
//?} else if =1.21.1 {
/*import net.minecraft.world.level.portal.TeleportTransition;
*///?} else {
/*import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class InfinityPortalBlock extends NetherPortalBlock implements EntityBlock, Boopable {
    public InfinityPortalBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(BOOP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        appendBoop(builder);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfinityPortalBlockEntity(pos, state, Level.OVERWORLD.location());
    }



    /**
     * This is being called when the portal is right-clicked.
     */

    @Override
    //? if >1.21 {
    public InteractionResult useWithoutItem(BlockState state, Level w, BlockPos pos,
                                            Player player, BlockHitResult hit) {
    //?} else {
    /*public InteractionResult use(BlockState state, Level w, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
    *///?}
        if (w instanceof ServerLevel world) {
            MinecraftServer s = world.getServer();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InfinityPortalBlockEntity npbe) {
                /* If the portal is open already, nothing should happen. */
                if (npbe.isOpen() && world_exists(s, npbe.getDimension()))
                    return InteractionResult.PASS;

                /* If the portal key is blank, open the portal on any right-click. */
                RandomProvider prov = InfinityMod.provider;
                Optional<Item> key = prov.getPortalKeyAsItem();
                if (key.isEmpty()) {
                    if (!npbe.isOpen()) PortalCreator.openWithStatIncrease(player, s, world, pos);
                }

                /* Otherwise check if we're using the correct key. If so, open. */
                else {
                    ItemStack usedKey = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (usedKey.is(key.get())) {
                        if (!player.getAbilities().instabuild && RandomProvider.rule("consumePortalKey")) {
                            usedKey.shrink(1); // Consume the key if needed
                        }
                        PortalCreator.openWithStatIncrease(player, s, world, pos);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public ItemStack getCloneItemStack(
            //? if >1.21 {
            LevelReader
            //?} else {
            /*BlockGetter
            *///?}
             world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof InfinityPortalBlockEntity ipbe)
            return ModItems.TRANSFINITE_KEY.get().withPortalData(ipbe);
        return ItemStack.EMPTY;
    }

    static boolean world_exists(MinecraftServer s, ResourceLocation l) {
        return (!l.getNamespace().equals(InfinityMod.MOD_ID)) ||
                s.getWorldPath(LevelResource.DATAPACK_DIR).resolve(l.getPath()).toFile().exists() ||
                s.levelKeys().contains(ResourceKey.create(Registries.DIMENSION, l));
    }

    /**
     * Spawns colourful particles.
     */
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            world.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + random.nextDouble();
            double f = (double)pos.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5) * 0.5;
            double h = ((double)random.nextFloat() - 0.5) * 0.5;
            double j = ((double)random.nextFloat() - 0.5) * 0.5;
            int k = random.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
                d = (double)pos.getX() + 0.5 + 0.25 * (double)k;
                g = random.nextFloat() * 2.0F * (float)k;
            } else {
                f = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
                j = random.nextFloat() * 2.0F * (float)k;
            }

            ParticleOptions eff = ParticleTypes.PORTAL;
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InfinityPortalBlockEntity npbe) {
                int colorInt = npbe.getPortalColor();
                Vec3 vec3d = Vec3.fromRGB24(colorInt);
                double color = 1.0D + (colorInt >> 16 & 0xFF) / 255.0D;
                eff = new DustParticleOptions(new Vector3f((float)vec3d.x, (float)vec3d.y, (float)vec3d.z), (float)color);
            }

            world.addParticle(eff, d, e, f, g, h, j);
        }
    }

    /**
     * Adds logic for portal-based recipes.
     */
    @Override
    public void entityInside(BlockState state, Level w, BlockPos pos, Entity entity) {
        AtomicBoolean bl = new AtomicBoolean(false);
        if (w instanceof ServerLevel world
                && world.getBlockEntity(pos) instanceof InfinityPortalBlockEntity ipbe) {
            MinecraftServer server = world.getServer();
            if (entity instanceof ItemEntity e) {
                ModItemFunctions.checkCollisionRecipes(world, e, ModItemFunctions.PORTAL_CRAFTING_TYPE.get(),
                        //? if >1.21 {
                        item -> PortalDataHolder.addPortalComponents(item, ipbe));
                        //?} else {
                        /*PortalDataHolder.addPortalComponents(e.getItem().getItem(), ipbe).orElse(new CompoundTag()));
                        *///?}
                InfinityMod.provider.getPortalKeyAsItem().ifPresent(item -> { //opening a portal by tossing a key in
                    if (e.getItem().is(item)) {
                        InfinityPortal.tryUpdateOpenStatus(ipbe, world, pos, server);
                        if (ipbe.isOpen()) return;
                        Player nearestPlayer =
                                world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
                        PortalCreator.openWithStatIncrease(nearestPlayer, server, world, pos);
                        e.getItem().shrink(1);
                        e.setDeltaMovement(e.getDeltaMovement().scale(-1));
                        e.setPortalCooldown(200);
                        bl.set(true);
                    }
                });
            }
            if (entity instanceof Player player
                    && InfinityMod.provider.isPortalKeyBlank()) {
                ServerLevel world1 = server.getLevel(ResourceKey.create(Registries.DIMENSION, ipbe.getDimension()));
                if ((world1 == null) || !ipbe.isOpen())
                    PortalCreator.openWithStatIncrease(player, server, world, pos);
                else {
                    Timebombable tw = (Timebombable)world1;
                    if (tw.infinity$isTimebombed() && tw.infinity$tryRestore()) {
                        new RandomDimension(ipbe.getDimension(), server);
                        PortalCreator.openWithStatIncrease(player, server, world, pos);
                    }
                }
            }
        }
        if (!bl.get()) super.entityInside(state, w, pos, entity);
    }
    /**
     * Spawns chaos pawns in the portal.
     */
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.dimensionType().natural() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                && random.nextInt(2000) < world.getDifficulty().getId()) {
            ChaosPawn entity;
            while (world.getBlockState(pos).is(this)) {
                pos = pos.below();
            }
            if (world.getBlockState(pos).isValidSpawn(world, pos, ModEntities.CHAOS_PAWN.get()) &&
                    InfinityMethods.chaosMobsEnabled() &&
                    (entity = ModEntities.CHAOS_PAWN.get().spawn(world, pos.above(), EntitySpawnReason.STRUCTURE)) != null) {
                entity.setPortalCooldown();
                BlockEntity blockEntity = world.getBlockEntity(pos.above());
                if (blockEntity instanceof InfinityPortalBlockEntity npbe) {
                    int color = npbe.getPortalColor();
                    Vec3 c = Vec3.fromRGB24(color);
                    entity.setAllColors((int)(256 * c.z) + 256 * (int)(256 * c.y) + 65536 * (int)(256 * c.x));
                }
            }
        }
    }

    /**
     * This is called when something's trying to use the portal and returns the data on where they end up.
     */
    @Nullable
    @Override
    public
    //? if >1.21.2 {
    TeleportTransition
    //?} else if >1.21 {
    /*TeleportTransition
    *///?} else {
    /*PortalInfo
    *///?}
    getPortalDestination(ServerLevel worldFrom, Entity entity, BlockPos posFrom) {
        if (worldFrom.getBlockEntity(posFrom) instanceof InfinityPortalBlockEntity ipbe) {
            return (new InfinityPortal(ipbe, worldFrom, posFrom)).getTeleportTarget(entity);
        }
        else if (entity instanceof ServerPlayer player)
            InfinityMethods.sendUnexpectedError(player, "portal");
        return InfinityPortal.emptyTarget(entity); //if anything goes wrong, don't teleport anywhere
    }
}
