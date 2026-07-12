package net.codexarchonic.infinity.block.entity;

import net.codexarchonic.infinity.block.custom.BiomeBottleBlock;
import net.codexarchonic.infinity.registry.core.ModBlockEntities;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
//? if >1.21.4 {
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

//? if >1.21 {
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
//?}

public class BiomeBottleBlockEntity extends SpecialBlockEntity {
    private final ContainerData propertyDelegate;
    private ResourceLocation biome;
    private int color;
    public int charge;
    private int from_charge;

    public BiomeBottleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIOME_BOTTLE.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                if (index == 0) {
                    return BiomeBottleBlockEntity.this.color;
                }
                return 0;
            }

            public void set(int index, int value) {
                if (index == 0) {
                    BiomeBottleBlockEntity.this.color = value;
                }

            }
            public int getCount() {
                return 1;
            }
        };
        this.empty();
    }

    public void setBiome(Holder<Biome> biome) {
        if (biome.unwrapKey().isPresent()) setBiome(biome.value().getSkyColor(), biome.unwrapKey().get().location());
    }

    public void setBiome(int c, ResourceLocation i) {
        this.color = c;
        this.biome = i;
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
        res.putString("Biome", biome.toString());
        res.putInt("Color", color);
        res.putInt("Charge", charge);
        if (from_charge > 0) res.putInt("from_charge", from_charge);
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
        this.charge = NbtUtils.getInt(res, "Charge");
        this.biome = VersionMethods.id(NbtUtils.getString(res, "Biome"));
        this.color = NbtUtils.getInt(res, "Color");
        this.from_charge = NbtUtils.getInt(res, "from_charge", 0);
    }

    //? if >1.21 {
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        BiomeBottleBlock.addComponents(componentMapBuilder, biome, color, charge);
    }

    @Override
    protected void applyImplicitComponents(
            //? if >1.21.4 {
            DataComponentGetter
             //?} else {
            /*DataComponentInput
            *///?}
            components) {
        super.applyImplicitComponents(components);
        this.biome = components.getOrDefault(ModComponentTypes.BIOME_CONTENTS.get(), BiomeBottleBlock.defaultBiome());
        this.color = components.getOrDefault(ModComponentTypes.COLOR.get(), 0xFFFFFF);
        this.charge = components.getOrDefault(ModComponentTypes.CHARGE.get(), 0);
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
    //?}

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getTint() {
        return propertyDelegate.get(0);
    }

    public ItemStack asStack() {
        ItemStack itemStack = ModItems.BIOME_BOTTLE_ITEM.get().getDefaultInstance();
        //? if >1.21
        itemStack.applyComponents(this.collectComponents());
        return itemStack;
    }

    public boolean isTicking() {
        return from_charge > 0;
    }

    public void startTicking() {
        from_charge = charge;
    }

    public void empty() {
        color = 0xFFFFFF;
        biome = BiomeBottleBlock.defaultBiome();
        charge = 0;
        from_charge = 0;
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, BiomeBottleBlockEntity be) {
        if (be.isTicking() && world instanceof ServerLevel w) {
            if (w.getGameTime() % 20 == 0) {
                int level = be.charge/100;
                if (level <= 0) {
                    be.empty();
                }
                else {
                    int diff2 = be.charge > 1500 ? 500 : 100;
                    int diff = be.charge%diff2;
                    if (diff == 0) diff = diff2;
                    int charge_new = be.charge - diff;
                    BiomeBottleBlock.spreadRing(w, pos, be.biome, be.from_charge - be.charge, be.from_charge - charge_new);
                    be.charge = charge_new;
                    world.setBlockAndUpdate(pos, state.setValue(BiomeBottleBlock.LEVEL, Mth.clamp(level - 1, 0, 10)));
                    BiomeBottleBlock.playSploosh(w, pos);
                    be.setChanged();
                }
            }
        }
    }
}
