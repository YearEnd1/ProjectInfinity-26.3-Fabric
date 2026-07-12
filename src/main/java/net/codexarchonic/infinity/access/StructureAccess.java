package net.codexarchonic.infinity.access;

import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public interface StructureAccess {
    Optional<Structure.GenerationStub> invokeFindGenerationPoint(Structure.GenerationContext context);
}
