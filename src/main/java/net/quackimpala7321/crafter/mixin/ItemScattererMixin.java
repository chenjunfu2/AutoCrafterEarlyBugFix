package net.quackimpala7321.crafter.mixin;

import net.minecraft.util.ItemScatterer;
import net.quackimpala7321.crafter.ItemScattererAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin implements ItemScattererAccessor {
}
