/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import static farcore.FarCoreRegistry.registerTESR;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.client.render.ItemRenderWithTESR;
import fle.core.client.render.TESRDryingTable;
import fle.core.client.render.TESRLeverOilMill;
import fle.core.client.render.TESRStoneMill;
import fle.core.tile.wooden.TEDryingTable;
import fle.core.tile.wooden.TELeverOilMill;
import fle.core.tile.wooden.TEStoneMill;
import nebula.base.IRegister;
import nebula.client.NebulaRenderHandler;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockWoodenMiscMachine extends BlockTE
{
	public BlockWoodenMiscMachine()
	{
		super(FLE.MODID, "wooden.misc.machine", Materials.WOOD);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Drying Table");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Lever Oil Mill");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), "Stone Mill");
		
		LanguageManager.registerLocal("inventory.drying.table", "Drying Table");
		LanguageManager.registerLocal("inventory.lever.oil.mill", "Lever Oil Mill");
		LanguageManager.registerLocal("inventory.stone.mill", "Stone Mill");
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "drying_table", TEDryingTable.class);
		register.register(1, "lever_oil_mill", TELeverOilMill.class);
		register.register(2, "stone_mill", TEStoneMill.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		NebulaRenderHandler.registerRender(this.item, ItemRenderWithTESR.INSTANCE);
		
		registerTESR(TESRDryingTable.class);
		registerTESR(TESRLeverOilMill.class, this.item, 1);
		registerTESR(TESRStoneMill.class, this.item , 2);
		
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "misc_machine", this.property_TE);
		registerRenderMapper(mapper);
		registerCustomBlockRender(mapper, 0, "misc_machine/drying_table");
		registerCustomBlockRender(mapper, 1, "misc_machine/lever_oil_mill");
		registerCustomBlockRender(mapper, 2, "misc_machine/stone_mill");
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}