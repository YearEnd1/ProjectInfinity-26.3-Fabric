package net.lerariemann.infinity.data;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModCriteria;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.advancements.Advancement;
//? >1.21 {
import net.minecraft.advancements.AdvancementHolder;
import static net.minecraft.advancements.AdvancementRequirements.*;
import static net.minecraft.advancements.AdvancementRequirements.Strategy.OR;
import static net.minecraft.advancements.AdvancementType.TASK;
//?} else {
/*import static net.minecraft.advancements.FrameType.TASK;
import static net.minecraft.advancements.RequirementsStrategy.OR;
*///?}
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;



public class InfinityAdvancementProvider extends FabricAdvancementProvider {

	public InfinityAdvancementProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput
				//? >1.21
				, registryLookup
		);
	}

	@Override
	public void generateAdvancement(
									//? if >1.21 {
									HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer
									//?} else {
									/*Consumer<Advancement> consumer
									*///?}
									) {
		var acidTest = Advancement.Builder.advancement()
				//.parent(iridescence)
				.display(
						ModItems.TRANSFINITE_KEY.get(), // The display icon
						Component.translatable("advancements.infinity.acid_test.title"), // The title
						Component.literal("advancements.infinity.acid_test.description"), // The description
						null, // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
						TASK, // TASK, CHALLENGE, or GOAL
						true, // Show the toast when completing it
						true, // Announce it to chat
						false // Hide it in the advancement tab until it's achieved
				)
				.addCriterion("creeper", mobChangeCriterion("minecraft:creeper"))
				.addCriterion("skeleton", mobChangeCriterion("minecraft:skeleton"))
				.addCriterion("slime", mobChangeCriterion("minecraft:slime"))
				.addCriterion("pawn", mobChangeCriterion("infinity:chaos_pawn"))
				.requirements(OR)
				// Give the advancement an id
				.save(consumer, InfinityMod.MOD_ID + ":acid_test");
		// FIXME - generating advancements with parents appears to be broken on 1.20?
		//? if >1.21 {
		var multiverseRoot = Advancement.Builder.advancement()
				.parent(VersionMethods.id("minecraft", "story/enter_the_nether"))
				.display(
						Items.WRITABLE_BOOK, // The display icon
						Component.translatable("advancements.infinity.multiverse_start.title"), // The title
						Component.literal("advancements.infinity.multiverse_start.description"), // The description
						null, // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
						TASK, // TASK, CHALLENGE, or GOAL
						true, // Show the toast when completing it
						true, // Announce it to chat
						false // Hide it in the advancement tab until it's achieved
				)
				.addCriterion("infinity_dims_open", dimsOpenCriterion(1))
				// Give the advancement an id
				.save(consumer, InfinityMod.MOD_ID + ":multiverse_root");

		var allTime = Advancement.Builder.advancement()
				.parent(multiverseRoot)
				.display(
						Items.CLOCK, // The display icon
						Component.translatable("advancements.infinity.all_time.title"), // The title
						Component.literal("advancements.infinity.all_time.description"), // The description
						null, // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
						TASK, // TASK, CHALLENGE, or GOAL
						true, // Show the toast when completing it
						true, // Announce it to chat
						false // Hide it in the advancement tab until it's achieved
				)
				.addCriterion("infinity_dims_closed", dimsClosedCriterion(1))
				// Give the advancement an id
				.save(consumer, InfinityMod.MOD_ID + ":all_time");
		 //?}
	}

	public Criterion mobChangeCriterion(String data) {
		//? if >1.21 {
		return ModCriteria.CONVERT_MOB.get().createCriterion(new ModCriteria.DataConditions(Optional.empty(), data));
		//?} else {
		/*return new Criterion(ModCriteria.DataConditions.create(data, ModCriteria.CONVERT_MOB.get().getId()));
		*///?}
	}

	public Criterion dimsOpenCriterion(int data) {
		//? if >1.21 {
		return ModCriteria.DIMS_OPENED.get().createCriterion(new ModCriteria.ScoredConditions(Optional.empty(), data));
		//?} else {
		/*return new Criterion(ModCriteria.ScoredConditions.create(data, ModCriteria.DIMS_OPENED.get().getId()));
		 *///?}
	}

	public Criterion dimsClosedCriterion(int data) {
		//? if >1.21 {
		return ModCriteria.DIMS_CLOSED.get().createCriterion(new ModCriteria.ScoredConditions(Optional.empty(), data));
		 //?} else {
		/*return new Criterion(ModCriteria.ScoredConditions.create(data, ModCriteria.DIMS_CLOSED.get().getId()));
		*///?}
	}
}
//?}