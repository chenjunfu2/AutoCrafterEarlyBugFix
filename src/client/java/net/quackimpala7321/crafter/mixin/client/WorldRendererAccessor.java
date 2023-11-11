package net.quackimpala7321.crafter.mixin.client;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Invoker("addParticle")
    <T extends ParticleEffect> void invokeAddParticle(T parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Accessor
    ClientWorld getWorld();
}
