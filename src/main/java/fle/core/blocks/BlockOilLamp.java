package fle.core.blocks;

import java.util.List;

import farcore.lib.block.BlockTE;
import farcore.lib.collection.IRegister;
import fle.core.FLE;
import fle.core.tile.tools.TEOilLamp;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOilLamp extends BlockTE
{
	public static final AxisAlignedBB AABB_OIL_LAMP = new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);

	public BlockOilLamp()
	{
		super(FLE.MODID, "oil.lamp", Material.ROCK);
		setLightOpacity(2);
	}

	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(property_TE, property_TE.parseValue("oillamp").get());
	}

	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register("oillamp", TEOilLamp.class);
		return true;
	}

	@Override
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_OIL_LAMP;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		super.getSubBlocks(itemIn, tab, list);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}