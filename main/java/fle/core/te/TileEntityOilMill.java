package fle.core.te;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.net.INetEventListener;
import fle.api.te.TEIT;
import fle.core.inventory.InventoryOilMill;

public class TileEntityOilMill extends TEIT<InventoryOilMill> implements INetEventListener
{
	public GuiCondition type = GuiError.DEFAULT;
	
	public TileEntityOilMill()
	{
		super(new InventoryOilMill());
	}

	@Override
	protected void updateInventory()
	{
		inv.updateEntity(this);
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return inv.getRecipeProgress(i);
	}

	@SideOnly(Side.CLIENT)
	public int getCache(int i)
	{
		return inv.getCache(i);
	}

	public void onWork()
	{
		inv.onWork();
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 0)
			inv.syncRecipeTime((Integer) contain);
	}
}