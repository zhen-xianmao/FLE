package fle.load.recipe;

import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.FleCraftingManager;
import farcore.lib.recipe.IFleRecipe;
import farcore.lib.recipe.ShapedFleRecipe;
import farcore.lib.recipe.ShapelessFleRecipe;
import fle.api.recipe.LogCutRecipe;
import fle.api.recipe.ToolSingleCraftingRecipe;
import net.minecraft.item.ItemStack;

public class Crafting
{
	public static void init()
	{
		registerRecipe(new LogCutRecipe(new Ety(EnumToolType.axe.stack(), null)));
		registerShapelessRecipe(EnumItem.plant.instance(1, "vine_rope"), 40, "vine", "vine", "vine", "vine");
		Object[][] inputs2 = {
				{"stone", "Stone"},
				{"andesite", "Andesite"},
				{"basalt", "Basalt"},
				{"peridotite", "Peridotite"},
				{"rhyolite", "Rhyolite"},
				{"stone-compact", "StoneCompact"}};
		for(Object[] element : inputs2)
		{
			registerShapelessRecipe(EnumItem.cobble_block.instance(1, element[0]), 8, "chip" + element[1], "chip" + element[1], "chip" + element[1], "chip" + element[1]);
		}
		
		registerShapedRecipe(EnumItem.tool.instance(1, "bar_grizzly", "simple_bar_grizzly"), 120, true, "xo", "ox", 'x', "branchWood", 'o', "ropeVine");
		Object[][] inputs1 = {
					{"flint", "SharpFlint"},
					{"obsidian", "Obsidian"}};
		for(Object[] element : inputs1)
		{
			registerShapelessRecipe(EnumItem.tool.instance(1, "rough_stone_adz", element[0]), 100, "branchWood", "ropeVine", "chip" + element[1]);
		}
		registerRecipe(new ToolSingleCraftingRecipe("flint_hammer", 0.7F, 140, "branchWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("flint_hammer", 1.0F, 100, "stickWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_axe", 0.7F, 140, "branchWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_axe", 1.0F, 100, "stickWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_shovel", 0.7F, 140, "branchWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_shovel", 1.0F, 100, "stickWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_hammer", 0.7F, 140, "branchWood", "ropeVine"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_hammer", 1.0F, 100, "stickWood", "ropeVine"));
	}
	
	public static void registerShapedRecipe(ItemStack output, int tick, Object...objects)
	{
		FleCraftingManager.registerRecipe(new ShapedFleRecipe(output, tick, objects));
	}
	
	public static void registerShapelessRecipe(ItemStack output, int tick, Object...objects)
	{
		FleCraftingManager.registerRecipe(new ShapelessFleRecipe(output, tick, objects));
	}
	
	public static void registerRecipe(IFleRecipe recipe)
	{
		FleCraftingManager.registerRecipe(recipe);
	}
}