package farcore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.V;
import farcore.handler.FarCoreChunkHandler;
import farcore.handler.FarCoreConfigHandler;
import farcore.handler.FarCoreGuiHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockRockSlab;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.item.instance.ItemDebugger;
import farcore.lib.item.instance.ItemFluidDisplay;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.net.PacketKey;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.gui.PacketGuiAction;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.net.tile.PacketTESAskRender;
import farcore.lib.net.tile.PacketTESync;
import farcore.lib.render.RenderCross;
import farcore.lib.render.RenderFallingBlockExtended;
import farcore.lib.render.RenderFire;
import farcore.lib.render.RenderHandler;
import farcore.lib.render.RenderSlab;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.CreativeTabBase;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.network.Network;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = FarCore.ID, version = "0.93", name = "Far Core")
public class FarCoreSetup
{
	public static final int minForge = 1558;

	private LanguageManager lang;

	@Instance(FarCore.ID)
	public static FarCoreSetup setup;

	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static Proxy proxy;

	public FarCoreSetup()
	{
		setup = this;
		Log.logger = LogManager.getLogger(FarCore.ID);
	}

	@EventHandler
	public void check(FMLFingerprintViolationEvent event)
	{
		Log.info("Far Core start check java version...");
		try
		{
			Map map = new HashMap();
			map.getOrDefault("", "");
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("Java version is out of date, this mod is suggested use java 8 to run.", exception);
		}
		Log.info("Far Core checking mod version...");
		try
		{
			new ChunkCoordinates(1, 2, 3);
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("You may download dev version, please check your mod version and use default version.", exception);
		}
		Log.info("Far Core checking forge version...");
		int forge = ForgeVersion.getBuildVersion();
		if ((forge > 0) && (forge < minForge))
			throw new RuntimeException("The currently installed version of "
					+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." +
					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" +
					"Please update the Minecraft Forge.\n" + "\n" +
					"(Technical information: " + forge + " < " + minForge + ")");
		Log.info("Checking end.");
	}

	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		for(ModContainer container : Loader.instance().getModList())
		{
			//			if(FarCore.OVERRIDE_ID.equals(container.getName()))
			//			{
			//				event.getModMetadata().childMods.add(container);
			//				break;
			//			}
		}
		try
		{
			lang = new LanguageManager(new File(U.Mod.getMCFile(), "lang"));
			Log.info("Loading configuration.");
			File file = event.getSuggestedConfigurationFile();
			Configuration configuration = new Configuration(file);
			configuration.load();
			//			V.init(configuration);
			configuration.save();
		}
		catch(Exception exception)
		{
			Log.warn("Fail to load configuration.", exception);
		}
		lang.read();
		proxy.load(event);
	}

	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		FarCoreKeyHandler.register(V.keyPlace, Keyboard.KEY_P);
		LanguageManager.registerLocal("info.debug.date", "Date : ");
		proxy.load(event);
	}

	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		proxy.load(event);
	}

	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		lang.write();
		proxy.load(event);
	}

	@EventHandler
	public void load(FMLServerStartingEvent event)
	{

	}

	public static class Proxy
	{
		public void load(FMLPreInitializationEvent event)
		{
			Object handler = new FarCoreChunkHandler();
			MinecraftForge.EVENT_BUS.register(handler);
			FMLCommonHandler.instance().bus().register(handler);
			FMLCommonHandler.instance().bus().register(new FarCoreConfigHandler());
			FMLCommonHandler.instance().bus().register(new FarCoreKeyHandler());
			FarCore.tabResourceBlock = new CreativeTabBase("farcore.resource.block", "Far Resource Block")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(M.peridotite.rock, 1, 2);
				}
			};
			FarCore.tabResourceItem = new CreativeTabBase("farcore.resource.item", "Far Resource Item")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(EnumItem.stone_chip.item, 1, M.peridotite.id);
				}
			};
			FarCore.tabMachine = new CreativeTabBase("farcore.machine", "Far Machine")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Blocks.crafting_table);
				}
			};
			FarCore.tabMaterial = new CreativeTabBase("farcore.material", "Far Material")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Items.emerald);
				}
			};
			FarCore.tabTool = new CreativeTabBase("farcore.tool", "Far Tool")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(EnumItem.debug.item);
				}
			};
			M.init();
			new ItemDebugger().setCreativeTab(FarCore.tabTool);
			new ItemTreeLog().setCreativeTab(FarCore.tabResourceItem);
			new ItemFluidDisplay();
			new ItemStoneChip().setCreativeTab(FarCore.tabResourceItem);
			new BlockSapling().setBlockTextureName("sapling").setCreativeTab(FarCore.tabResourceBlock);
			new BlockCrop();
			new BlockFire();
			GameRegistry.registerTileEntity(TESapling.class, "farcore.sapling");
			GameRegistry.registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
			GameRegistry.registerTileEntity(TECrop.class, "farcore.crop");
			int id = 0;
			EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id++, FarCore.ID, 32, 20, true);
		}

		public void load(FMLInitializationEvent event)
		{
			FarCore.network = Network.network(FarCore.ID);
			FarCore.network.registerPacket(PacketGuiAction.class, Side.SERVER);
			FarCore.network.registerPacket(PacketEntity.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketEntityAsk.class, Side.SERVER);
			FarCore.network.registerPacket(PacketKey.class, Side.SERVER);
			//			FarCore.network.registerPacket(PacketPlayerStatUpdate.class, Side.CLIENT);

			FarCore.network.registerPacket(PacketTESync.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTESAskRender.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTEAsk.class, Side.SERVER);
		}

		public void load(FMLPostInitializationEvent event)
		{

		}

		public void load(FMLLoadCompleteEvent event)
		{

		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends Proxy implements IResourceManagerReloadListener
	{
		private boolean loadComplete = false;

		public ClientProxy()
		{
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
		}

		@Override
		public void load(FMLPreInitializationEvent event)
		{
			super.load(event);
			int id1 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id1, FarCore.handlerA = new RenderHandler(id1, false));
			int id2 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id2, FarCore.handlerB = new RenderHandler(id2, true));
			RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, new RenderFallingBlockExtended());
			MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
		}

		@Override
		public void load(FMLInitializationEvent event)
		{
			super.load(event);
		}

		@Override
		public void load(FMLPostInitializationEvent event)
		{
			super.load(event);
			FarCore.handlerA.register(EnumBlock.sapling.block, OreDictionary.WILDCARD_VALUE, RenderCross.class);
			FarCore.handlerA.register(EnumBlock.fire.block, OreDictionary.WILDCARD_VALUE, RenderFire.class);
			for(Object key : GameData.getBlockRegistry().getKeys())
			{
				Block block = (Block) GameData.getBlockRegistry().getObject(key);
				if(block instanceof BlockRockSlab)
					FarCore.handlerB.register(block, OreDictionary.WILDCARD_VALUE, RenderSlab.class);
			}
		}

		@Override
		public void load(FMLLoadCompleteEvent event)
		{
			super.load(event);
			loadComplete = true;
		}

		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			if(loadComplete)
			{

			}
		}
	}
}