package net.quackimpala7321.crafter.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class CrafterRecipeCraftedCriterion extends AbstractCriterion<CrafterRecipeCraftedCriterion.Conditions>
{
	static final Identifier ID = new Identifier("crafter_recipe_crafted");
	
	public Identifier getId() {
		return ID;
	}
	
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "recipe_id"));
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("ingredients"));
		return new Conditions(lootContextPredicate, identifier, List.of(itemPredicates));
	}
	
	public void trigger(ServerPlayerEntity player, Identifier recipeId, List<ItemStack> ingredients) {
		this.trigger(player, (conditions) -> conditions.matches(recipeId, ingredients));
	}
	
	public static class Conditions extends AbstractCriterionConditions
	{
		private final Identifier recipeId;
		private final List<ItemPredicate> ingredients;
		
		public Conditions(LootContextPredicate player, Identifier recipeId, List<ItemPredicate> ingredients) {
			super(ID, player);
			this.recipeId = recipeId;
			this.ingredients = ingredients;
		}
		
		public static Conditions create(Identifier recipeId, List<ItemPredicate> ingredients) {
			return new Conditions(LootContextPredicate.EMPTY, recipeId, ingredients);
		}
		
		public static Conditions create(Identifier recipeId) {
			return new Conditions(LootContextPredicate.EMPTY, recipeId, List.of());
		}
		
		boolean matches(Identifier recipeId, List<ItemStack> ingredients) {
			if (!recipeId.equals(this.recipeId)) {
				return false;
			} else {
				List<ItemStack> list = new ArrayList(ingredients);
				
				for(ItemPredicate itemPredicate : this.ingredients) {
					boolean bl = false;
					Iterator<ItemStack> iterator = list.iterator();
					
					while(iterator.hasNext()) {
						if (itemPredicate.test((ItemStack)iterator.next())) {
							iterator.remove();
							bl = true;
							break;
						}
					}
					
					if (!bl) {
						return false;
					}
				}
				
				return true;
			}
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("recipe_id", this.recipeId.toString());
			if (! this.ingredients.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				for(ItemPredicate itemPredicate : this.ingredients) {
					jsonArray.add(itemPredicate.toJson());
				}
				
				jsonObject.add("ingredients", jsonArray);
			}
			
			return jsonObject;
		}
	}
}