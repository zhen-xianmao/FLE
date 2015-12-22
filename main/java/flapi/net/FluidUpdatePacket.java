package flapi.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import flapi.FleAPI;
import flapi.gui.ContainerBase;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;

public class FluidUpdatePacket extends FleAbstractPacket
{

	private int id;
	private int tankIndex;
	private FluidStack infos;
	
	public FluidUpdatePacket()
	{
		
	}
	public FluidUpdatePacket(ContainerBase container, int selectTank)
	{
		id = container.windowId;
		tankIndex = selectTank;
		infos = container.fluidSlotList.get(selectTank).getStack();
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		id = is.readInt();
		tankIndex = is.readInt();
		infos = is.readFluidStack();
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(id);
		os.writeInt(tankIndex);
		os.writeFluidStack(infos);
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		EntityPlayer player = FleAPI.mod.getPlatform().getPlayerInstance();
		if(player.openContainer.windowId == id)
		{
			ContainerBase container = (ContainerBase) player.openContainer;
			container.fluidSlotList.get(tankIndex).setStack(infos);
		}
		return null;
	}
}