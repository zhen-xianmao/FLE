package fle.core.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleAPI;
import fle.api.material.IAtoms.EnumCountLevel;
import fle.api.material.Matter;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ItemOreStack;
import fle.api.util.IFuelHandler;
import fle.core.init.Config;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;

public class FleFuelHandler implements IFuelHandler
{
	@Override
	public float getFuelCalorificValue(FluidStack aStack, Matter aAirBase)
	{
		if(FleAPI.fluidDictionary.isFluidLava(aStack))
		{
			return aStack.amount * 100;
		}
		else if(FleAPI.fluidDictionary.matchFluid("oil", aStack))
		{
			return (float) ((double) aStack.amount * (2 + aAirBase.getIonContain(EnumCountLevel.Matter, Matter.mO2)) / 3);
		}
		return -1;
	}

	@Override
	public boolean getFuelRequireSmoke(FluidStack aStack, Matter aAirBase)
	{
		return false;
	}

	private final int powerCharcoal = Config.getInteger("pCharcoal");
	private final int powerFirewood = Config.getInteger("pFirewood");
	
	@Override
	public long getFuelCalorificValue(ItemStack aStack, Matter aAirBase)
	{
		if(aStack != null)
		{
			if(new ItemBaseStack(ItemFleSub.a("charred_log")).isStackEqul(aStack))
			{
				return (long) (powerCharcoal * 25000 / 4);
			}
			else if(new ItemBaseStack(new ItemStack(IB.peat)).isStackEqul(aStack))
			{
				return (long) (powerCharcoal * 25000 / 6);
			}
			else if(new ItemOreStack("logWood").isStackEqul(aStack))
			{
				return (long) (powerFirewood * 30000 / 4);
			}
			else if(new ItemOreStack("branchWood").isStackEqul(aStack) || new ItemBaseStack(ItemFleSub.a("branch_bush")).isStackEqul(aStack))
			{
				return (long) (powerFirewood * 30000 / 12);
			}
			else if(new ItemBaseStack(ItemFleSub.a("tinder")).isStackEqul(aStack))
			{
				return (long) (1600000);
			}
		}
		return 0;
	}

	@Override
	public ItemStack getFuelBurnedOutput(ItemStack aStack)
	{
		if(aStack != null)
		{
			if(new ItemBaseStack(ItemFleSub.a("charred_log")).isStackEqul(aStack))
			{
				return ItemFleSub.a("plant_ash");
			}
			else if(new ItemBaseStack(ItemFleSub.a("tinder")).isStackEqul(aStack))
			{
				return ItemFleSub.a("plant_ash");
			}
		}
		return null;
	}
}