package net.codexarchonic.infinity.block.entity;

import net.codexarchonic.infinity.block.custom.AltarBlock;
import net.codexarchonic.infinity.registry.core.ModBlockEntities;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static net.codexarchonic.infinity.block.custom.AltarBlock.numColors;

/// This will be used later for player-performed invocations such as creating new biomes, but for now is kinda deprecated
public class CosmicAltarBlockEntity extends SpecialBlockEntity.Boopable implements GameEventListener.
        //? if <1.21 {
        /*Holder
        *///?} else {
        Provider
        //?}
        <VibrationSystem.Listener>,
        VibrationSystem {
    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.Listener vibrationListener;
    private final VibrationSystem.User vibrationUser;

    public Set<Integer> error = new HashSet<>();
    public Map<Integer, Set<BlockPos>> pedestals = new HashMap<>();
    public Map<BlockPos, Integer> pedestalsInv = new HashMap<>();
    public boolean populated = false;

    public CosmicAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_ALTAR.get(), pos, state);
        this.vibrationUser = new CosmicAltarBlockEntity.User(this);
        this.vibrationData = new VibrationSystem.Data();
        this.vibrationListener = new VibrationSystem.Listener(this);
    }

    void clear() {
        error.clear();
        pedestals.clear();
        pedestalsInv.clear();
        populated = false;
    }

    void updatePedestals(ServerLevel level, BlockPos pos,
    //? if <1.21 {
    /*GameEvent
    *///?} else {
    Holder<GameEvent>
    //?}
    event) {
        if (VersionMethods.gameEventsEqual(event, GameEvent.BLOCK_PLACE)) addPedestal(level, pos);
        if (VersionMethods.gameEventsEqual(event, GameEvent.BLOCK_CHANGE)) {
            popPedestal(pos);
            addPedestal(level, pos);
        }
        if (VersionMethods.gameEventsEqual(event, GameEvent.BLOCK_DESTROY)) popPedestal(pos);
        sync();
    }

    void addPedestal(Level level, BlockPos pos) {
        if (pedestalsInv.containsKey(pos)) return;
        int i = VersionMethods.getPropertyOrElse(level.getBlockState(pos), AltarBlock.COLOR, 0);
        assert i < numColors;
        addPedestal(pos, i);
    }

    void addPedestal(BlockPos pos, int i) {
        pedestalsInv.put(pos, i);

        if (!pedestals.containsKey(i))
            pedestals.put(i, new HashSet<>());

        else if (i != 0) error.add(i);

        pedestals.get(i).add(pos);

        populated = true;
    }

    void popPedestal(BlockPos pos) {
        if (!pedestalsInv.containsKey(pos)) return;

        int i = pedestalsInv.get(pos);
        pedestalsInv.remove(pos);

        if (pedestals.containsKey(i)) {
            pedestals.get(i).remove(pos);

            if (pedestals.get(i).isEmpty())
                pedestals.remove(i);

            else if (i != 0 && pedestals.get(i).size() == 1)
                error.remove(i);
        }

        if (pedestals.isEmpty()) populated = false;
    }

    //? if >1.21 {
    @Override
    public boolean isValidBlockState(BlockState state) {
        return true;
    }
    //?}

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
        if (populated) {
            ListTag pds = new ListTag();
            pedestalsInv.keySet().forEach(p -> {
                CompoundTag pd = new CompoundTag();
                pd.put("pos", NbtUtils.wrapBP(p));
                pd.putInt("i", pedestalsInv.get(p));
                pds.add(pd);
            });
            res.put("pedestals", pds);
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
        clear();
        CompoundTag res = VersionMethods.loadBlockEntityData(tag);
        ListTag t = NbtUtils.getList(res, "pedestals", Tag.TAG_COMPOUND);
        for (Tag tg: t) if (tg instanceof CompoundTag pos) {
            BlockPos p = NbtUtils.unwrapBP(NbtUtils.getCompound(pos, "pos"));
            addPedestal(p, NbtUtils.getInt(pos, "i"));
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

    @Override
    public VibrationSystem.Listener getListener() {
        return this.vibrationListener;
    }
    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }
    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public int getTint() {
        return -1;
    }

    public static class User implements VibrationSystem.User {
        protected final BlockPos pos;
        private final PositionSource positionSource;
        private final CosmicAltarBlockEntity parent;

        public User(CosmicAltarBlockEntity parent) {
            this.pos = parent.getBlockPos();
            this.positionSource = new BlockPositionSource(pos);
            this.parent = parent;
        }

        @Override
        public int getListenerRadius() {
            return 6;
        }

        @Override
        public PositionSource getPositionSource() {
            return positionSource;
        }


        //? if <1.21 {
        /*@Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent gameEvent, GameEvent.Context context) {
        *///?} else {
        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
         //?}
            boolean bl = ((!pos.equals(this.pos)) &&
                    pos.getY() == this.pos.getY() &&
                    (VersionMethods.gameEventsEqual(gameEvent, GameEvent.BLOCK_DESTROY)
                            || VersionMethods.gameEventsEqual(gameEvent, GameEvent.BLOCK_PLACE)
                            || VersionMethods.gameEventsEqual(gameEvent, GameEvent.BLOCK_CHANGE))
                    && context.affectedState() != null
                    && context.affectedState().getBlock() instanceof AltarBlock
            );
            if (bl) parent.updatePedestals(level, pos, gameEvent);
            return bl;
        }

        @Override
        //? if <1.21 {
        /*public void onReceiveVibration(ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity entity1, float v) {
        *///?} else {
        public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity entity, @Nullable Entity playerEntity, float distance) {
        //?}
        }
    }
}
