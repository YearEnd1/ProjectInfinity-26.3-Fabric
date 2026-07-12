package net.codexarchonic.infinity.mixin.core;

import net.codexarchonic.infinity.access.StructureAccess;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(Structure.class)
public abstract class StructureMixin implements StructureAccess {
    @Shadow
    protected abstract Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context);

    @Override
    public Optional<Structure.GenerationStub> invokeFindGenerationPoint(Structure.GenerationContext context) {
        return findGenerationPoint(context);
    }
}
