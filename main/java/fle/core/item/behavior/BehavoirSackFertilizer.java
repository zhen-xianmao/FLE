package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.crop.IFertilableBlock;
import fle.api.crop.IFertilableBlock.FertitleLevel;
import fle.api.item.ItemFleMetaBase;
import fle.api.soild.Solid;
import fle.api.soild.SolidTankInfo;
import fle.api.world.BlockPos;

public class BehavoirSackFertilizer extends BehaviorSack
{
	FertitleLevel lv;
	
	public BehavoirSackFertilizer(String str, Solid aSolid, int aCapacity, FertitleLevel aLevel)
	{
		super(str, aSolid, aCapacity);
		lv = aLevel;
	}

	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
		if(player.canPlayerEdit(x, y, z, FleAPI.getIndexFromDirection(side), itemstack))
		{
			if(world.getBlock(x, y, z) instanceof IFertilableBlock)
			{
				SolidTankInfo info = getTankInfo(itemstack);
				if(info.haveSolid() && info.solid.getSize() >= 36)
				{
					IFertilableBlock block = (IFertilableBlock) world.getBlock(x, y, z);
					if(block.needFertilize(new BlockPos(world, x, y, z), lv))
					{
						block.doFertilize(world, x, y, z, lv);
						drain(itemstack, 36, true);
						return true;
					}
				}
				return false;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
}