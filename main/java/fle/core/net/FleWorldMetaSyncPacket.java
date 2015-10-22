package fle.core.net;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.FLE;
import fle.api.enums.EnumWorldNBT;
import fle.api.net.FleAbstractPacket;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos.ChunkPos;

public class FleWorldMetaSyncPacket extends FleAbstractPacket<FleWorldMetaSyncPacket>
{
	int dim;
	ChunkPos pos;
	int[][] datas;
	
	public FleWorldMetaSyncPacket(int aDim, ChunkPos aPos, int[][] aDatas)
	{
		dim = aDim;
		pos = aPos;
		datas = aDatas.clone();
	}
	public FleWorldMetaSyncPacket()
	{
		
	}
	

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(dim);
		os.writeLong(pos.x);
		os.writeLong(pos.z);
		for(int[] array : datas)
		{
			os.writeIntArray(array);
		}
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		dim = is.readInt();
		long x = is.readLong();
		long z = is.readLong();
		pos = new ChunkPos(x, z);
		datas = new int[EnumWorldNBT.values().length][];
		for(int i = 0; i < datas.length; ++i)
		{
			datas[i] = is.readIntArray();
		}
	}

	@Override
	public IMessage onMessage(FleWorldMetaSyncPacket message, MessageContext ctx)
	{
		FLE.fle.getWorldManager().syncData(message.dim, message.pos, message.datas);
		return null;
	}
}