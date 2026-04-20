package net.quackimpala7321.crafter.registry;

import net.minecraft.advancement.criterion.Criteria;
import net.quackimpala7321.crafter.criterion.CrafterRecipeCraftedCriterion;

public class ModCriteria
{
	public static final CrafterRecipeCraftedCriterion CRAFTER_RECIPE_CRAFTED = Criteria.register(new CrafterRecipeCraftedCriterion());
	public static void registerCriteria() {}
}
