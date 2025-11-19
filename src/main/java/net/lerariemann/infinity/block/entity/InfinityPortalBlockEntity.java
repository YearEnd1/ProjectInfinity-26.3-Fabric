package net.lerariemann.infinity.block.entity;

import net.lerariemann.infinity.options.PortalColorApplier;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.registry.core.ModBlockEntities;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.teleport.InfinityPortal;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

//? if neoforge
/*import javax.annotation.ParametersAreNonnullByDefault;*/
import java.util.Random;

@MethodsReturnNonnullByDefault
//? if neoforge
/*@ParametersAreNonnullByDefault*/
public class InfinityPortalBlockEntity extends TintableBlockEntity {
    private final ContainerData propertyDelegate;
    private ResourceLocation dimension;
    private int portalColor;
    private boolean isOpen;
    @Nullable
    private BlockPos otherSidePos;

    public InfinityPortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEITHER_PORTAL.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                if (index == 0) {
                    return InfinityPortalBlockEntity.this.portalColor;
                }
                return 0;
            }

            public void set(int index, int value) {
                if (index == 0) {
                    InfinityPortalBlockEntity.this.portalColor = value & 0xFFFFFF;
                }

            }
            public int getCount() {
                return 1;
            }
        };
    }

    public InfinityPortalBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        this(pos, state, (int)InfinityMethods.getNumericFromId(id), id);
    }

    public InfinityPortalBlockEntity(BlockPos pos, BlockState state, int i, ResourceLocation id) {
        this(pos, state);
        this.dimension = id;
        this.portalColor = i & 0xFFFFFF;
        this.isOpen = false;
        this.otherSidePos = null;
    }

    public ResourceLocation getDimension() {
        return this.dimension;
    }
    public int getPortalColor() {
        return this.portalColor;
    }
    public boolean isOpen() {
        return this.isOpen;
    }
    @Nullable
    public BlockPos getOtherSidePos() { return this.otherSidePos; }

    public void setDimension(long c) {
        setColor((int)c);
        setDimension(InfinityMethods.getDimId(c));
    }
    public void setDimension(ResourceLocation i) {
        this.dimension = i;
    }
    public void setColor(int c) {
        this.portalColor = c & 0xFFFFFF;
        if (level != null) {
            BlockState bs = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, bs, bs, 0);
        }
    }
    public void setOpen(boolean i) {
        this.isOpen = i;
    }
    public void setBlockPos(BlockPos pos) {
        this.otherSidePos = pos;
        setChanged();
    }

    public void setData(@Nullable MinecraftServer server, ResourceLocation i) {
        if (server == null) {
            setData(i);
            return;
        }
        setDimension(i);
        setColor(PortalColorApplier.of(i, server).apply(getBlockPos()));
        setOpen(server.levelKeys().contains(ResourceKey.create(Registries.DIMENSION, i)));
    }
    public void setData(ResourceLocation i) {
        setDimension(i);
        setColor((int)InfinityMethods.getNumericFromId(i));
        setOpen(false);
    }

    @Nullable
    public ServerLevel getDimensionAsWorld() {
        if (getLevel() instanceof ServerLevel serverWorld)
            return serverWorld.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, getDimension()));
        return null;
    }
    public boolean isConnected() {
        if (!isOpen()) return false;
        if (getLevel() instanceof ServerLevel worldFrom && !InfinityMethods.isTimebombed(worldFrom)) {
            return InfinityPortal.isValidDestination(worldFrom, getDimensionAsWorld(), getOtherSidePos());
        }
        return false;
    }
    public boolean isConnectedBothSides() {
        if (!isOpen()) return false;
        if (getLevel() instanceof ServerLevel worldFrom && !InfinityMethods.isTimebombed(worldFrom)) {
            ServerLevel worldTo = getDimensionAsWorld();
            BlockPos posTo = getOtherSidePos();
            if (posTo == null || !InfinityMethods.dimExists(worldTo)) return false;
            return (worldTo.getBlockEntity(posTo) instanceof InfinityPortalBlockEntity ipbe
                    && ipbe.getDimension().toString().equals(worldFrom.dimension().location().toString())
                    && ipbe.isConnected());
        }
        return false;
    }


    @Override
    public void saveAdditional(CompoundTag tag
                               //? if >1.21
                               ,HolderLookup.Provider registryLookup
    ) {
        super.saveAdditional(tag
                //? if >1.21
                , registryLookup
        );
        tag.putInt("Color", this.portalColor);
        tag.putString("Dimension", this.dimension.toString());
        tag.putBoolean("Open", this.isOpen);
        if (otherSidePos != null) {
            CompoundTag pos = new CompoundTag();
            pos.putInt("x", otherSidePos.getX());
            pos.putInt("y", otherSidePos.getY());
            pos.putInt("z", otherSidePos.getZ());
            tag.put("other_side", pos);
        }
    }

    @Override
    //? if >1.21 {
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
        super.loadAdditional(tag, registryLookup);
    //?} else {
    /*public void load(CompoundTag tag) {
        super.load(tag);
    *///?}
        if (tag.getTagType("Dimension") == Tag.TAG_ANY_NUMERIC) { //conversion from legacy formats
            this.portalColor = NbtUtils.getInt(tag, "Dimension") & 0xFFFFFF;
            if (tag.contains("DimensionName")) {
                this.dimension = VersionMethods.id(NbtUtils.getString(tag, "DimensionName"));
            }
            else this.dimension = InfinityMethods.getDimId(this.portalColor);
        }
        else if (tag.getTagType("Dimension") == Tag.TAG_STRING) { //new better format
            this.dimension = VersionMethods.id(NbtUtils.getString(tag, "Dimension"));
            if (level != null && level.getServer() != null)
                this.portalColor = NbtUtils.getInt(tag, "Color", PortalColorApplier.of(dimension, level.getServer()).apply(worldPosition) & 0xFFFFFF);
            else
                this.portalColor = NbtUtils.getInt(tag, "Color", PortalColorApplier.of(dimension, new CompoundTag()).apply(worldPosition) & 0xFFFFFF);
        }
        else {
            setDimension(InfinityMethods.getRandomSeed(new Random())); //random by default
        }
        this.isOpen = NbtUtils.getBoolean(tag, "Open", false);
        if (tag.contains("other_side")) {
            CompoundTag pos = NbtUtils.getCompound(tag,"other_side");
            otherSidePos = new BlockPos(NbtUtils.getInt(pos, "x"), NbtUtils.getInt(pos, "y"), NbtUtils.getInt(pos, "z"));
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(
            //? if >1.21
            HolderLookup.Provider registryLookup
    ) {
        return saveWithoutMetadata(
                //? if >1.21
                registryLookup
        );
    }

//    @Override
    public int getTint() {
        return propertyDelegate.get(0);
    }
}
