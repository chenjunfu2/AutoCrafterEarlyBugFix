package net.chenjunfu2.crafter.mixin;

import net.minecraft.util.ItemScatterer;
import net.chenjunfu2.crafter.util.ItemScattererAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin implements ItemScattererAccessor {
}
