package net.lerariemann.infinity.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.access.StructureAccess;
import net.lerariemann.infinity.registry.core.ModStructureTypes;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jspecify.annotations.NonNull;

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
    public @NonNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        Structure shadow = VersionMethods.getFromRegistry(VersionMethods.getRegistry(context.registryAccess(), Registries.STRUCTURE), VersionMethods.id(id));
        return shadow == null ? Optional.empty() : ((StructureAccess) shadow).invokeFindGenerationPoint(context);
    }

    @Override
    public @NonNull StructureType<?> type() {
        return ModStructureTypes.SETUPPER.get();
    }
}
