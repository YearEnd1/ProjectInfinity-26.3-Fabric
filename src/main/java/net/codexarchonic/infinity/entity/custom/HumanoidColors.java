package net.codexarchonic.infinity.entity.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
//? if >1.21 {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
//?}

public class HumanoidColors {
	public static final Codec<HumanoidColors> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.INT.fieldOf("left_arm").forGetter(shapedRecipe -> shapedRecipe.left_arm),
							Codec.INT.fieldOf("left_leg").forGetter(shapedRecipe -> shapedRecipe.left_leg),
							Codec.INT.fieldOf("right_arm").forGetter(shapedRecipe -> shapedRecipe.right_arm),
							Codec.INT.fieldOf("right_leg").forGetter(shapedRecipe -> shapedRecipe.right_leg),
							Codec.INT.fieldOf("head").forGetter(shapedRecipe -> shapedRecipe.head),
							Codec.INT.fieldOf("hat").forGetter(shapedRecipe -> shapedRecipe.hat),
							Codec.INT.fieldOf("body").forGetter(shapedRecipe -> shapedRecipe.body)
							)
					.apply(instance, HumanoidColors::new)
	);

	//? if >1.21.2 {
	public static final StreamCodec<? super FriendlyByteBuf, HumanoidColors> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			HumanoidColors::left_arm,
			ByteBufCodecs.INT,
			HumanoidColors::left_leg,
			ByteBufCodecs.INT,
			HumanoidColors::right_arm,
			ByteBufCodecs.INT,
			HumanoidColors::right_leg,
			ByteBufCodecs.INT,
			HumanoidColors::head,
			ByteBufCodecs.INT,
			HumanoidColors::hat,
			ByteBufCodecs.INT,
			HumanoidColors::body,
			HumanoidColors::new
	);
	//?}

	public HumanoidColors(int left_arm, int left_leg, int right_arm, int right_leg, int head, int hat, int body) {
		this.left_leg = left_leg;
		this.left_arm = left_arm;
		this.right_leg = right_leg;
		this.right_arm = right_arm;
		this.head = head;
		this.hat = hat;
		this.body = body;
	}

	public HumanoidColors() {
		this(0,0,0,0,0,0,0);
	}

	private int left_leg;
	private int right_leg;
	private int left_arm;
	private int right_arm;
	private int body;
	private int head;
	private int hat;

	public HumanoidColors(int color) {
		this(color, color, color, color, color, color, color);
	}

	public static HumanoidColors decode(CompoundTag colors) {
		//? if >1.21 {
		return CODEC.decode(NbtOps.INSTANCE, colors).getOrThrow().getFirst();
		//?} else
		//return CODEC.decode(NbtOps.INSTANCE, colors).get().orThrow().getFirst();
	}

	public void putInt(String s, int integer) {
		switch (s) {
			case "body" -> body = integer;
			case "left_arm" -> left_arm = integer;
			case "right_arm" -> right_arm = integer;
			case "left_leg" -> left_leg = integer;
			case "right_leg" -> right_leg = integer;
			case "head" -> head = integer;
			case "hat" -> hat = integer;
		}
	}

	public Integer body() {
		return body;
	}

	public Integer hat() {
		return hat;
	}

	public Integer head() {
		return head;
	}

	public Integer right_arm() {
		return right_arm;
	}

	public Integer left_arm() {
		return left_arm;
	}

	public Integer left_leg() {
		return left_leg;
	}

	public Integer right_leg() {
		return right_leg;
	}

	public int get(String name) {
		return switch (name) {
			case "body" -> body;
			case "left_arm" -> left_arm;
			case "right_arm" -> right_arm;
			case "left_leg" -> left_leg;
			case "right_leg" -> right_leg;
			case "head" -> head;
			case "hat" -> hat;
			default -> throw new IllegalStateException("Unexpected value: " + name);
		};
	}

	public Tag encode() {
		//? if >1.21 {
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		//?} else {
		/*return CODEC.encodeStart(NbtOps.INSTANCE, this).get().orThrow();
		*///?}
	}

	//? if =1.21.1 {
	/*public static final StreamCodec<FriendlyByteBuf, HumanoidColors> STREAM_CODEC = StreamCodec.of(HumanoidColors::toNetwork, HumanoidColors::fromNetwork);
	 *///?}


	public static HumanoidColors fromNetwork(FriendlyByteBuf buffer) {
		return new HumanoidColors(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	public static void toNetwork(FriendlyByteBuf buffer, HumanoidColors humanoidColors) {
		buffer.writeInt(humanoidColors.left_leg);
		buffer.writeInt(humanoidColors.right_leg);
		buffer.writeInt(humanoidColors.left_arm);
		buffer.writeInt(humanoidColors.right_arm);
		buffer.writeInt(humanoidColors.body);
		buffer.writeInt(humanoidColors.head);
		buffer.writeInt(humanoidColors.hat);
	}
}
