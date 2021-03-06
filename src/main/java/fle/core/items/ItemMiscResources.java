/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items;

import java.util.List;

import com.google.common.collect.Maps;

import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.core.FLE;
import fle.core.items.behavior.BehaviorBlockableTool;
import fle.core.items.behavior.BehaviorResearchItems1;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.item.ItemSubBehavior;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemMiscResources extends ItemSubBehavior implements IPolishableItem, IIP_Containerable
{
	public ItemMiscResources()
	{
		super(FLE.MODID, "misc_resource");
		initalize();
		EnumItem.misc_resource.set(this);
	}
	
	protected void initalize()
	{
		addSubItem(1, "flint_fragment", "Flint Fragment", null);
		addSubItem(2, "flint_sharp", "Sharp Flint Chip", null);
		addSubItem(3, "flint_small", "Small Flint", null);
		addSubItem(11, "quartz_large", "Quartz", null);
		addSubItem(12, "quartz_chip", "Quartz Chip", null);
		addSubItem(21, "opal", "Opal", null);
		
		addSubItem(1001, "vine_rope", "Vine Rope", null);
		addSubItem(1002, "dry_ramie_fiber", "Dried Ramie Fiber", null);
		addSubItem(1003, "hay", "Hay", null);
		addSubItem(1004, "dry_broadleaf", "Dried Broadleaf", null);
		addSubItem(1005, "dry_coniferous", "Dried Coniferous", null);
		addSubItem(1006, "tinder", "Tinder", null);
		
		addSubItem(2001, "ramie_rope", "Ramie Rope", null);
		addSubItem(2002, "ramie_rope_bundle", "Ramie Rope Bundle", null);
		addSubItem(2003, "crushed_bone", "Crushed Bone", null);
		addSubItem(2004, "defatted_crushed_bone", "Defatted Crushed Bone", null);
		
		addSubItem(3001, "researchitem1", "Research Item", null, new BehaviorResearchItems1());
		
		addSubItem(4001, "wooden_brick_mold", "Brick Mold", null);
		addSubItem(4002, "filled_wooden_brick_mold", "Brick Mold", null, new BehaviorBlockableTool(2));
		addSubItem(4003, "dried_filled_wooden_brick_mold", "Brick Mold", null);
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		MC.fragment.registerOre(M.flint, new ItemStack(this, 1, 1));
		MC.fragment.registerOre(M.quartz, new ItemStack(this, 1, 11));
		MC.chip_rock.registerOre(M.quartz, new ItemStack(this, 1, 12));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/misc_resource"));
		NebulaModelLoader.registerItemMetaGenerator(getRegistryName(), stack->this.nameMap.get(getBaseDamage(stack)));
		NebulaModelLoader.registerTextureSet(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/misc_resource/" + key)));
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
		case 3 : return  8;
		case 12 : return 11;
		default: return -1;
		}
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		switch (stack.getItemDamage())
		{
		case 3 : return 'c';
		case 12 : return 'c';
		default: return base;
		}
	}
	
	@Override
	public void onPolished(EntityPlayer player, ItemStack stack)
	{
		if (player == null || !player.capabilities.isCreativeMode)
		{
			stack.stackSize --;
		}
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> behaviors = getBehavior(stack);
		for (IBehavior behavior : behaviors)
		{
			if (behavior instanceof IIP_Containerable)
				return ((IIP_Containerable) behavior).openContainer(world, pos, player, stack);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> behaviors = getBehavior(stack);
		for (IBehavior behavior : behaviors)
		{
			if (behavior instanceof IIP_Containerable)
				return ((IIP_Containerable) behavior).openGui(world, pos, player, stack);
		}
		return null;
	}
}