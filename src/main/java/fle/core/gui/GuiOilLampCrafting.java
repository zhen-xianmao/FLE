package fle.core.gui;

import farcore.lib.gui.GuiContainerBase;
import farcore.lib.world.ICoord;
import fle.core.FLE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOilLampCrafting extends GuiContainerBase
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/crafting_oillamp.png");

	public GuiOilLampCrafting(EntityPlayer player, ICoord coordOilLamp)
	{
		super(new ContainerOilLampCrafting(player, coordOilLamp), LOCATION);
	}
}