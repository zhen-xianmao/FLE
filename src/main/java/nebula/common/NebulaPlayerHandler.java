/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import nebula.Nebula;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_DigSpeed;
import nebula.common.network.packet.PacketBlockData;
import nebula.common.util.Sides;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author ueyudiud
 */
public class NebulaPlayerHandler
{
	@SubscribeEvent
	public void getDigSpeed(BreakSpeed event)
	{
		if(event.getEntityPlayer() == null) return;
		
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		
		if (stack == null) return;
		if (stack.getItem() instanceof IIP_DigSpeed)
		{
			event.setNewSpeed(((IIP_DigSpeed) stack.getItem()).replaceDigSpeed(stack, event));
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(Sides.isServer())
		{
			synchronized (ExtendedBlockStateRegister.SERVER)
			{
				Nebula.network.sendLargeToPlayer(new PacketBlockData(), event.player);
			}
		}
	}
}