package farcore.lib.block.instance;

import static farcore.lib.util.Direction.D;
import static farcore.lib.util.Direction.E;
import static farcore.lib.util.Direction.N;
import static farcore.lib.util.Direction.Q;
import static farcore.lib.util.Direction.S;
import static farcore.lib.util.Direction.U;
import static farcore.lib.util.Direction.W;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.data.EnumBlock;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFire extends BlockBase
{
	public static final PropertyInteger STATE = PropertyInteger.create("state", 0, 15);
	public static final PropertyEnum<SpreadDir> SPREAD_PREFERENCE =
			PropertyEnum.create("spread_preference", SpreadDir.class);
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool UPPER = PropertyBool.create("up");
	public static final PropertyBool SMOLDER = PropertyBool.create("smoldering");
	
	private static enum SpreadDir implements IStringSerializable
	{
		up(U), down(D), north(N), south(S), east(E), west(W), unknown(Q);
		Direction direction;
		SpreadDir(Direction dir){direction = dir;}
		@Override
		public String getName(){return name();}
	}
	
	public BlockFire()
	{
		super("fire", Material.FIRE);
		setDefaultState(getDefaultState().withProperty(STATE, 0)
				.withProperty(NORTH, false)
				.withProperty(EAST, false)
				.withProperty(SOUTH, false)
				.withProperty(WEST, false)
				.withProperty(UPPER, false)
				.withProperty(SMOLDER, true));
		setTickRandomly(true);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Fire");
		EnumBlock.fire.set(this);
		setLightLevel(11);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, STATE, NORTH, EAST, SOUTH, WEST, UPPER, SMOLDER);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(STATE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(STATE, meta);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		boolean isCatchingRain;
		return canStayFire(worldIn, pos, EnumFacing.UP) ? state : state
				.withProperty(NORTH, Blocks.FIRE.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
				.withProperty(EAST,  Blocks.FIRE.canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
				.withProperty(SOUTH, Blocks.FIRE.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
				.withProperty(WEST,  Blocks.FIRE.canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
				.withProperty(UPPER, Blocks.FIRE.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN));
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		return new ArrayList();
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 28;
	}

	private boolean canBlockStayAt(World world, BlockPos pos)
	{
		return farcore.util.U.Worlds.isAirNearby(world, pos, true) &&
				(canStayFire(world, pos.down(), EnumFacing.UP) ||
						canStayFire(world, pos.up(), EnumFacing.DOWN) ||
						canStayFire(world, pos.north(), EnumFacing.SOUTH) ||
						canStayFire(world, pos.south(), EnumFacing.NORTH) ||
						canStayFire(world, pos.east(), EnumFacing.WEST) ||
						canStayFire(world, pos.west(), EnumFacing.EAST));
	}

	private boolean canBlockBurnAt(World world, BlockPos pos)
	{
		boolean isCatchRain = farcore.util.U.Worlds.isCatchingRain(world, pos, true);
		return canBurnFire(world, pos.down(), EnumFacing.UP, isCatchRain) ||
				canBurnFire(world, pos.up(), EnumFacing.DOWN, isCatchRain) ||
				canBurnFire(world, pos.north(), EnumFacing.SOUTH, isCatchRain) ||
				canBurnFire(world, pos.south(), EnumFacing.NORTH, isCatchRain) ||
				canBurnFire(world, pos.east(), EnumFacing.WEST, isCatchRain) ||
				canBurnFire(world, pos.west(), EnumFacing.EAST, isCatchRain);
	}

	private boolean canBurnFire(World world, BlockPos pos, EnumFacing side, boolean isCatchRain)
	{
		IBlockState state;
		return ((state = world.getBlockState(pos)).getBlock() instanceof IBurnCustomBehaviorBlock &&
				((IBurnCustomBehaviorBlock) state.getBlock()).canFireBurnOn(world, pos, side, isCatchRain)) ||
				state.getBlock().isFireSource(world, pos, side) ||
				state.getBlock().isFlammable(world, pos, side) && !isCatchRain;
	}

	private boolean canStayFire(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return world.isSideSolid(pos, side, false);
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		IBlockState newState = state;
		if(!canBlockStayAt(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		if (worldIn.getGameRules().getBoolean("doFireTick"))
		{
			if(state.getValue(SMOLDER))
			{
				int l = state.getValue(STATE) + random.nextInt(3) / 2;
				if(l > 15)
				{
					worldIn.setBlockToAir(pos);
				}
				else
				{
					FireLocateInfo info = new FireLocateInfo(2, worldIn, pos);
					tryCatchFire(info, state, worldIn, pos.east(), 15, random, l, W);
					tryCatchFire(info, state, worldIn, pos.west(), 15, random, l, E);
					tryCatchFire(info, state, worldIn, pos.down(), 15, random, l, U);
					tryCatchFire(info, state, worldIn, pos.up(), 15, random, l, D);
					tryCatchFire(info, state, worldIn, pos.north(), 15, random, l, S);
					tryCatchFire(info, state, worldIn, pos.south(), 15, random, l, N);
					if(canBlockBurnAt(worldIn, pos) && random.nextInt(3) == 0)
					{
						newState = newState.withProperty(SMOLDER, false);
					}
					newState = newState.withProperty(STATE, l);
					if(newState != state)
					{
						worldIn.setBlockState(pos, newState, 2);
					}
				}
			}
			else
			{
				updateTick(worldIn, pos, newState, random);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.getGameRules().getBoolean("doFireTick"))
		{
			if(!canBlockBurnAt(worldIn, pos))
			{
				if(rand.nextInt(19 - state.getValue(STATE)) == 0)
				{
					worldIn.setBlockState(pos, state.withProperty(SMOLDER, true), 2);
					return;
				}
			}
			int l = state.getValue(STATE);
			int range = l < 3 ? 3 : l < 8 ? 2 : l != 15 ? 1 : 0;
			FireLocateInfo info = new FireLocateInfo(range + 2, worldIn, pos);
			Direction off1 = state.getValue(SPREAD_PREFERENCE).direction;
			boolean isFireSource =
					info.isFireSource(0, +1, 0, 0) ||
					info.isFireSource(0, -1, 0, 1) ||
					info.isFireSource(0, 0, +1, 2) ||
					info.isFireSource(0, 0, -1, 3) ||
					info.isFireSource(+1, 0, 0, 4) ||
					info.isFireSource(-1, 0, 0, 5);
			if(!isFireSource)
			{
				int l1 = l + rand.nextInt(5) / 3;
				if(l1 > 15 && rand.nextInt(3) == 0)
				{
					worldIn.setBlockToAir(pos);
				}
				if(l1 != l)
				{
					worldIn.setBlockState(pos, state.withProperty(STATE, l1), 2);
				}
			}
			worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + rand.nextInt(14));
			boolean flag1 = worldIn.isBlockinHighHumidity(pos);

			int chance = 300;

			if (flag1)
			{
				chance -= 50;
			}

			tryCatchFire(info, state, worldIn, pos.east(), chance, rand, l, W);
			tryCatchFire(info, state, worldIn, pos.west(), chance, rand, l, E);
			tryCatchFire(info, state, worldIn, pos.down(), chance, rand, l, U);
			tryCatchFire(info, state, worldIn, pos.up(), chance, rand, l, D);
			tryCatchFire(info, state, worldIn, pos.north(), chance, rand, l, S);
			tryCatchFire(info, state, worldIn, pos.south(), chance, rand, l, N);
			if(range > 0)
			{
				for(int i = -range + off1.x; i <= range + off1.x; ++i)
				{
					for(int j = -range + off1.y; j <= range + off1.y; ++j)
					{
						for(int k = -range + off1.z; k <= range + off1.z; ++k)
							if(i != 0 || j != 0 || k != 0)
							{
								int speed;
								if((
										info.canBlockStay(i, j, k, 0) ||
										info.canBlockStay(i, j, k, 1) ||
										info.canBlockStay(i, j, k, 2) ||
										info.canBlockStay(i, j, k, 3) ||
										info.canBlockStay(i, j, k, 4) ||
										info.canBlockStay(i, j, k, 5)) &&
										(speed = info.getSpreadSpeed(i, j, k)) > 0)
								{
									chance = 100;
									chance *= Math.cbrt(
											(i + off1.x * .6) * (i + off1.x * .6) +
											(j + off1.y * .6) * (j + off1.y * .6) +
											(k + off1.z * .6) * (k + off1.z * .6));
									if(flag1)
									{
										chance /= 2;
									}
									BlockPos pos2 = pos.add(i, j, k);
									IBlockState state2;
									if(info.isCustomed(i, j, k) &&
											((IBurnCustomBehaviorBlock) (state = worldIn.getBlockState(pos2)).getBlock()).onBurn(worldIn, pos2, 1000F / chance, U))
									{
										continue;
									}
									if(chance > 0 && rand.nextInt(chance) < speed)
									{
										int l2 = Math.min(15, l + rand.nextInt(4) / 2);
										worldIn.setBlockState(pos2, getDefaultState().withProperty(STATE, l2).withProperty(SMOLDER, false), 3);
									}
								}
							}
					}
				}
			}
		}
	}
	
	private void tryCatchFire(FireLocateInfo info, IBlockState fireState, World worldIn, BlockPos pos, int chance, Random random, int age, Direction face)
	{
		int i = info.getFlammability(face.x, face.y, face.z);

		IBlockState iblockstate = worldIn.getBlockState(pos);
		if(iblockstate.getBlock() instanceof IBurnCustomBehaviorBlock &&
				((IBurnCustomBehaviorBlock) iblockstate.getBlock()).onBurningTick(worldIn, pos, random, face, fireState))
			return;
		if (random.nextInt(chance) < i)
		{
			if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos))
			{
				int j = Math.min(15, age + random.nextInt(5) / 4);
				worldIn.setBlockState(pos, getDefaultState().withProperty(STATE, Integer.valueOf(j)).withProperty(SMOLDER, false), 3);
			}
			else
			{
				iblockstate.getBlock().onBlockDestroyedByPlayer(worldIn, pos, iblockstate);
				worldIn.setBlockToAir(pos);
			}
		}
	}
	
	private float countBlockDencyInRange(World world, BlockPos pos, int range)
	{
		int count = 0;
		int air = 0;
		for(int i = -range; i <= range; ++i)
		{
			for(int j = -range; j <= range; ++j)
			{
				for(int k = -range; k <= range; ++k)
				{
					BlockPos pos2;
					if(world.isAirBlock(pos2 = pos.add(i, j, k)) && canBlockStayAt(world, pos2))
					{
						++air;
					}
					else if(world.getBlockState(pos2).getBlock() == this)
					{
						++air;
						++count;
					}
				}
			}
		}
		return (float) count / (float) air;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if(!canBlockStayAt(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos))
		{
			if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) && !canBlockBurnAt(worldIn, pos))
			{
				worldIn.setBlockToAir(pos);
			}
			else
			{
				worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + worldIn.rand.nextInt(10));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (rand.nextInt(24) == 0)
		{
			worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
		}

		if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !Blocks.FIRE.canCatchFire(worldIn, pos.down(), EnumFacing.UP))
		{
			if (Blocks.FIRE.canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
			{
				for (int j = 0; j < 2; ++j)
				{
					double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
					double d8 = pos.getY() + rand.nextDouble();
					double d13 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.FIRE.canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
			{
				for (int k = 0; k < 2; ++k)
				{
					double d4 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612D;
					double d9 = pos.getY() + rand.nextDouble();
					double d14 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.FIRE.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
			{
				for (int l = 0; l < 2; ++l)
				{
					double d5 = pos.getX() + rand.nextDouble();
					double d10 = pos.getY() + rand.nextDouble();
					double d15 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.FIRE.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
			{
				for (int i1 = 0; i1 < 2; ++i1)
				{
					double d6 = pos.getX() + rand.nextDouble();
					double d11 = pos.getY() + rand.nextDouble();
					double d16 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612D;
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.FIRE.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN))
			{
				for (int j1 = 0; j1 < 2; ++j1)
				{
					double d7 = pos.getX() + rand.nextDouble();
					double d12 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612D;
					double d17 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
		else
		{
			for (int i = 0; i < 3; ++i)
			{
				double d0 = pos.getX() + rand.nextDouble();
				double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
				double d2 = pos.getZ() + rand.nextDouble();
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	
	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return MapColor.TNT;
	}
	
	@Override
	public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	private class FireLocateInfo
	{
		int range;
		BlockPos pos;
		/**
		 * The information for fire update.
		 * The elementary list length is 13.
		 * 0 for boolean type prop
		 *
		 * 1-6 for spread speed
		 * 7-12 for flammability
		 */
		int[][][][] values;

		public FireLocateInfo(int range, World world, BlockPos pos)
		{
			this.range = range;
			this.pos = pos;
			int r1 = 2 * range + 1;
			values = new int[r1][r1][r1][13];
			for(int i = -range; i < range; ++i)
			{
				for(int j = -range; j < range; ++j)
				{
					for(int k = -range; k < range; ++k)
					{
						int[] list = values[i + range][j + range][k + range];
						BlockPos pos1 = pos.add(i, j, k);
						if(world.isAirBlock(pos1))
						{
							continue;
						}
						IBlockState state = world.getBlockState(pos1);
						if(state.getBlock() == BlockFire.this)
						{
							list[0] = 1;
							continue;
						}
						list[0] = 2;
						for(EnumFacing facing : EnumFacing.VALUES)
						{
							boolean isCatchingRaining = farcore.util.U.Worlds.isCatchingRain(world, pos1, true);
							if((state.getBlock() instanceof IBurnCustomBehaviorBlock &&
									((IBurnCustomBehaviorBlock) state.getBlock()).canFireBurnOn(world, pos1, facing, isCatchingRaining)) ||
									state.getBlock().isFlammable(world, pos1, facing))
							{
								list[0] |= 1 << (8 + facing.ordinal());
							}
							else if(state.getBlock().isFireSource(world, pos1, facing))
							{
								list[0] |= 1 << (16 + facing.ordinal());
							}
							if(state.getBlock() instanceof IBurnCustomBehaviorBlock)
							{
								list[0] |= 0x4;
							}
							list[1 + facing.ordinal()] = state.getBlock().getFlammability(world, pos1, facing);
							list[7 + facing.ordinal()] = state.getBlock().getFireSpreadSpeed(world, pos1, facing);
						}
					}
				}
			}
		}
		
		public int getSpreadSpeed(BlockPos pos)
		{
			return getSpreadSpeed(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		}
		public int getSpreadSpeed(int ofX, int ofY, int ofZ)
		{
			int value = 0;
			value = Math.max(value, values[ofX + range][ofY + range + 1][ofZ + range][1]);
			value = Math.max(value, values[ofX + range][ofY + range - 1][ofZ + range][2]);
			value = Math.max(value, values[ofX + range][ofY + range][ofZ + range + 1][3]);
			value = Math.max(value, values[ofX + range][ofY + range][ofZ + range - 1][4]);
			value = Math.max(value, values[ofX + range + 1][ofY + range][ofZ + range][5]);
			value = Math.max(value, values[ofX + range - 1][ofY + range][ofZ + range][6]);
			return value;
		}
		
		public int getFlammability(BlockPos pos)
		{
			return getFlammability(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		}
		public int getFlammability(int ofX, int ofY, int ofZ)
		{
			int value = 0;
			value = Math.max(value, values[ofX + range][ofY + range + 1][ofZ + range][7]);
			value = Math.max(value, values[ofX + range][ofY + range - 1][ofZ + range][8]);
			value = Math.max(value, values[ofX + range][ofY + range][ofZ + range + 1][9]);
			value = Math.max(value, values[ofX + range][ofY + range][ofZ + range - 1][10]);
			value = Math.max(value, values[ofX + range + 1][ofY + range][ofZ + range][11]);
			value = Math.max(value, values[ofX + range - 1][ofY + range][ofZ + range][12]);
			return value;
		}

		public boolean isAir(BlockPos pos)
		{
			return isAir(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		}
		public boolean isAir(int ofX, int ofY, int ofZ)
		{
			return values[ofX + range][ofY + range][ofZ + range][0] == 0;
		}

		public boolean isFire(BlockPos pos)
		{
			return isFire(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		}
		public boolean isFire(int ofX, int ofY, int ofZ)
		{
			return values[ofX + range][ofY + range][ofZ + range][0] == 1;
		}

		public boolean isFlammable(BlockPos pos, EnumFacing facing)
		{
			return isFlammable(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ(), facing.ordinal());
		}
		public boolean isFlammable(int ofX, int ofY, int ofZ, int facing)
		{
			return (values[ofX + range][ofY + range][ofZ + range][0] & (1 << (facing + 8))) != 0;
		}

		public boolean isCustomed(BlockPos pos)
		{
			return isCustomed(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		}
		public boolean isCustomed(int ofX, int ofY, int ofZ)
		{
			return (values[ofX + range][ofY + range][ofZ + range][0] & 0x4) != 0;
		}

		public boolean isFireSource(BlockPos pos, EnumFacing facing)
		{
			return isFireSource(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ(), facing.ordinal());
		}
		public boolean isFireSource(int ofX, int ofY, int ofZ, int facing)
		{
			return (values[ofX + range][ofY + range][ofZ + range][0] & (1 << (facing + 16))) != 0;
		}

		public boolean canBlockStay(BlockPos pos, EnumFacing facing)
		{
			return canBlockStay(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ(), facing.ordinal());
		}
		public boolean canBlockStay(int ofX, int ofY, int ofZ, int facing)
		{
			return
					(values[ofX + range + 1][ofY + range][ofZ + range][0] & 0x2) == 0 ||
					(values[ofX + range - 1][ofY + range][ofZ + range][0] & 0x2) == 0 ||
					(values[ofX + range][ofY + range + 1][ofZ + range][0] & 0x2) == 0 ||
					(values[ofX + range][ofY + range - 1][ofZ + range][0] & 0x2) == 0 ||
					(values[ofX + range][ofY + range][ofZ + range + 1][0] & 0x2) == 0 ||
					(values[ofX + range][ofY + range][ofZ + range - 1][0] & 0x2) == 0;
		}
	}
}