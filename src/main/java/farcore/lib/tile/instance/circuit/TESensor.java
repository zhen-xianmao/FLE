package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class TESensor extends TECircuitBase
{
	private static final Facing[] OUT = {Facing.FRONT, Facing.BACK, Facing.LEFT, Facing.RIGHT};

	private byte value;
	
	protected abstract int getValue();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("power", value);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		value = nbt.getByte("power");
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("p", value);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("p"))
		{
			value = nbt.getByte("p");
		}
	}

	@Override
	protected void updateServer()
	{
		super.updateServer();
		if((getWorldTime() & 0x1) == 0)
		{
			int newvalue = getValue();
			if(newvalue != value)
			{
				notifyNeighbors();
				value = (byte) newvalue;
			}
		}
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		
	}

	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return value;
	}

	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return value;
	}

	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
}