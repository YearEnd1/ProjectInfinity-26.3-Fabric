package net.codexarchonic.infinity.dimensions.features.top_layer;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class RandomBonusChest extends RandomisedFeature {
    boolean isFrequent;

    public RandomBonusChest(RandomFeaturesList lst) {
        super(lst, "chest");
        id = "infinity:bonus_chest";
        isFrequent = random.nextBoolean();
        savePlacement();
    }

    @Override
    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        config.putString("loot", random.nextBoolean() ? BuiltInLootTables.SPAWN_BONUS_CHEST
                //? if >1.21 {
                //~ if >26 'location'->'identifier'
                .location()
                //?}
                .toString() :
                random.nextBoolean() ? "infinity:random" : PROVIDER.randomName(random, ConfigType.LOOT_TABLES));
        config.put("block", NbtUtils.nameToElement(random.nextBoolean() ? "minecraft:chest" :
                isFrequent ? "minecraft:barrel" : PROVIDER.randomName(random, ConfigType.CHESTS)));
        return feature(config);
    }

    @Override
    public ListTag placement() {
        Placement res = new Placement();
        if (isFrequent) {
            res.addCountEveryLayer(random.nextInt(1, 8));
        } else {
            res.addCountEveryLayer(1);
            res.addRarityFilter(random.nextInt(1, 8));
        }
        res.addBiome();
        return res.data;
    }
}
