package fla.core.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import fla.api.block.IInventoryTile;

public abstract class TileEntityInventory<T extends TileEntity> extends TileEntityBaseWithFacing implements IInventory
{
	protected IInventoryTile<T> inv;
	
	protected TileEntityInventory(IInventoryTile<T> inv)
	{
		this.inv = inv;
	}
	
	public abstract void updateEntity();
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		NBTTagCompound nbt1 = nbt.getCompoundTag("Inventory");
		inv.readFromNBT(nbt1);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		inv.writeToNBT(nbt1);
		nbt.setTag("Inventory", nbt1);
	}

	@Override
	public int getSizeInventory() 
	{
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return inv.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		return inv.decrStackSize(i, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return inv.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		inv.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInventoryName() 
	{
		return inv.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return inv.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return inv.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inv.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {	}

	@Override
	public void closeInventory() {  }

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return inv.isItemValidForSlot(i, itemstack);
	}	
}
