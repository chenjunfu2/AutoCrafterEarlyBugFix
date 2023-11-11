package net.quackimpala7321.crafter.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.crafter.AutocrafterEarly;

public class ModParticles {
    public static final DefaultParticleType WHITE_SMOKE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(AutocrafterEarly.MOD_ID, "white_smoke"), FabricParticleTypes.simple(false));

    public static void registerParticles() {}
}
