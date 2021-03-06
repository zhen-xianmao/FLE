/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.behavior;

import java.util.List;

import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.lib.block.instance.BlockFire;
import fle.api.recipes.instance.FlamableItems;
import nebula.Log;
import nebula.base.IntegerMap;
import nebula.common.item.ITool;
import nebula.common.util.Worlds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author ueyudiud
 */
public class BehaviorFirestarter extends BehaviorTool
{
	public static final FirestarterStorage STORAGE;
	@CapabilityInject(FirestarterCache.class)
	public static Capability<FirestarterCache> capability;
	
	static
	{
		CapabilityManager.INSTANCE.register(FirestarterCache.class, STORAGE = new FirestarterStorage(), FirestarterCache.class);
	}
	
	public static class FirestarterStorage implements IStorage<FirestarterCache>
	{
		@Override
		public NBTBase writeNBT(Capability<FirestarterCache> capability, FirestarterCache instance, EnumFacing side)
		{
			return new NBTTagByte((byte) 0);
		}
		
		@Override
		public void readNBT(Capability<FirestarterCache> capability, FirestarterCache instance, EnumFacing side,
				NBTBase nbt)
		{
			
		}
	}
	
	public static class FirestarterCache
	{
		IntegerMap<EntityItem> map = new IntegerMap<>();
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(facing);
		
		if (!player.canPlayerEdit(pos, facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			player.setActiveHand(hand);
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		World world = player.world;
		RayTraceResult result = Worlds.rayTrace(world, player, false);
		if (result != null)
		{
			switch (result.typeOfHit)
			{
			case BLOCK :
				List<EntityItem> list = world.getEntities(EntityItem.class, entity -> entity.getDistanceSq(result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord) < 0.04);
				FirestarterCache cache = stack.getCapability(capability, null);
				for (EntityItem item : list)
				{
					if (FlamableItems.isFlamable(item.getEntityItem()))
					{
						int tick = cache.map.getOrDefault(item, -1);
						if (FlamableItems.isSmoder(item.getEntityItem(), tick))
						{
							Worlds.spawnParticleWithRandomOffset(world, EnumParticleTypes.SMOKE_NORMAL, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, 0, 0.2F, 0, 0);
						}
						if (!world.isRemote && FlamableItems.isFlamable(item.getEntityItem(), tick))
						{
							BlockPos pos = result.getBlockPos().offset(result.sideHit);
							if (Worlds.isAirOrReplacable(world, pos) && ((BlockFire) EnumBlock.fire.block).canBlockStayAt(world, pos))
							{
								world.setBlockState(pos, EnumBlock.fire.block.getDefaultState(), 11);
							}
							item.setDead();
							cache.map.remove(item);
						}
						else
						{
							cache.map.put(item, tick + 1);
							Log.info("" + tick);
						}
					}
				}
				break;
			case ENTITY :
			default:
				player.stopActiveHand();
				break;
			}
		}
		super.onUsingTick(stack, player, count);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		super.onPlayerStoppedUsing(stack, world, entity, timeLeft);
		FirestarterCache cache = stack.getCapability(capability, null);
		cache.map.clear();
		((ITool) stack.getItem()).onToolUse(entity, stack, EnumToolTypes.FIRESTARTER, (stack.getMaxItemUseDuration() - timeLeft) * 0.02F);
	}
}