package fle.core.item.resource;

import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.interfaces.item.IItemInfo;
import farcore.lib.substance.SubstanceWood;
import fle.api.item.ItemResource;
import fle.api.item.behavior.BehaviorBlockable;
import fle.load.BlockItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemPlant extends ItemResource
{
	public ItemPlant()
	{
		super("plant");
		EnumItem.plant.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "seed_oak", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("oak")), "seed_oak");
		addSubItem(2, "seed_spruce", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("spruce")), "seed_spruce");
		addSubItem(3, "seed_birch", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("birch")), "seed_birch");
		addSubItem(4, "seed_ceiba", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("ceiba")), "seed_ceiba");
		addSubItem(5, "seed_acacia", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("acacia")), "seed_acacia");
		addSubItem(6, "seed_oak-black", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("oak-black")), "seed_oak-black");
		
		addSubItem(1001, "leaves_1", "leaves_1");
		addSubItem(1002, "leaves_2", "leaves_2");

		addSubItem(2001, "branch_oak", "branch_oak");
		addSubItem(2002, "branch_spruce", "branch_spruce");
		addSubItem(2003, "branch_birch", "branch_birch");
		addSubItem(2004, "branch_ceiba", "branch_ceiba");
		addSubItem(2005, "branch_acacia", "branch_acacia");
		addSubItem(2006, "branch_oak-black", "branch_oak-black");
	}
	
	@Override
	public void addSubItem(int id, String name, String iconName)
	{
		super.addSubItem(id, name, iconName);
	}
	
	@Override
	public void addSubItem(int id, String name, IItemInfo itemInfo, String iconName)
	{
		super.addSubItem(id, name, itemInfo, "fle:resource/plant/" + iconName);
	}
}