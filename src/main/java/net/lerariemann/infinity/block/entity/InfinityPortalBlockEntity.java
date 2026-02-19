package net.lerariemann.infinity.block.entity;

import net.lerariemann.infinity.options.PortalColorApplier;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.registry.core.ModBlockEntities;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.teleport.InfinityPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import org.jspecify.annotations.Nullable;
import java.util.Random;

public class InfinityPortalBlockEntity extends SpecialBlockEntity {
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
    public boolean isFootprint() { return this.dimension.getNamespace().equals("footprint"); }

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


    //? if >1.21.4 {
    public void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
//?} else if >1.21 {
    /*public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
        super.saveAdditional(tag, registryLookup);
*///?} else {
    /*public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
*///?}
        CompoundTag res = new CompoundTag();
        res.putInt("Color", this.portalColor);
        res.putString("Dimension", this.dimension.toString());
        res.putBoolean("Open", this.isOpen);
        if (otherSidePos != null) {
            res.put("other_side", NbtUtils.wrapBP(otherSidePos));
        }

        VersionMethods.saveBlockEntityData(tag, res);
    }

    //? if >1.21.4 {
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
//?} else if >1.21 {
    /*public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
        super.loadAdditional(tag, registryLookup);
*///?} else {
    /*public void load(CompoundTag tag) {
        super.load(tag);
*///?}
        CompoundTag res = VersionMethods.loadBlockEntityData(tag);

        if (NbtUtils.compoundHasString(res, "Dimension")) {
            this.dimension = VersionMethods.id(NbtUtils.getString(res, "Dimension"));
            if (level != null && level.getServer() != null)
                this.portalColor = NbtUtils.getInt(res, "Color", PortalColorApplier.of(dimension, level.getServer()).apply(worldPosition) & 0xFFFFFF);
            else
                this.portalColor = NbtUtils.getInt(res, "Color", PortalColorApplier.of(dimension, new CompoundTag()).apply(worldPosition) & 0xFFFFFF);
        }
        else {
            setDimension(InfinityMethods.getRandomSeed(new Random())); //random by default
        }
        this.isOpen = NbtUtils.getBoolean(res, "Open", false);
        CompoundTag pos = NbtUtils.getCompound(res,"other_side");
        if (!pos.isEmpty()) otherSidePos = NbtUtils.unwrapBP(pos);
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
