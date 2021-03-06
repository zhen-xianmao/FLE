/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import fle.api.recipes.instance.FlamableItems;
import fle.api.recipes.instance.FuelHandler;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import net.minecraft.init.Items;

/**
 * @author ueyudiud
 */
public class Fuels
{
	public static void init()
	{
		FlamableItems.addFlamableItem(new BaseStack(Items.PAPER), 60F, 200F);
		FuelHandler.addFuel(new BaseStack(IBF.miscResources.getSubItem("dry_ramie_fiber")), 290F, 8000, 320000);
		FuelHandler.addFuel(new BaseStack(IBF.miscResources.getSubItem("dry_broadleaf")), 300F, 8000, 160000);
		FuelHandler.addFuel(new BaseStack(IBF.miscResources.getSubItem("dry_coniferous")), 310F, 8000, 80000);
		FuelHandler.addFuel(new BaseStack(IBF.miscResources.getSubItem("tinder")), 250F, 10000, 300000);
		for (PropertyWood property : Mat.filtAndGet(SubTags.WOOD, MP.property_wood))
		{
			FuelHandler.addFuel(new OreStack(MC.log.getOreName(property.material)), 500F, (int) (property.burnHeat * 1000), 10000000);
			FuelHandler.addFuel(new OreStack(MC.firewood.getOreName(property.material)), 440F, (int) (property.burnHeat * 1200), 3000000);
		}
	}
}