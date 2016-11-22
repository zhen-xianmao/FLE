package farcore.lib.tile.abstracts;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.nbt.NBTSynclizedCompound;
import farcore.lib.net.tile.PacketTESAsk;
import farcore.lib.net.tile.PacketTESync;
import farcore.lib.tile.ISynchronizableTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.world.EnumSkyBlock;

public class TESynchronization extends TEBuffered
implements ISynchronizableTile
{
	/**
	 * This a state assistant.
	 * Use to contain boolean state.
	 */
	protected long state;
	private long lastState;

	private boolean markChanged;
	private boolean initialized = false;
	public NBTSynclizedCompound nbt = new NBTSynclizedCompound();

	/**
	 * The sync state.
	 * 1 for mark to all.
	 * 2 for mark to dim.
	 * 4 for mark to near by.
	 * 8 for mark block update.
	 * 16 for mark render update.
	 * 32 for mark sky light update.
	 * 64 for mark block light update.
	 * 128 for mark dirty (mark this tile entity ant chunk is required save).
	 */
	public long syncState = 0L;
	private List<EntityPlayer> syncAskedPlayer = new ArrayList();

	public TESynchronization()
	{

	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("state", state);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		state = nbt.getLong("state");
	}

	@Override
	public void syncToAll()
	{
		syncState |= 0x1;
	}

	@Override
	public void syncToDim()
	{
		syncState |= 0x2;
	}

	@Override
	public void syncToNearby()
	{
		syncState |= 0x4;
	}

	@Override
	public void syncToPlayer(EntityPlayer player)
	{
		syncAskedPlayer.add(player);
	}

	@Override
	public void markBlockUpdate()
	{
		syncState |= 0x8;
	}

	@Override
	public void markBlockRenderUpdate()
	{
		syncState |= 0x10;
	}

	@Override
	public void markDirty()
	{
		syncState |= 0x80;
	}
	
	@Override
	public void markLightForUpdate(EnumSkyBlock type)
	{
		if(type == EnumSkyBlock.SKY)
		{
			syncState |= 0x20;
		}
		if(type == EnumSkyBlock.BLOCK)
		{
			syncState |= 0x40;
		}
	}

	@Override
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public final void readFromDescription(NBTTagCompound nbt)
	{
		readFromDescription1(nbt);
		//Needn't always update render and block states.
		//		markBlockUpdate();
		//		markBlockRenderUpdate();
		initialized = true;
	}

	public void readFromDescription1(NBTTagCompound nbt)
	{
		if(nbt.hasKey("s"))
		{
			state = nbt.getLong("s");
		}
	}

	public void writeToDescription(NBTTagCompound nbt)
	{
		nbt.setLong("s", state);
	}

	//TESynchronization use custom packet for sync.
	@Override
	@Deprecated
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return null;
	}

	@Override
	@Deprecated
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
	}
	
	@Override
	public void onLoad()
	{
		if(isServer())
		{
			initServer();
		}
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		nbt.merge(super.getUpdateTag());
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		if(!initialized)
		{
			initClient(tag);
		}
		else
		{
			readFromDescription(tag);
		}
	}
	
	@Override
	protected final void updateEntity2()
	{
		if(isServer())
		{
			updateServer();
			updateServerStates();
		}
		else
		{
			updateClient();
			updateClientStates();
		}
	}

	protected void updateServerStates()
	{
		if(lastState != state)
		{
			onStateChanged(true);
			lastState = state;
		}
		nbt.reset();
		writeToDescription(nbt);
		if((syncState & 0x1) != 0)
		{
			sendToAll(new PacketTESync(worldObj, pos, nbt.getChanged(true)));
		}
		else if((syncState & 0x2) != 0)
		{
			sendToDim(new PacketTESync(worldObj, pos, nbt.getChanged(true)));
		}
		else if((syncState & 0x4) != 0)
		{
			sendToNearby(new PacketTESync(worldObj, pos, nbt.getChanged(true)), getSyncRange());
		}
		if((syncState & 0x8) != 0)
		{
			worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
		}
		if((syncState & 0x80) != 0)
		{
			super.markDirty();
		}
		int state = 0;
		if((syncState & 0x10) != 0) { state |= 0x1; }
		if((syncState & 0x20) != 0) { state |= 0x2; }
		if((syncState & 0x40) != 0) { state |= 0x4; }
		sendToNearby(new PacketTESAsk(worldObj, pos, state), getSyncRange());
		onCheckingSyncState();
		syncState = 0;
		if(!syncAskedPlayer.isEmpty())
		{
			for(EntityPlayer player : syncAskedPlayer)
			{
				sendToPlayer(new PacketTESync(worldObj, pos, nbt.asCompound()), player);
			}
		}
		syncAskedPlayer.clear();
	}

	protected void updateClientStates()
	{
		if(lastState != state)
		{
			onStateChanged(false);
			lastState = state;
		}
		if((syncState & 0x8) != 0)
		{
			super.markBlockUpdate();
		}
		if((syncState & 0x10) != 0)
		{
			int range = getRenderUpdateRange();
			worldObj.markBlockRangeForRenderUpdate(pos.add(-range, -range, -range), pos.add(range, range, range));
		}
		if((syncState & 0x20) != 0)
		{
			super.markLightForUpdate(EnumSkyBlock.SKY);
		}
		if((syncState & 0x40) != 0)
		{
			super.markLightForUpdate(EnumSkyBlock.BLOCK);
		}
		onCheckingSyncState();
		syncState = 0;
		syncAskedPlayer.clear();
	}

	
	/**
	 * Called when checking state.
	 */
	protected void onCheckingSyncState()
	{
		
	}


	protected void onStateChanged(boolean isServerSide)
	{
		if(isServerSide)
		{
			syncToNearby();
		}
		else
		{
			markBlockUpdate();
			markBlockRenderUpdate();
		}
	}
	
	protected float getSyncRange()
	{
		return 16F;
	}

	protected int getRenderUpdateRange()
	{
		return 3;
	}

	protected void initServer()
	{
		initialized = true;
	}

	protected void initClient(NBTTagCompound nbt)
	{
		readFromDescription(nbt);
	}

	protected void updateServer()
	{

	}
	

	protected void updateClient()
	{

	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		onRemoveFromLoadedWorld();
	}
	

	@Override
	public void invalidate()
	{
		super.invalidate();
		onRemoveFromLoadedWorld();
	}
	

	public void onRemoveFromLoadedWorld()
	{
		nbt.clear();
	}
	
	protected boolean is(int i)
	{
		return (state & (1 << i)) != 0;
	}
	
	protected boolean islast(int i)
	{
		return (lastState & (1 << i)) != 0;
	}

	protected void change(int i)
	{
		state ^= 1 << i;
	}

	protected void disable(int i)
	{
		state &= ~(1 << i);
	}
	
	protected void enable(int i)
	{
		state |= (1 << i);
	}

	protected void set(int i, boolean flag)
	{
		if(flag)
		{
			enable(i);
		}
		else
		{
			disable(i);
		}
	}
}