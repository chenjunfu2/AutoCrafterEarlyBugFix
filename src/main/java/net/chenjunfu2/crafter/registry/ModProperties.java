package net.chenjunfu2.crafter.registry;

import net.minecraft.state.property.BooleanProperty;

public class ModProperties {
    public static final BooleanProperty CRAFTING;

    static {
        CRAFTING = BooleanProperty.of("crafting");
    }
}
