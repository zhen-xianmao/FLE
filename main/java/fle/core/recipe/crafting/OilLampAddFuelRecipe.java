package fle.core.recipe.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import fle.api.FleValue;
import fle.api.recipe.ShapelessFleRecipe;
import fle.core.block.ItemOilLamp;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemTool;

public class OilLampAddFuelRecipe extends ShapelessFleRecipe
{
	private static final ItemStack lamp = new ItemStack(IB.oilLamp);
	
	int contain;
	
	public OilLampAddFuelRecipe(int aContain, Object recipeInput)
	{
		super(lamp, new Object[]{lamp, recipeInput});
		contain = aContain;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) 
	{
		ItemStack ret = super.getCraftingResult(var1);
		if(var1 == null)
			ItemOilLamp.setAmount(ret, contain);
		for(int i = 0; i < var1.getSizeInventory(); ++i)
			if(var1.getStackInSlot(i) != null)
				if(lamp.isItemEqual(var1.getStackInSlot(i)))
					ItemOilLamp.setAmount(ret, Math.min(contain + ItemOilLamp.getToolAmount(var1.getStackInSlot(i)), FleValue.CAPACITY[4]));
		return ret;
	}
}