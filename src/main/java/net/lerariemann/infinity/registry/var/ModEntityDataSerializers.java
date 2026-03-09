package net.lerariemann.infinity.registry.var;

//? fabric && >1.21.2
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.entity.custom.HumanoidColors;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
//? if >1.21
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
//? neoforge
/*import net.neoforged.neoforge.registries.NeoForgeRegistries;*/

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
		//? fabric && >1.21.2 {
		FabricTrackedDataRegistry.register(id, HUMANOID_COLORS);
		//?} else if neoforge {
		/*Registry.register(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, id, HUMANOID_COLORS);
		*///?} else
		/*EntityDataSerializers.registerSerializer(HUMANOID_COLORS);*/
	}
}
