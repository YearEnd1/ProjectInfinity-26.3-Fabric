package net.lerariemann.infinity.util.var;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

public interface InfinitySpawnHelper {
    static <T extends Entity> boolean canSpawn(EntityType<T> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        boolean bl;
        MobCategory sg = type.getCategory();
        if (isSpawnGroupWater(sg) || (WaterAnimal.class.isAssignableFrom(type.getBaseClass())))
            bl = world.getFluidState(pos).is(FluidTags.WATER);
        else bl = (world.getBlockState(pos.below()).isValidSpawn(world, pos.below(), type));

        bl = bl || MobSpawnType.isSpawner(spawnReason);

        if (sg.equals(MobCategory.MONSTER))
            bl = bl && (Monster.isDarkEnoughToSpawn(world, pos, random) && world.getDifficulty() != Difficulty.PEACEFUL);
        if (BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals(InfinityMod.MOD_ID)) bl = bl && InfinityMethods.chaosMobsEnabled();
        return bl;
    }

    static boolean isSpawnGroupWater(MobCategory sg) {
        return switch (sg) {
            case WATER_CREATURE, WATER_AMBIENT, UNDERGROUND_WATER_CREATURE, AXOLOTLS -> true;
            default -> false;
        };
    }
}
