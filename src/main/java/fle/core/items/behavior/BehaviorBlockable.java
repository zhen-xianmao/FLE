package fle.core.items.behavior;

import farcore.lib.item.behavior.BehaviorBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorBlockable extends BehaviorBase
{
	public final Block block;
	public int usePerBlock;
	
	public BehaviorBlockable(Block block, int usePerBlock)
	{
		this.block = block;
		this.usePerBlock = usePerBlock;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(stack.stackSize < usePerBlock)
			return EnumActionResult.PASS;
		else
		{
			IBlockState iblockstate = world.getBlockState(pos);
			Block block = iblockstate.getBlock();
			
			if (!block.isReplaceable(world, pos))
			{
				pos = pos.offset(facing);
			}
			
			if (stack.stackSize != 0 && player.canPlayerEdit(pos, facing, stack) && world.canBlockBePlaced(this.block, pos, false, facing, (Entity)null, stack))
			{
				IBlockState iblockstate1 = this.block.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, stack.getItemDamage(), player);
				
				if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, iblockstate1))
				{
					SoundType soundtype = this.block.getSoundType();
					world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.stackSize -= usePerBlock;
				}
				
				return EnumActionResult.SUCCESS;
			}
			else
				return EnumActionResult.FAIL;
		}
	}
	
	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 3)) return false;
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == block)
		{
			//Set tile entity NBT in block place.
			//ItemBlockBase.setTileEntityNBT(world, player, pos, stack);
			block.onBlockPlacedBy(world, pos, state, player, stack);
		}
		
		return true;
	}
}