package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.PacketBufferExt;
import net.minecraft.world.World;

public abstract class PacketChunkCoord extends PacketWorld
{
	protected int x;
	protected int z;
	
	public PacketChunkCoord()
	{
		
	}
	public PacketChunkCoord(World world, int x, int z)
	{
		super(world);
		this.x = x;
		this.z = z;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeInt(x);
		output.writeInt(z);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		x = input.readInt();
		z = input.readInt();
	}
}