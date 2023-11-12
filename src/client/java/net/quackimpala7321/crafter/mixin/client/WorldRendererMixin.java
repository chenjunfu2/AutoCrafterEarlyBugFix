package net.quackimpala7321.crafter.mixin.client;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.quackimpala7321.crafter.registry.ModParticles;
import net.quackimpala7321.crafter.registry.ModSoundEvents;
import net.quackimpala7321.crafter.registry.ModWorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Unique
    private final WorldRendererAccessor _acc = (WorldRendererAccessor) this;

    private void shootParticles(int direction, BlockPos pos, Random random, DefaultParticleType particleType) {
        Direction direction2 = Direction.byId(direction);
        int i = direction2.getOffsetX();
        int j = direction2.getOffsetY();
        int k = direction2.getOffsetZ();
        double d = (double)pos.getX() + (double)i * 0.6 + 0.5;
        double e = (double)pos.getY() + (double)j * 0.6 + 0.5;
        double f = (double)pos.getZ() + (double)k * 0.6 + 0.5;

        for(int l = 0; l < 10; ++l) {
            double g = random.nextDouble() * 0.2 + 0.01;
            double h = d + (double)i * 0.01 + (random.nextDouble() - 0.5) * (double)k * 0.5;
            double m = e + (double)j * 0.01 + (random.nextDouble() - 0.5) * (double)j * 0.5;
            double n = f + (double)k * 0.01 + (random.nextDouble() - 0.5) * (double)i * 0.5;
            double o = (double)i * g + random.nextGaussian() * 0.01;
            double p = (double)j * g + random.nextGaussian() * 0.01;
            double q = (double)k * g + random.nextGaussian() * 0.01;
            _acc.invokeAddParticle(particleType, h, m, n, o, p, q);
        }

    }

    @Inject(method = "processWorldEvent", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addSoundWorldEvents(int eventId, BlockPos pos, int data, CallbackInfo ci, Random random) {
        if (_acc.getWorld() == null) return;
        switch (eventId) {
            case ModWorldEvents.CRAFTER_CRAFTS: {
                _acc.getWorld().playSound(
                        pos.getX(), pos.getY(), pos.getZ(),
                        ModSoundEvents.CRAFTER_CRAFT,
                        SoundCategory.BLOCKS,
                        1f, 1f, true);
                break;
            }
            case ModWorldEvents.CRAFTER_FAILS: {
                _acc.getWorld().playSound(
                        pos.getX(), pos.getY(), pos.getZ(),
                        ModSoundEvents.CRAFTER_FAIL,
                        SoundCategory.BLOCKS,
                        1f, 1f, true);
                break;
            }
            case ModWorldEvents.CRAFTER_SHOOTS: {
                shootParticles(
                        data, pos, random, ModParticles.WHITE_SMOKE);
                break;
            }
        }
    }
}
