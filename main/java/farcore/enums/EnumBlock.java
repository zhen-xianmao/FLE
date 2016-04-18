package farcore.enums;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public enum EnumBlock 
{
	/**
	 * Far Land Era fire, registered by fle.<br>
	 * <code>spawn(world, x, y, z)</code><br>
	 * Spawn a new fire block which will not extinguished by update.
	 * <code>spawn(world, x, y, z, level)</code><br>
	 * Spawn a new fire block with select level.
	 * @param level The level of fire, higher level will cause fire spread in a larger range.
	 * The value is from 1 to 15.
	 */
	fire,
	/**
	 * Far Land Era water, registed by fle.<br>
	 * The method spawn use way see fluid block type.
	 * @see farcore.block.BlockStandardFluid
	 */
	water, 
	lava, 
	ice, 
	rock,
	cobble, 
	sand;
	
	boolean init = false;
	Block block;
	EnumItem item;

	public void setBlock(Block block)
	{
		setBlock(block, null);
	}
	public void setBlock(Block block, EnumItem item)
	{
		if(init)
		{
			throw new RuntimeException("The block " + name() + " has already init!");
		}
		this.item = item;
		this.block = block;
		this.init = true;
	}
	
	public Block block()
	{
		return block;
	}
	
	public EnumItem item()
	{
		return item;
	}
	
	public void spawn(World world, int x, int y, int z)
	{
		if(init)
		{
			world.setBlock(x, y, z, block);
		}
	}
	
	public void spawn(World world, int x, int y, int z, int meta)
	{
		if(init)
		{
			spawn(world, x, y, z, meta, 3);
		}
	}
	
	public void spawn(World world, int x, int y, int z, int meta, int type)
	{
		if(init)
		{
			world.setBlock(x, y, z, block, meta, type);
		}
	}
	
	public void spawn(World world, int x, int y, int z, Object...objects)
	{
		if(init)
		{
			if(block instanceof IInfoSpawnable)
			{
				((IInfoSpawnable) block).spawn(world, x, y, z, objects);
			}
			else if(objects.length == 0)
			{
				spawn(world, x, y, z);
			}
			else if(objects.length == 1 && objects[0] instanceof Integer)
			{
				spawn(world, x, y, z, ((Integer) objects[0]).intValue());
			}
		}
	}
	
	public static interface IInfoSpawnable
	{
		/**
		 * Spawn a block with custom data in world.
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @param objects Custom data.
		 * @return Whether spawn is succeed.
		 */
		boolean spawn(World world, int x, int y, int z, Object...objects);
	}
}