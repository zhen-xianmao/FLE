package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCoreSetup.ClientProxy;
import farcore.data.CT;
import farcore.data.EnumSlabState;
import farcore.data.EnumRockType;
import farcore.lib.block.BlockSlab;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.block.instance.old.BlockRockOld;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRockSlab extends BlockSlab implements IThermalCustomBehaviorBlock
{
	private final Mat material;
	private final BlockRockOld parent;
	private String localName;
	/**
	 * The meta of main block.
	 */
	private int meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockRockSlab[] group;
	public final PropertyRock property;
	
	public BlockRockSlab(int id, BlockRockOld parent, BlockRockSlab[] group, String name, Mat material,
			String localName)
	{
		super(name + ".slab", Material.ROCK);
		this.meta = id;
		this.group = group;
		this.parent = parent;
		this.material = parent.material;
		this.localName = localName;
		this.property = parent.property;
		setHardness(this.property.hardness * 0.6F);
		setResistance(this.property.explosionResistance * 0.4F);
		if(EnumRockType.values()[id].displayInTab)
		{
			setCreativeTab(CT.tabBuilding);
		}
		setTickRandomly(true);
		setDefaultState(getDefaultState().withProperty(BlockRockOld.HEATED, false));
	}
	
	@Override
	protected String getLocalName()
	{
		return this.localName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "rock/slab/" + this.material.name, null, BlockRockOld.HEATED);
		mapper.setVariants("type", EnumRockType.values()[this.meta].getName());
		ClientProxy.registerCompactModel(mapper, this, 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumSlabState.PROPERTY, BlockRockOld.HEATED);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		EnumRockType type = EnumRockType.values()[this.meta];
		switch (type)
		{
		case cobble_art:
			return 1;
		case cobble :
		case mossy :
			return this.property.harvestLevel / 2;
		default:
			return this.property.harvestLevel;
		}
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return EnumRockType.values()[this.meta].burnable;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(world, pos, face) ? 40 : 0;
	}
	
	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		if(isFlammable(world, pos, direction.of()))
		{
			U.Worlds.setBlock(world, pos, this.group[EnumRockType.values()[this.meta].noMossy],
					U.Worlds.getBlockMeta(world, pos), 3);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public double getThermalConduct(World world, BlockPos pos)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
}