package fle.core.inventory.tank;

import fle.api.inventory.IInventoryTile;
import fle.api.te.TEInventory;
import net.minecraftforge.fluids.FluidStack;

public interface IConnectableTank<T extends TEInventory> extends IInventoryTile<T>
{
	void initMainTank(T tile, int capcity);
	
	FluidStack breakTank(T tile);
}