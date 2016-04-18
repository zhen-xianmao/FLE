package fle.core.item.resource;

import farcore.enums.EnumItem;
import fle.api.item.ItemResource;
import net.minecraft.item.ItemStack;

public class ItemStoneFragment extends ItemResource
{
	public ItemStoneFragment()
	{
		super("stone.fragment");
		EnumItem.stone_fragment.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "stone", "stone");
		addSubItem(2, "andesite", "andesite");
		addSubItem(3, "basalt", "basalt");
		addSubItem(4, "limestone", "limestone");
		addSubItem(5, "netherrack", "netherrack");
		addSubItem(6, "obsidian", "obsidian");
		addSubItem(7, "peridotite", "peridotite");
		addSubItem(8, "rhyolite", "rhyolite");
		addSubItem(9, "sandstone", "sandstone");
		addSubItem(10, "stone-compact", "stone_compact");
		addSubItem(101, "flint", "flint");
	}
	
	@Override
	public void addSubItem(int id, String name, String iconName)
	{
		super.addSubItem(id, name, "fle:resource/stone_fragment/" + iconName);
	}
}