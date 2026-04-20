package net.chenjunfu2.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.chenjunfu2.WhiteSmokeParticle;

public class ClientModParticles {
    public static void registerParticles() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.WHITE_SMOKE, WhiteSmokeParticle.Factory::new);
    }
}
