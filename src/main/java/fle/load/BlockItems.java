package fle.load;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.block.plant.tree.BlockSapling;
import farcore.util.FleLog;
import fle.core.block.BlockFire;
import fle.core.block.BlockIce;
import fle.core.block.BlockLava;
import fle.core.block.BlockOre;
import fle.core.block.BlockRock;
import fle.core.block.BlockSand;
import fle.core.block.BlockSnow;
import fle.core.block.BlockTorch;
import fle.core.block.BlockWater;
import fle.core.block.machine.alpha.BlockCampfire;
import fle.core.block.machine.alpha.BlockWithOutTileAlpha;
import fle.core.block.machine.alpha.BlockWithTileAlpha;
import fle.core.block.plant.BlockBush1;
import fle.core.block.plant.BlockVine1;
import fle.core.item.ItemBowl;
import fle.core.item.ItemLog;
import fle.core.item.ItemToolFle;
import fle.core.item.ItemToolHeadFle;
import fle.core.item.resource.ItemFleDividableSmeltableFood;
import fle.core.item.resource.ItemFleSmeltableFood;
import fle.core.item.resource.ItemPile;
import fle.core.item.resource.ItemPlant;
import fle.core.item.resource.ItemPlantProduction;
import fle.core.item.resource.ItemStick;
import fle.core.item.resource.ItemStoneChip;
import fle.core.item.resource.ItemStoneFragment;
import fle.core.tile.TileEntityCampfire;
import fle.core.tile.TileEntityDryingTable;
import fle.core.tile.TileEntityTorch;
import fle.core.tile.statics.TileEntityOre;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockItems
{
	public static Block torch;
	public static Block fire;
	public static Block water;
	public static Block lava;
	public static Block ice;
	public static Block sapling;
	public static Block vine1;
	public static Block bush1;
	public static Block ore;
	public static Block snow;
	public static Block campfire;

	public static Block machineIAlpha;
	public static Block machineIIAlpha;

	public static Item plant;
	public static Item plant_production;
	public static Item tools;
	public static Item toolHead;
	public static Item stoneChip;
	public static Item stoneFragment;
	public static Item pile;
	public static Item log;
	public static Item smeltableFood;
	public static Item divideSmeltableFood;
	public static Item stick;
	public static Item bowl;
	
	public static void init()
	{
		FleLog.getLogger().info("Start register fle blocks and items.");
		torch = new BlockTorch().setBlockTextureName("fle:tools/torch").setCreativeTab(CreativeTabs.tabDecorations);
		GameRegistry.registerTileEntity(TileEntityTorch.class, "torch");
		fire = new BlockFire().setBlockTextureName("fle:iconsets/fire");
		water = new BlockWater().setBlockTextureName("fle:fluids/water");
		lava = new BlockLava().setBlockTextureName("fle:fluids/lava");
		ice = new BlockIce().setBlockTextureName("fle:iconsets/ice");
		sapling = new BlockSapling().setBlockTextureName("fle:sapling");
		bush1 = new BlockBush1("bush1").setBlockTextureName("fle:plant");
		vine1 = new BlockVine1("vine1").setBlockTextureName("fle:plant");
		ore = new BlockOre().setBlockTextureName("fle:ore");
		GameRegistry.registerTileEntity(TileEntityOre.class, "fle.ore");
		snow = new BlockSnow().setBlockTextureName("fle:snow");
		BlockRock.init();
		BlockSand.init();
		campfire = new BlockCampfire().setBlockTextureName("fle:machine/alpha");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, "fle.campfire");
		machineIAlpha = new BlockWithOutTileAlpha().setBlockTextureName("fle:machine/alpha");
		machineIIAlpha = new BlockWithTileAlpha().setBlockTextureName("fle:machine/alpha");
		GameRegistry.registerTileEntity(TileEntityDryingTable.class, "fle.drying.table");
		
		tools = new ItemToolFle().setCreativeTab(CreativeTabs.tabTools);
		toolHead = new ItemToolHeadFle().setCreativeTab(CreativeTabs.tabMaterials);
		stoneChip = new ItemStoneChip().setCreativeTab(CreativeTabs.tabMaterials);
		stoneFragment = new ItemStoneFragment().setCreativeTab(CreativeTabs.tabMaterials);
		pile = new ItemPile().setCreativeTab(CreativeTabs.tabMaterials);
		log = new ItemLog().setCreativeTab(CreativeTabs.tabMaterials);
		plant = new ItemPlant().setCreativeTab(CreativeTabs.tabMaterials);
		plant_production = new ItemPlantProduction().setCreativeTab(CreativeTabs.tabMaterials);
		smeltableFood = new ItemFleSmeltableFood().setCreativeTab(CreativeTabs.tabFood);
		divideSmeltableFood = new ItemFleDividableSmeltableFood().setCreativeTab(CreativeTabs.tabFood);
		stick = new ItemStick().setTextureName("fle:resource/stick").setCreativeTab(CreativeTabs.tabMaterials);
		bowl = new ItemBowl().setTextureName("fle:resource/bowl").setCreativeTab(CreativeTabs.tabMaterials);
	}
}