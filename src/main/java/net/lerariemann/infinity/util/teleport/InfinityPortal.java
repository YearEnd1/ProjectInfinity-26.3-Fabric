package net.lerariemann.infinity.util.teleport;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.registry.var.ModPoi;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.BlockUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public class InfinityPortal {
    InfinityPortalBlockEntity ipbe;
    ServerLevel worldFrom;
    BlockPos posFrom;
    BlockUtil.FoundRectangle portalFrom;
    @Nullable ServerLevel worldTo;
    @Nullable BlockPos posTo;
    @Nullable BlockUtil.FoundRectangle portalTo;
    boolean unableToCreatePortalFlag = false;
    boolean noSyncFlag = false;

    public InfinityPortal(InfinityPortalBlockEntity ipbe, ServerLevel worldFrom, BlockPos startingPos) {
        this.ipbe = ipbe;
        this.worldFrom = worldFrom;
        portalFrom = getRect(worldFrom, startingPos);
        posFrom = lowerCenterPos(portalFrom, worldFrom);
        worldTo = worldFrom.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ipbe.getDimension()));
        tryUpdateOpenStatus(ipbe, worldFrom, startingPos, worldTo);

        if (portalShouldWork()) {
            BlockPos targetPos = ipbe.getOtherSidePos();
            if (isValidDestination(worldFrom, worldTo, targetPos)) {
                posTo = targetPos;
                scanExistingTarget();
            }
            scanNewTeleportTarget();
        }
    }

    /** A portal should be marked as "open" if and only if it has a non-null destination dimension. These functions are here to ensure it */
    public static void tryUpdateOpenStatus(InfinityPortalBlockEntity ipbe, ServerLevel worldFrom, BlockPos posFrom,
                                           MinecraftServer server) {
        ServerLevel worldTo = server.getLevel(ResourceKey.create(Registries.DIMENSION, ipbe.getDimension()));
        tryUpdateOpenStatus(ipbe, worldFrom, posFrom, worldTo);
    }
    public static void tryUpdateOpenStatus(InfinityPortalBlockEntity ipbe, ServerLevel worldFrom, BlockPos posFrom,
                                           ServerLevel worldTo) {
        if (!ipbe.isOpen() ^ worldTo == null) {
            PortalCreator.modifyPortalRecursive(worldFrom, posFrom, e -> e.setOpen(!ipbe.isOpen()));
        }
    }

    /** Finding a rectangle of portal blocks provided a position of one of them. */
    public static BlockUtil.FoundRectangle getRect(Level world, BlockPos pos) {
        BlockState blockStateFrom = world.getBlockState(pos);
        return BlockUtil.getLargestRectangleAround(
                pos, getAxisOrDefault(blockStateFrom),
                21, Direction.Axis.Y, 21,
                posx -> world.getBlockState(posx) == blockStateFrom
        );
    }

    /** Getting the central block of a portal rectangle; setting posTo to this ensures CreateCompat works correctly. */
    static BlockPos lowerCenterPos(BlockUtil.FoundRectangle rect, Level world) {
        return lowerCenterPos(rect, world.getBlockState(rect.minCorner).getValue(BlockStateProperties.HORIZONTAL_AXIS));
    }
    static BlockPos lowerCenterPos(BlockUtil.FoundRectangle rect, Direction.Axis axis) {
        boolean bl = axis.equals(Direction.Axis.X);
        int i = rect.axis1Size / 2;
        return rect.minCorner.offset(bl ? i : 0, 0, bl ? 0 : i);
    }

    /** Finding where to teleport stuff. The constructor ensures all scanning is already done by this point */
    public DimensionTransition getTeleportTarget(Entity entity) {
        if (portalShouldWork() && portalTo != null && worldTo != null) {
            return NetherPortalBlock.getDimensionTransitionFromExit(entity, posTo, portalTo, worldTo,
                    DimensionTransition.PLAY_PORTAL_SOUND.then(entityx -> entityx.placePortalTicket(posTo)));
        }
        //below this point is error handling. note that not all such errors are bugs
        if (entity instanceof ServerPlayer player) {
            sendErrors(player);
        }
        return emptyTarget(entity);
    }

    public boolean portalShouldWork() {
        return (InfinityMethods.dimExists(worldTo) //dimension exists and is not timebombed
                && ipbe.isOpen() //the portal is not closed
                && !worldTo.dimension().equals(worldFrom.dimension())); //the portal does not lead back to its own dimension
    }

    /** If teleportation failed for any reason, this sends the reason to the player. */
    public void sendErrors(ServerPlayer player) {
        if (worldTo != null) {
            if (worldTo.dimension().equals(worldFrom.dimension()))
                player.sendSystemMessage(Component.translatable("error.infinity.portal.matching_ends"));
            else if (InfinityMethods.isTimebombed(worldTo))
                player.sendSystemMessage(Component.translatable("error.infinity.portal.deleted"));
            else if (unableToCreatePortalFlag)
                player.sendSystemMessage(Component.translatable("error.infinity.portal.cannot_create"));
            else InfinityMethods.sendUnexpectedError(player, "portal");
        }
        else if (!ipbe.isOpen())
            InfinityMod.provider.getPortalKeyAsItem().ifPresent(item -> player.sendSystemMessage(
                    Component.translatable("error.infinity.portal.closed",
                            ((MutableComponent)item.getDescription()).withStyle(ChatFormatting.AQUA))));
        else player.sendSystemMessage(Component.translatable("error.infinity.portal.null"));
    }

    public static Direction.Axis getAxisOrDefault(BlockState state) {
        if (state.getProperties().contains(BlockStateProperties.HORIZONTAL_AXIS))
            return state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
        return Direction.Axis.X;
    }

    /** If teleportation failed for any reason, this ensures the entity does not teleport anywhere. */
    public static DimensionTransition emptyTarget(Entity entity) {
        return new DimensionTransition((ServerLevel)entity.level(), entity.position(),
                entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(),
                DimensionTransition.DO_NOTHING);
    }

    /** Filter for infinity portals of the correct destination. */
    public static boolean isValidDestination(ServerLevel worldFrom, ServerLevel worldTo, BlockPos posTo) {
        if (posTo == null || !InfinityMethods.dimExists(worldTo)) return false;
        return (worldTo.getBlockEntity(posTo) instanceof InfinityPortalBlockEntity ipbeTo
                && ipbeTo.getDimension().toString().equals(worldFrom.dimension().location().toString()));
    }

    /** Filling in the blanks asserting the portal is correctly synced */
    private void scanExistingTarget() {
        assert worldTo != null && posTo != null;
        BlockState blockState = worldTo.getBlockState(posTo);
        portalTo = BlockUtil.getLargestRectangleAround(
                posTo, getAxisOrDefault(blockState),
                21, Direction.Axis.Y, 21,
                posx -> worldTo.getBlockState(posx) == blockState
        );
    }

    /** If the portal is not correctly synced, this resyncs it, possibly creating a new exit portal */
    private void scanNewTeleportTarget() {
        assert worldTo != null;
        findOrCreateExitPortal();
        if (portalTo == null) return;
        posTo = lowerCenterPos(portalTo, worldTo);
        if (!noSyncFlag) trySyncPortals(worldFrom, posFrom, worldTo, posTo);
    }

    /** Searches for a rectangle of portal blocks to teleport to */
    private void findOrCreateExitPortal() {
        assert worldTo != null;
        WorldBorder wb = worldTo.getWorldBorder();
        double d = DimensionType.getTeleportationScale(worldFrom.dimensionType(), worldTo.dimensionType());
        BlockPos originOfTesting = wb.clampToBounds(posFrom.getX() * d, posFrom.getY(), posFrom.getZ() * d);

        Optional<BlockPos> optional = findNewExitPortalPosition(wb, originOfTesting);

        if (optional.isPresent())
            portalTo = getRect(worldTo, optional.get());
        else { //we found nothing and will create a new portal
            Direction.Axis axis = getAxisOrDefault(worldFrom.getBlockState(posFrom));
            worldTo.getPortalForcer().createPortal(originOfTesting, axis)
                    .ifPresentOrElse(rect -> portalTo = rect, () -> unableToCreatePortalFlag = true);
        }
    }

    /** Trying to scan for any valid portal block. */
    private Optional<BlockPos> findNewExitPortalPosition(WorldBorder wbTo, BlockPos originOfTesting) {
        assert worldTo != null;
        int radiusOfTesting = 128;
        PoiManager poiStorage = worldTo.getPoiManager();
        poiStorage.ensureLoadedAndValid(worldTo, originOfTesting, radiusOfTesting);

        //First scan for valid infinity portals
        Optional<BlockPos> portal = poiStorage.getInSquare(poiType ->
                                poiType.is(ModPoi.NEITHER_PORTAL_KEY),
                        originOfTesting, radiusOfTesting, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .filter(wbTo::isWithinBounds)
                .filter(pos -> isValidDestination(worldFrom, worldTo, pos))
                .min(Comparator.comparingDouble(posTo -> posTo.distSqr(originOfTesting)));
        if (portal.isPresent()) return portal;

        //If one wasn't found, find a nether portal instead and ensure it will not be overwritten
        portal = poiStorage.getInSquare(poiType ->
                                poiType.is(PoiTypes.NETHER_PORTAL),
                        originOfTesting, radiusOfTesting, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .filter(wbTo::isWithinBounds)
                .min(Comparator.comparingDouble(posTo -> posTo.distSqr(originOfTesting)));
        noSyncFlag = portal.isPresent(); //if a nether portal indeed was found we do not wish to overwrite it

        return portal;
    }

    /** Establishing a mutual connection between two portals */
    public static void trySyncPortals(ServerLevel worldFrom, BlockPos posFrom,
                                      ServerLevel worldTo, BlockPos posTo) {
        if (worldTo == null || posTo == null
                || !(worldTo.getBlockState(posTo).getBlock() instanceof NetherPortalBlock)) return;

        PortalCreator.PortalModifierUnion otherSideModifier = new PortalCreator.PortalModifierUnion();
        ResourceLocation idFrom = worldFrom.dimension().location();

        if (worldTo.getBlockEntity(posTo) instanceof InfinityPortalBlockEntity ipbeTo) {
            if (!ipbeTo.getDimension().toString().equals(idFrom.toString())) return;
            if (ipbeTo.isConnectedBothSides()) return; //don't resync what's already synced
        }
        else {
            otherSideModifier = PortalCreator.forInitialSetupping(worldTo, posTo, idFrom, true);
            //make it an infinity portal while you're at it
        }

        otherSideModifier.addModifier(ipbe1 -> ipbe1.setBlockPos(posFrom));
        PortalCreator.modifyPortalRecursive(worldFrom, posFrom, ipbe -> ipbe.setBlockPos(posTo));
        PortalCreator.modifyPortalRecursive(worldTo, posTo, otherSideModifier);
    }
}
