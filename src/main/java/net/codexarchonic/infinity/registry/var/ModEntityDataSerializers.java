package net.codexarchonic.infinity.registry.var;

//? fabric && =1.21.8
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
//? fabric && >26
//import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.entity.custom.HumanoidColors;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
//? if >1.21
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
//? neoforge
//import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModEntityDataSerializers {

	public static final EntityDataSerializer<HumanoidColors> HUMANOID_COLORS = new EntityDataSerializer<>() {
		//? if >1.21 {
		@Override
		public StreamCodec<? super FriendlyByteBuf, HumanoidColors> codec() {
			return HumanoidColors.STREAM_CODEC;
		}
		//?} else {
		/*@Override
		public void write(FriendlyByteBuf buffer, HumanoidColors value) {
			HumanoidColors.toNetwork(buffer, value);
		}

		@Override
		public HumanoidColors read(FriendlyByteBuf buffer) {
			return HumanoidColors.fromNetwork(buffer);
		}
		*///?}

		@Override
		public HumanoidColors copy(HumanoidColors value) {
			return new HumanoidColors(value.left_arm(), value.left_leg(), value.right_arm(), value.right_leg(), value.head(), value.hat(), value.body());
		}
	};

	public static void registerSerializers() {
		var id = VersionMethods.id(InfinityMod.MOD_ID, "colors");
		//? fabric && >26 {
		/*FabricEntityDataRegistry.register(id, HUMANOID_COLORS);
		*///?} else if fabric && >1.21.2 {
		FabricTrackedDataRegistry.register(id, HUMANOID_COLORS);
		//?} else if neoforge {
		/*Registry.register(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, id, HUMANOID_COLORS);
		*///?} else
		//EntityDataSerializers.registerSerializer(HUMANOID_COLORS);
	}
}
