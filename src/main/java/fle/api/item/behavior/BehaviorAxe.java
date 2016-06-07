package fle.api.item.behavior;

import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import farcore.enums.EnumToolType;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class BehaviorAxe extends BehaviorDigableTool
{
	private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin });
	private static final Set toolClasses = Sets.newHashSet(EnumToolType.axe.name());
	
	private float damageVsEntity;
	
	public BehaviorAxe(float dve)
	{
		destroyBlockDamageBase = 1F;
		destroyBlockDamageHardnessMul = 0.2F;
		craftingDamage = 0.8F;
		hitEntityDamage = 3;
		damageVsEntity = dve;
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return blockCanHeaverst.contains(block) || super.canHarvestBlock(block, stack);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClasses;
	}
	
	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", damageVsEntity, 0));
	}
}