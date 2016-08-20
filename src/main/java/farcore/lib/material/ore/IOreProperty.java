package farcore.lib.material.ore;

import java.util.Random;

import javax.annotation.Nullable;

import farcore.data.EnumToolType;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IOreProperty
{
	static IOreProperty property = new OrePropStandard();

	void updateTick(TEOre ore, Random rand);
	
	boolean onBlockClicked(TEOre ore, EntityPlayer playerIn, Direction side);

	void onEntityWalk(TEOre ore, Entity entityIn);
	
	EnumActionResult onBlockActivated(TEOre ore, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, Direction side, float hitX, float hitY, float hitZ);

	boolean onBurn(TEOre ore, float burnHardness, Direction direction);

	boolean onBurningTick(TEOre ore, Random rand, Direction fireSourceDir, IBlockState fireState);
	
	float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore,
			Direction side, float hitX, float hitY, float hitZ);
	
	float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore,
			Direction side, float hitX, float hitY, float hitZ, long tick);
	
	@SideOnly(Side.CLIENT)
	void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, TEOre ore, Random rand);
}