package net.lerariemann.infinity.block.entity;

import net.lerariemann.infinity.registry.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/// This will be used later for player-performed invocations such as creating new biomes, but for now is kinda deprecated
public class CosmicAltarBlockEntity extends BlockEntity {
    public CosmicAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_ALTAR.get(), pos, state);
    }

    //? if >1.21 {
    /*@Override
    public boolean isValidBlockState(BlockState state) {
        return true;
    }
    *///?}
    
    @Deprecated
    public static void serverTick(Level world, BlockPos pos, BlockState state, CosmicAltarBlockEntity be) {
    }

    //? if >1.21 {
    /*@Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
    }
    *///?} else {
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }
    //?}

    @Override
    protected void saveAdditional(CompoundTag nbt
            //? if >1.21
            /*, HolderLookup.Provider registryLookup*/
    ) {
        super.saveAdditional(nbt
                //? if >1.21
                /*, registryLookup*/
        );
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(
            //? if >1.21
            /*HolderLookup.Provider registryLookup*/
    ) {
        return saveWithoutMetadata(
                //? if >1.21
                /*registryLookup*/
        );
    }
}
