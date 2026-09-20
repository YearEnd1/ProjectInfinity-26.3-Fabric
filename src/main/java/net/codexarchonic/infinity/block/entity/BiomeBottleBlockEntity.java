package net.codexarchonic.infinity.block.entity;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.block.custom.BiomeBottleBlock;
import net.codexarchonic.infinity.registry.core.ModBlockEntities;
import net.codexarchonic.infinity.registry.core.ModBlocks;
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

import java.util.Optional;

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
                    BiomeBottleBlockEntity.this.color = value & 0xFFFFFF;
                }

            }
            public int getCount() {
                return 1;
            }
        };
        this.empty();
    }

    public void setCharge(int charge) {
        this.charge = charge;
        if (this.level != null)
            level.setBlockAndUpdate(this.getBlockPos(), ModBlocks.BIOME_BOTTLE.get()
                    .defaultBlockState()
                    .setValue(BiomeBottleBlock.LEVEL, BiomeBottleBlock.getLevel(charge)));
    }

    public void setBiome(Holder<Biome> biome) {
        //~ if >26 'location'->'identifier'
        if (biome.unwrapKey().isPresent()) setBiome(biome.value().getFoliageColor(), biome.unwrapKey().get().location());
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
        VersionMethods.saveBlockEntityData(tag, dataTag());
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
    //?} else {
    /*@Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        VersionMethods.saveBlockEntityData(tag, dataTag());
        return tag;
    }
    *///?}

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getTint() {
        return propertyDelegate.get(0);
    }

    public CompoundTag dataTag() {
        CompoundTag beData = new CompoundTag();
        beData.putInt("Charge", charge);
        beData.putInt("Color", color);
        beData.putString("Biome", biome.toString());
        if (from_charge > 0) beData.putInt("from_charge", from_charge);
        InfinityMod.LOGGER.info("Charge {} Color {} Biome {}", charge, color, biome);
        return beData;
    }

    public ItemStack updateStack(ItemStack itemStack) {
        //? if >1.21 {
        itemStack.applyComponents(this.collectComponents());
         //?} else {
        /*CompoundTag tag = new CompoundTag();
        VersionMethods.saveBlockEntityData(tag, dataTag());
        itemStack.setTag(tag);
        *///?}
        return itemStack;
    }

    public ItemStack asStack() {
        InfinityMod.LOGGER.info("boop");
        return updateStack(ModItems.BIOME_BOTTLE_ITEM.get().getDefaultInstance());
    }

   // /setblock 2 78 5 infinity:biome_bottle[level=10]{infdata:{Biome:"minecraft:forest",Charge:1000,Color:-10899920}}
   // {Count:1b,Slot:0b,id:"infinity:biome_bottle",tag:{infdata:{Biome:"minecraft:plains",Charge:0,Color:16777215}}}]}

    public void startTicking() {
        if (charge > 10000) charge = 10000;
        if (from_charge == 0) from_charge = charge;
    }

    public void empty() {
        color = 0xFFFFFF;
        biome = BiomeBottleBlock.defaultBiome();
        charge = 0;
        from_charge = 0;
    }

    static ResourceLocation _getBiome(Level world, BlockPos pos) {
        Holder<Biome> biome = world.getBiome(pos);
        //~ if >26 'location'->'identifier'
        if (biome.unwrapKey().isPresent()) return biome.unwrapKey().get().location();
        return BiomeBottleBlock.defaultBiome();
    }

    public void startTickingCosmic(Level world, BlockPos pos) {
        if (charge == 0) setBiome(world.getBiome(pos));
    }

    private Optional<ResourceLocation> _cachedBiomeAtPosition = Optional.empty();

    public static void serverTick(Level world, BlockPos pos, BlockState state, BiomeBottleBlockEntity be) {
        if (world.getGameTime() % 20 == 0 && world instanceof ServerLevel) {
            BlockState bs = world.getBlockState(pos.below());
            boolean bl = bs.is(ModBlocks.COSMIC_ALTAR.get()) && be.charge < 1000;
            if (bl) {
                if (be._cachedBiomeAtPosition.isEmpty()) be._cachedBiomeAtPosition = Optional.of(_getBiome(world, pos));
                bl = be._cachedBiomeAtPosition.get().equals(be.biome);
            }
            boolean bl2 = bs.is(ModBlocks.ALTAR.get()) && be.from_charge > 0;

            if (bl || bl2) {
                ServerLevel w = (ServerLevel)world;

                if (bl2) {
                    int diff = be.charge > 1500 ? 500 : 100;
                    int charge_new = be.charge - diff;
                    charge_new = Math.max(charge_new, 0);
                    BiomeBottleBlock.spreadRing(w, pos, be.biome, be.from_charge - be.charge, be.from_charge - charge_new);
                    be.charge = charge_new;
                    if (charge_new == 0) {
                        be.empty();
                    }
                }
                else {
                    be.charge += 100;
                    be.charge = Mth.clamp(be.charge, 0, 1000);
                }

                int level = BiomeBottleBlock.getLevel(be.charge);
                world.setBlockAndUpdate(pos, state.setValue(BiomeBottleBlock.LEVEL, level));
                BiomeBottleBlock.playSploosh(w, pos);
                be.setChanged();
            }
        }
    }
}
