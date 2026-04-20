package net.chenjunfu2.crafter.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent CRAFTER_CRAFT = register("block.crafter.craft");
    public static final SoundEvent CRAFTER_FAIL = register("block.crafter.fail");

    private static SoundEvent register(String name) {
        final Identifier id = new Identifier(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {}
}
