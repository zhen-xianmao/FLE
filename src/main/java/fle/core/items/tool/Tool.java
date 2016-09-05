package fle.core.items.tool;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.EnumToolType;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceEntityAttack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class Tool implements IToolStat
{
	private EnumToolType type;

	protected Tool(EnumToolType type)
	{
		this.type = type;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return null;
	}

	@Override
	public EnumToolType getToolType()
	{
		return type;
	}

	@Override
	public void onToolCrafted(ItemStack stack, EntityPlayer player)
	{
	}

	@Override
	public float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos,
			IBlockState block)
	{
		return 1.0F;
	}

	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return 1.0F;
	}

	@Override
	public float getDamageVsEntity(ItemStack stack)
	{
		return 1.0F;
	}

	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return 1.0F;
	}

	@Override
	public float getMaxDurabilityMultiplier()
	{
		return 1.0F;
	}

	@Override
	public int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial)
	{
		return type.isToolClass(toolClass) ? baseMaterial.toolHarvestLevel : -1;
	}

	@Override
	public boolean canHarvestDrop(ItemStack stack, IBlockState state)
	{
		return true;
	}

	@Override
	public float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block)
	{
		return 1.0F;
	}

	@Override
	public DamageSource getDamageSource(EntityLivingBase user, Entity target)
	{
		String string = getDeathMessage(target, user);
		return new DamageSourceEntityAttack(type.name(),
				new TextComponentString(string.replace("(S)", "" + ChatFormatting.GREEN + user.getName() + ChatFormatting.RESET).replace("(M)", "" + ChatFormatting.RED + target.getName() + ChatFormatting.RESET)),
				user);
	}

	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return null;
	}

	@Override
	public boolean canBlock()
	{
		return false;
	}
}