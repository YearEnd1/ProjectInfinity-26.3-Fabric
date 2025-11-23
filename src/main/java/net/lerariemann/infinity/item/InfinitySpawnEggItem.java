package net.lerariemann.infinity.item;

import dev.architectury.registry.registries.RegistrySupplier;
//? if <1.21.9 {
import dev.architectury.core.item.ArchitecturySpawnEggItem;
//?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.SpawnEggItem;

public class InfinitySpawnEggItem extends
        //? if >1.21.9 {
        /*SpawnEggItem
        *///?} else {
        ArchitecturySpawnEggItem
        //?}
          {


    //? if >1.21.9 {
    /*public InfinitySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> defaultType, int backgroundColor, int highlightColor, Properties properties) {
        super(properties.spawnEgg(defaultType.get()));
    }
    *///?} else if >1.21.5 {
    public InfinitySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> defaultType, int backgroundColor, int highlightColor, Properties properties) {
        super(defaultType, properties);
    }
    //?} else {
    /*public InfinitySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> defaultType, int backgroundColor, int highlightColor, Properties properties) {
        super(defaultType, backgroundColor, highlightColor, properties);
    }
  *///?}
}
