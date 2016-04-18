package farcore.lib.net.world;

import java.io.IOException;

import farcore.lib.net.PacketChunkCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.U;
import farcore.util.io.DataStream;
import net.minecraft.world.World;

public class PacketWorldDataUpdateAll extends PacketChunkCoord
{
	int[] datas;
	
	public PacketWorldDataUpdateAll()
	{
		
	}
	public PacketWorldDataUpdateAll(World world, int x, int z, int[] datas)
	{
		super(world, x, z);
		this.datas = datas;
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		datas = input.readIntArray();
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeIntArray(datas);
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		U.Worlds.datas.loadChunkData(dimID, x, z, datas);
		return null;
	}
}