package net.lerariemann.infinity.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModStructureTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import java.util.Optional;

public class PyramidStructure extends Structure {
    public static final MapCodec<PyramidStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PyramidStructure.settingsCodec(instance),
            (Codec.INT.fieldOf("top_y")).forGetter(a -> a.top_y),
            (Codec.INT.fieldOf("bottom_y")).forGetter(a -> a.top_y),
            (BlockStateProvider.CODEC.fieldOf("block")).forGetter(a -> a.block)).apply(instance, PyramidStructure::new));
    int top_y;
    int bottom_y;
    BlockStateProvider block;

    PyramidStructure(StructureSettings config, int i, int j, BlockStateProvider p) {
        super(config);
        top_y = i;
        bottom_y = j;
        block = p;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return PyramidStructure.onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, collector -> addPieces(collector, context));
    }

    private void addPieces(StructurePiecesBuilder collector, GenerationContext context) {
        collector.addPiece(PyramidGenerator.of(context, top_y, bottom_y, block));
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.PYRAMID.get();
    }
}
