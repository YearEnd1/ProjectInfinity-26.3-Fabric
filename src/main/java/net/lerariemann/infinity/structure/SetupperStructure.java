package net.lerariemann.infinity.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModStructureTypes;
import net.lerariemann.infinity.util.Methods;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import java.util.Optional;

public class SetupperStructure extends Structure {
    String id;
    public static final MapCodec<SetupperStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SetupperStructure.settingsCodec(instance),
            (Codec.STRING.fieldOf("id")).forGetter(a -> a.id)).apply(instance, SetupperStructure::new));

    SetupperStructure(StructureSettings config, String i) {
        super(config);
        id = i;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        Structure shadow = context.registryAccess().registryOrThrow(Registries.STRUCTURE).get(Methods.id(id));
        return shadow == null ? Optional.empty() : shadow.findGenerationPoint(context);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.SETUPPER.get();
    }
}
