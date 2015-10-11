package fle.api.soild;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface ISolidContainerItem
{
	SolidStack getSolid(ItemStack aStack);
    int fill(ItemStack aStack, SolidStack resource, boolean doFill);
    SolidStack drain(ItemStack aStack, SolidStack resource, boolean doDrain);
    SolidStack drain(ItemStack aStack, int maxDrain, boolean doDrain);
    boolean canFill(ItemStack aStack, Solid solid);
    boolean canDrain(ItemStack aStack, Solid solid);
    SolidTankInfo getTankInfo(ItemStack aStack);
}