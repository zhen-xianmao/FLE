package flapi.net;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flapi.te.TEBase;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public class FleNBTPacket extends FleCoordinatesPacket
{
	public NBTTagCompound nbt;
	
	public FleNBTPacket()
	{
		super(true);
	}
	public FleNBTPacket(INetEventHandler te)
	{
		super(true, te.getBlockPos());
		te.getBlockPos().getBlockTile().writeToNBT(nbt = new NBTTagCompound());
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeNBT(nbt);
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		nbt = is.readNBT();
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		World world = world();
		BlockPos pos = pos();
		if(pos.getBlockTile() == null)
		{
			world.setTileEntity(pos.x, pos.y, pos.z, TileEntity.createAndLoadEntity(nbt));
		}
		else
		{
			pos.getBlockTile().readFromNBT(nbt);
			if(pos.getBlockTile() instanceof TEBase)
				((TEBase) pos.getBlockTile()).markRenderForUpdate();
		}
		return null;
	}
}