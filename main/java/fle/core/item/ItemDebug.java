package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import fle.FLE;
import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.api.energy.IRotationTileEntity;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumDamageResource;
import fle.api.enums.EnumWorldNBT;
import fle.api.item.ItemFle;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;
import fle.api.world.BlockPos.ChunkPos;

public class ItemDebug extends ItemFle
{
	public ItemDebug(String aUnlocalized)
	{
		super(aUnlocalized);
	}
	
	@Override
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer,
			World aWorld, int x, int y, int z,
			int aSide, float xPos, float yPos,
			float zPos)
	{
    	if(!aWorld.isRemote)
    	{
        	BlockPos pos = new BlockPos(aWorld, x, y, z);
        	ChunkPos pos1 = pos.getChunkPos();
        	try
        	{
        		aPlayer.addChatMessage(new ChatComponentText("This block is named " + aWorld.getBlock(x, y, z).getUnlocalizedName() + "."));
            	aPlayer.addChatMessage(new ChatComponentText("Block name is " + GameData.getBlockRegistry().getNameForObject(pos.getBlock()) + ", by id " + Block.getIdFromBlock(pos.getBlock()) + "."));
            	aPlayer.addChatMessage(new ChatComponentText("Metadata: " + pos.getBlockMeta() + "."));
            	aPlayer.addChatMessage(new ChatComponentText("Harvest Level: " + pos.getBlock().getHarvestLevel(pos.getBlockMeta()) + "."));
        		aPlayer.addChatMessage(new ChatComponentText("Hardness: " + pos.getBlock().getBlockHardness(aWorld, x, y, z) + "."));
        		aPlayer.addChatMessage(new ChatComponentText("FTN :"));
        		aPlayer.addChatMessage(new ChatComponentText("Wind Speed : " + FLE.fle.getRotationNet().getWindSpeed(pos)));
        		
        		String str1 = "";
            	for(EnumWorldNBT nbt : EnumWorldNBT.values())
        			str1 += FLE.fle.getWorldManager().getData(pos, nbt) + " ";
            	aPlayer.addChatMessage(new ChatComponentText("FWM: " + str1 + "."));
            	if(pos.getBlockTile() instanceof IFluidHandler)
            	{
            		IFluidHandler handler = (IFluidHandler) pos.getBlockTile();
            		FluidTankInfo[] infos = handler.getTankInfo(ForgeDirection.VALID_DIRECTIONS[aSide]);
            		for(FluidTankInfo info : infos)
            		{
            			aPlayer.addChatMessage(new ChatComponentText(String.format("Capacity: %s.", FleValue.format_L.format_c(info.capacity))));
            			if(info.fluid != null)
            				aPlayer.addChatMessage(new ChatComponentText(String.format("Fluid Amount: %sx%s.", info.fluid.getLocalizedName(), FleValue.format_L.format_c(info.fluid.amount))));
            		}
            	}
            	if(pos.getBlockTile() instanceof IThermalTileEntity)
            	{
            		IThermalTileEntity tile = (IThermalTileEntity) pos.getBlockTile();
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Temperature: %s.", FleValue.format_K.format_c(tile.getTemperature(ForgeDirection.VALID_DIRECTIONS[aSide])))));
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Heat Currect: %s.", FleValue.format_MJ.format_c(tile.getThermalEnergyCurrect(ForgeDirection.VALID_DIRECTIONS[aSide])))));
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Emit Heat: %s.", FleValue.format_MJ.format_c(tile.getPreHeatEmit()))));
            	}
            	if(pos.getBlockTile() instanceof IRotationTileEntity)
            	{
            		IRotationTileEntity tile = (IRotationTileEntity) pos.getBlockTile();
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Kinetic Energy Currect: %s.", FleValue.format_MJ.format_c(tile.getEnergyCurrect()))));
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Emit Heat: %s.", FleValue.format_MJ.format_c(tile.getPreEnergyEmit()))));
            	}
            	if(pos.getBlock() instanceof IDebugableBlock)
        		{
        			List<String> tList = new ArrayList();
        			((IDebugableBlock) pos.getBlock()).addInfomationToList(aWorld, x, y, z, tList);
        			for(String str : tList)
        			{
        				aPlayer.addChatMessage(new ChatComponentText(str));
        			}
        		}
        	}
        	catch(Throwable e)
        	{
        		FleLog.getLogger().catching(Level.WARN, e);
        		aPlayer.addChatMessage(new ChatComponentText("FLE: This block require a bug place check your log and report this bug."));
        	}
    	}
		return true;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
	}

	@Override
	public void damageItem(ItemStack stack, EntityLivingBase aUser,
			EnumDamageResource aReource, float aDamage)
	{
		
	}
}