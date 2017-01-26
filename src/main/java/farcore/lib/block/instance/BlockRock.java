/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.data.CT;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.EnumRockType;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.MP;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IExtendedDataBlock;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.block.IUpdateDelayBlock;
import farcore.lib.block.state.PropertyMaterial;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.material.Mat;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.util.Direction;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.lib.util.SubTag;
import farcore.lib.world.chunk.ExtendedBlockStateRegister;
import farcore.util.ItemStacks;
import farcore.util.U.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRock extends BlockBase
implements ISmartFallableBlock, IThermalCustomBehaviorBlock, IToolableBlock, IUpdateDelayBlock, IExtendedDataBlock
{
	public static final PropertyBool HEATED = PropertyBool.create("heated");
	public static final PropertyEnum<EnumRockType> ROCK_TYPE = PropertyEnum.create("type", EnumRockType.class);
	public static final PropertyMaterial ROCK_MATERIAL = PropertyMaterial.create("material", SubTag.ROCK);
	
	public static ItemStack stack(Mat material, EnumRockType type)
	{
		return ((BlockRock) EnumBlock.rock.block).createStackedBlock(material, type);
	}
	
	public BlockRock(String name)
	{
		super(name, Material.ROCK);
		setSoundType(SoundType.STONE);
		setTickRandomly(true);
		EnumBlock.rock.set(this);
		EnumBlock.rock.stateApplier = objects ->
		{
			Mat material;
			EnumRockType type = EnumRockType.resource;
			if(objects.length == 0) throw new IllegalArgumentException();
			material = (Mat) objects[0];
			if(objects.length >= 2)
				type = (EnumRockType) objects[1];
			return getDefaultState().withProperty(ROCK_MATERIAL, material).withProperty(ROCK_TYPE, type);
		};
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		
		final Function<ItemStack, IBlockState> applier = stack -> getStateFromData(getMetaFromStack(stack));
		for(EnumRockType type : EnumRockType.values())
		{
			ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
			for(Mat material : ROCK_MATERIAL.getAllowedValues())
			{
				ItemStack stack = createStackedBlock(material, type);
				LanguageManager.registerLocal(getTranslateNameForItemStack(stack), String.format(type.local, material.localName));
				builder.add(stack);
				switch (type)
				{
				case resource :
					OreDictExt.registerOreFunction(MC.stone.getOreName(material), this.item,
							((IDataChecker<IBlockState>) state -> state.getValue(ROCK_MATERIAL) == material && state.getValue(ROCK_TYPE) == type).from(applier), stack);
					break;
				case cobble :
				case mossy :
					OreDictExt.registerOreFunction(MC.cobble.getOreName(material), this.item,
							((IDataChecker<IBlockState>) state -> state.getValue(ROCK_MATERIAL) == material && state.getValue(ROCK_TYPE) == type).from(applier), stack);
					break;
				case brick :
				case brick_compacted :
				case brick_crushed :
				case brick_mossy :
					OreDictExt.registerOreFunction(MC.brickBlock.getOreName(material), this.item,
							((IDataChecker<IBlockState>) state -> state.getValue(ROCK_MATERIAL) == material && state.getValue(ROCK_TYPE) == type).from(applier), stack);
					break;
				default:
					break;
				}
			}
			List<ItemStack> list = builder.build();
			switch (type)
			{
			case resource :
				OreDictExt.registerOreFunction("stone", this.item,
						((IDataChecker<IBlockState>) state -> state.getValue(ROCK_TYPE) == type).from(applier), list);
				break;
			case cobble :
				OreDictExt.registerOreFunction("cobble", this.item,
						((IDataChecker<IBlockState>) state -> state.getValue(ROCK_TYPE) == type).from(applier), list);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void registerStateToRegister(ExtendedBlockStateRegister register)
	{
		register.registerStates(this, ROCK_MATERIAL, ROCK_TYPE, HEATED);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(FarCore.ID, "rock", ROCK_MATERIAL, HEATED);
		ModelLoader.setCustomStateMapper(this, mapper);
		ModelLoader.setCustomMeshDefinition(this.item,
				stack -> mapper.getModelResourceLocation(getStateFromData(getMetaFromStack(stack))));
		for(Mat material : ROCK_MATERIAL.getAllowedValues())
		{
			for(EnumRockType type : EnumRockType.values())
			{
				ModelLoader.registerItemVariants(this.item, mapper.getModelResourceLocation(EnumBlock.rock.apply(material, type)));
			}
		}
	}
	
	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		StringBuilder builder = new StringBuilder(super.getUnlocalizedName());
		int meta = ItemStacks.getOrSetupNBT(stack, false).getShort("data");
		Mat material = Mat.material(stack.getItemDamage());
		if(material == null) material = Mat.VOID;
		return builder.append("@").append(material.name).append(".").append(EnumRockType.values()[meta & 0xF].name()).toString();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_MATERIAL, ROCK_TYPE, HEATED);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(ROCK_MATERIAL, M.stone).withProperty(ROCK_TYPE, EnumRockType.resource).withProperty(HEATED, false);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getStateFromData(meta);
	}
	
	//Meta part | ID part
	//FFFF      | FFFF
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		int data = 0;
		data |= state.getValue(ROCK_MATERIAL).id;
		data |= state.getValue(ROCK_TYPE).ordinal() << 16;
		if(state.getValue(HEATED))
			data |= 0x100000;
		return data;
	}
	
	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		state = state.withProperty(HEATED, false);
		int data = getDataFromState(state);
		ItemStack stack = new ItemStack(this.item, 1, data & 0xFFFF);
		ItemStacks.getOrSetupNBT(stack, true).setShort("data", (short) ((data >> 16) & 0xFFFF));
		return stack;
	}
	
	private ItemStack createStackedBlock(Mat material, EnumRockType type)
	{
		ItemStack stack = new ItemStack(this.item, 1, material.id);
		ItemStacks.getOrSetupNBT(stack, true).setShort("data", (short) type.ordinal());
		return stack;
	}
	
	private int getMetaFromStack(ItemStack stack)
	{
		int data = stack.getItemDamage();
		int meta = ItemStacks.getOrSetupNBT(stack, false).getShort("data");
		data |= meta << 16;
		return data;
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		try
		{
			IBlockState state = getDefaultState();
			state = state.withProperty(ROCK_MATERIAL, Mat.material(meta & 0xFFFF));
			state = state.withProperty(ROCK_TYPE, EnumRockType.values()[(meta >> 16) & 0xFF]);
			if((meta & 0x100000) != 0) state = state.withProperty(HEATED, true);
			return state;
		}
		catch (Exception exception)
		{
			Log.warn("The id : {} is invalid for rock, use default id replaced.", meta);
			return getDefaultState();
		}
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[]{CT.tabBuilding, CT.tabTerria};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		if(tab == CT.tabTerria)
		{
			list.add(new ItemStack(itemIn, 1, EnumRockType.resource.ordinal()));
		}
		else
		{
			for(Mat material : ROCK_MATERIAL.getAllowedValues())
			{
				for(EnumRockType type : EnumRockType.values())
				{
					if(type.displayInTab)
					{
						list.add(createStackedBlock(material, type));
					}
				}
			}
		}
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		int data = getMetaFromStack(stackIn);
		Mat material = Mat.material(data & 0xFFFF);
		if(!ROCK_MATERIAL.getAllowedValues().contains(material))
			material = M.stone;
		state = state.withProperty(ROCK_MATERIAL, material);
		state = state.withProperty(ROCK_TYPE, EnumRockType.values()[(data >> 16) & 0xF]);
		return state;
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
		Mat material = state.getValue(ROCK_MATERIAL);
		EnumRockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case cobble_art:
			return 1;
		case cobble :
		case mossy :
			return material.getProperty(MP.property_rock).harvestLevel / 2;
		default:
			return material.getProperty(MP.property_rock).harvestLevel;
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		Mat material = state.getValue(ROCK_MATERIAL);
		
		List<ItemStack> ret = new ArrayList<>();
		if(silkTouch)
		{
			ret.add(new ItemStack(this, 1, state.getValue(ROCK_TYPE).ordinal()));
		}
		else
		{
			Random rand = world instanceof World ? ((World) world).rand : RANDOM;
			EnumRockType type = state.getValue(ROCK_TYPE);
			switch (type)
			{
			case resource:
			case cobble:
				ret.add(new ItemStack(EnumItem.stone_chip.item, rand.nextInt(4) + 3, material.id));
				break;
			case cobble_art:
				ret.add(new ItemStack(EnumItem.stone_chip.item, 9, material.id));
				break;
			default:
				ret.add(new ItemStack(this, 1, type.noSilkTouchDropMeta));
				break;
			}
		}
		return ret;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		Mat material = state.getValue(ROCK_MATERIAL);
		EnumRockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case resource :
			if(ThermalNet.getTemperature(worldIn, pos, false) > material.getProperty(MP.property_rock).minTemperatureForExplosion)
			{
				if(!state.getValue(HEATED) && random.nextInt(3) == 0)
				{
					worldIn.setBlockState(pos, state.withProperty(HEATED, true), 6);
				}
				else if(Worlds.isBlockNearby(worldIn, pos, EnumBlock.water.block, true))
				{
					worldIn.setBlockState(pos, state.withProperty(ROCK_TYPE, EnumRockType.cobble), 3);
					worldIn.playSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
							.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F, true);
					for (int k = 0; k < 8; ++k)
					{
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
					}
					return;
				}
			}
			else if(state.getValue(HEATED) && random.nextInt(4) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(HEATED, false), 6);
			}
		default:
			break;
		}
		updateTick(worldIn, pos, state, random);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!canBlockStay(worldIn, pos, state))
		{
			Worlds.fallBlock(worldIn, pos, state);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(HEATED))
		{
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			double u1, v1, t1;
			for(int i = 0; i < 2; ++i)
			{
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + v1, z - 0.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .1;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y, z + v1, 0D, t1, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.1, y + u1, z + v1, t1, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + v1, z + 1.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + 1D, z + v1, 0D, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 1.1, y + u1, z + v1, t1, 0D, 0D);
			}
		}
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		EnumRockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case cobble :
		case mossy :
		case cobble_art :
			return world.isSideSolid(pos.down(), EnumFacing.UP, false);
		default:
			return !world.isAirBlock(pos.down()) ? true :
				world.isSideSolid(pos.north(), EnumFacing.SOUTH, false) ||
				world.isSideSolid(pos.south(), EnumFacing.NORTH, false) ||
				world.isSideSolid(pos.east() , EnumFacing.WEST , false) ||
				world.isSideSolid(pos.west() , EnumFacing.EAST , false);
		}
	}
	
	public boolean canBlockStayTotally(IBlockAccess world, BlockPos pos, IBlockState state, Random rand)
	{
		EnumRockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case cobble :
		case mossy :
		case cobble_art :
			if(!world.isSideSolid(pos.down(), EnumFacing.UP, false))
				return false;
			BlockPos pos1 = pos.down();
			for(Direction direction : Direction.DIRECTIONS_2D)
			{
				if(world.isAirBlock(direction.offset(pos)) &&
						world.isAirBlock(direction.offset(pos1)) &&
						rand.nextInt(7) == 0)
					return false;
			}
			return true;
		case resource :
			if(!world.isAirBlock(pos.down())) return true;
			int c = 0;
			for(Direction direction : Direction.DIRECTIONS_3D)
			{
				if(world.isAirBlock(direction.offset(pos)))
				{
					c++;
				}
			}
			return c > 3 ? true : c == 3 ? rand.nextInt(9) != 0 : rand.nextInt(3) != 0;
		default :
			return !world.isAirBlock(pos.down());
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		worldIn.scheduleUpdate(pos, blockIn, tickRate(worldIn));
	}
	
	@Override
	public void notifyAfterTicking(IBlockState state, World world, BlockPos pos, IBlockState state1)
	{
		if(!canBlockStayTotally(world, pos, state, world.rand))
		{
			Worlds.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		if(!canBlockStay(worldIn, pos, state))
		{
			Worlds.fallBlock(worldIn, pos, state);
		}
	}
	
	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		IBlockState state;
		EnumRockType type = (state = world.getBlockState(pos)).getValue(ROCK_TYPE);
		if(type.burnable)
		{
			world.setBlockState(pos, state.withProperty(ROCK_TYPE, type.burned()), 3);
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
		return world.getBlockState(pos).getValue(ROCK_MATERIAL).getProperty(MP.property_basic).thermalConduct;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
		
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return canBlockStay(world, pos, state);
	}
	
	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
		boolean broken = height < 2 ? false : height < 5 ? RANDOM.nextInt(5 - height) == 0 : true;
		if(broken)
		{
			state = state.withProperty(ROCK_TYPE, EnumRockType.values()[state.getValue(ROCK_TYPE).fallBreakMeta]);
		}
		state = state.withProperty(HEATED, false);
		world.setBlockState(pos, state, 3);
		return true;
	}
	
	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return (float) ((1.0F + block.getBlock().getValue(ROCK_MATERIAL).getProperty(MP.property_tool).damageToEntity) * block.motionY * block.motionY * 0.25F);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(ROCK_TYPE).burnable;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(world, pos, face) ? 40 : 0;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 0;
	}
	
	@Override
	public void onHeatChanged(World world, BlockPos pos, Direction direction, double amount)
	{
		Mat material = world.getBlockState(pos).getValue(ROCK_MATERIAL);
		if(amount >= material.getProperty(MP.property_rock).minTemperatureForExplosion * material.getProperty(MP.property_basic).heatCap)
		{
			Worlds.switchProp(world, pos, HEATED, true, 2);
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		if(tool == EnumToolType.chisel)
		{
			if(player.canPlayerEdit(pos, side.of(), stack))
			{
				IBlockState state = world.getBlockState(pos);
				Mat material = state.getValue(ROCK_MATERIAL);
				EnumRockType type = state.getValue(ROCK_TYPE);
				if(world.setBlockState(pos, EnumBlock.carved_rock.block.getDefaultState(), 2))
				{
					TileEntity tile = world.getTileEntity(pos);
					if(tile instanceof TECustomCarvedStone)
					{
						((TECustomCarvedStone) tile).setRock(material, type);
						return ((TECustomCarvedStone) tile).carveRock(player, hitX, hitY, hitZ);
					}
				}
			}
		}
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public ActionResult<Float> onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
}