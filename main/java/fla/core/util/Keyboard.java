package fla.core.util;

import java.util.*;
import net.minecraft.entity.player.EntityPlayer;

public class Keyboard
{
	protected static enum Key
	{
		Place,
		Sneak,
		Forward,
		Jump,
		Tech;

		public static int toInt(Iterable keySet)
		{
			int ret = 0;
			for (Iterator itr = keySet.iterator(); itr.hasNext();)
			{
				Key key = (Key)itr.next();
				ret |= 1 << key.ordinal();
			}

			return ret;
		}

		public static Set fromInt(int keyState)
		{
			Set ret = EnumSet.noneOf(Key.class);
			int i = 0;
			for (; keyState != 0; keyState >>= 1)
			{
				if ((keyState & 1) != 0)
					ret.add(Key.values()[i]);
				i++;
			}

			return ret;
		}
	}

	private final Map playerKeys = new WeakHashMap();

	public Keyboard()
	{
	}

	public boolean isTechKeyDown(EntityPlayer player)
	{
		return get(player, Key.Tech);
	}

	public boolean isPlaceKeyDown(EntityPlayer player)
	{
		return get(player, Key.Place);
	}

	public boolean isForwardKeyDown(EntityPlayer player)
	{
		return get(player, Key.Forward);
	}

	public boolean isJumpKeyDown(EntityPlayer player)
	{
		return get(player, Key.Jump);
	}
	
	public boolean isSneakKeyDown(EntityPlayer player)
	{
		return player.isSneaking();
	}

	public void sendKeyUpdate()
	{
	}

	public void processKeyUpdate(EntityPlayer player, int keyState)
	{
		playerKeys.put(player, Key.fromInt(keyState));
	}

	public void removePlayerReferences(EntityPlayer player)
	{
		playerKeys.remove(player);
	}

	private boolean get(EntityPlayer player, Key key)
	{
		Set keys = (Set)playerKeys.get(player);
		if (keys == null)
			return false;
		else
			return keys.contains(key);
	}
}
