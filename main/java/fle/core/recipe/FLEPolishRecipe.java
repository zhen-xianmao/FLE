package fle.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.cg.RecipesTab;
import fle.api.config.JsonLoader;
import fle.api.material.MaterialRock;
import fle.api.recipe.CraftingState;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.ColorMap;
import fle.core.block.BlockFleRock;
import fle.core.block.ItemOilLamp;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;

public class FLEPolishRecipe extends IRecipeHandler<PolishRecipe>
{
	private static final FLEPolishRecipe instance = new FLEPolishRecipe();
	
	public static void init()
	{
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("branch_bush")), "p pp pp p", new ItemStack(Items.stick)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(Items.bone), "cc c c cc", ItemTool.a("bone_needle", Materials.Bone)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " c ccc c ", ItemFleSub.a("flint_b", 4)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c c      ", new ItemStack(Items.flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "  c ccccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c  cc ccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c ccccccc", ItemToolHead.a("stone_shovel", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "     cccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c  ccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c cc c", ItemTool.a("flint_awl", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " cc cc cc", ItemTool.a("stone_knife", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cc cc cc ", ItemTool.a("stone_knife", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "   c cc c", ItemTool.a("flint_awl", Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), " cc cc cc", ItemTool.a("stone_knife", Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "cc cc cc ", ItemTool.a("stone_knife", Materials.Obsidian)));
		Object[][] o = {{"stone", Materials.Stone}, {"rhyolite", Materials.Rhyolite}, {"basalt", Materials.Basalt}, {"andesite", Materials.Andesite}, {"peridotite", Materials.Peridotite}};
		for(Object[] s : o)
		{
			ItemStack fragment = ItemFleSub.a("fragment_" + (String) s[0]);
			ItemStack chip = ItemFleSub.a("chip_" + (String) s[0]);
			MaterialRock material = (MaterialRock) s[1];
			Block m = BlockFleRock.a(material);
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(fragment), "pcp p p p", ItemOilLamp.a(m, 0)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(fragment), "ppp   ppp", ItemTool.a("whetstone", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(fragment), "ppp p    ", ItemTool.a("stone_decorticating_plate", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "p cpccccc", ItemToolHead.a("stone_axe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "c pccpccc", ItemToolHead.a("stone_axe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "cpcc cccc", ItemToolHead.a("stone_shovel", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "      ccc", ItemToolHead.a("stone_hammer", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "  pcc ccc", ItemToolHead.a("stone_sickle", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "p c     p", ItemToolHead.a("stone_spade_hoe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "c p   p  ", ItemToolHead.a("stone_spade_hoe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), "cp p p pc", ItemTool.a("stone_decorticating_stick", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(chip), " pcp pcp ", ItemTool.a("stone_decorticating_stick", material)));
		}
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("chip_stone")), "ppp   ppp", ItemFleSub.a("stone_plate")));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("chip_limestone")), "ccccccccc", ItemFleSub.a("dust_limestone")));
	}
	
	public static void postInit(JsonLoader loader)
	{
		instance.reloadRecipes(loader);
	}

	public static FLEPolishRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(PolishRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static boolean canPolish(ItemStack input) 
	{
		for(PolishRecipe recipe : instance.recipeSet)
		{
			if(recipe.input.isStackEqul(input))
			{
				return true;
			}
		}
		return false;
	}
	
	private FLEPolishRecipe() {}
	
	public static class PolishRecipe extends MachineRecipe
	{
		PolishRecipeKey key;
		RecipesTab tab;
		private ItemAbstractStack input;
		private char[] cs;
		public ItemStack output;
		
		public PolishRecipe(RecipesTab tab, ItemAbstractStack input, String str, ItemStack output)
		{
			this(tab, input, str.toCharArray(), output);
		}
		public PolishRecipe(RecipesTab tab, ItemAbstractStack input, char[] cs, ItemStack output)
		{
			this.tab = tab;
			this.input = input;
			this.cs = cs;
			this.output = output.copy();
		}
		public PolishRecipe(ItemAbstractStack input, ColorMap aMap, int xStart, int yStart, ItemStack output)
		{
			this.input = input;
			this.output = output;
			char cs[] = new char[9];
			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 3; ++j)
					cs[i + j * 3] = CraftingState.getState(aMap.getColorFromCrood(xStart + i, yStart + j)).getCharIndex();
			this.cs = cs;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			if(key == null)
			{
				key = new PolishRecipeKey(input, new String(cs));
			}
			return key;
		}
		
		public boolean matchRecipe(ItemStack target, char[] input)
		{
			if(input == null) return this.input.isStackEqul(target);
			if(this.input.isStackEqul(target))
			{
				for(int i = 0; i < input.length; ++i)
				{
					if(input[i] != cs[i]) return false;
				}
				return true;
			}
			return false;
		}
		
		public char[] getRecipeMap()
		{
			return cs;
		}
		
		public ItemAbstractStack getinput()
		{
			return input;
		}
		
		public RecipesTab getTab()
		{
			return tab;
		}
	}
	
	public static class PolishRecipeKey extends RecipeKey
	{
		ItemAbstractStack stack;
		ItemStack stack1;
		String key;

		public PolishRecipeKey(ItemAbstractStack aStack, String aKey)
		{
			stack = aStack;
			key = aKey;
		}
		public PolishRecipeKey(ItemStack aStack, String aKey)
		{
			if(stack1 != null)
				stack1 = aStack.copy();
			key = aKey;
		}
		
		@Override
		public int hashCode()
		{
			int code = 1;
			code = code * 31 + key.hashCode();
			return code;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			PolishRecipeKey key = (PolishRecipeKey) keyRaw;
			if(key.stack == null && key.stack1 == null) return false;
			if(stack != null)
			{
				if(key.stack1 != null && !stack.isStackEqul(key.stack1)) return false;
				if(key.stack != null && !stack.isStackEqul(key.stack)) return false;
			}
			else if(key.stack != null)
			{
				if(stack1 != null && !key.stack.isStackEqul(stack1)) return false;
				if(stack != null && !key.stack.isStackEqul(stack)) return false;
			}
			if(!key.key.equals(this.key)) return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:" + stack.toString() + "." + key;
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
	}
}