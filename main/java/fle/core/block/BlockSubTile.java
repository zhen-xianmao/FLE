package fle.core.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.api.block.IBlockBehaviour;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.block.IFacingBlock;
import fle.api.util.IBlockTextureManager;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;
import fle.api.world.BlockPos;

public abstract class BlockSubTile extends BlockHasTile implements IFacingBlock
{
	protected final Register<IBlockWithTileBehaviour<BlockSubTile>> blockBehaviors = new Register();
	protected Map<String, IBlockTextureManager> textureNameMap = new HashMap();
	private Map<String, IIcon[]> iconMap;

	protected BlockSubTile(String aName, Material aMaterial)
	{
		super(ItemSubTile.class, aName, aMaterial);
	}
	protected BlockSubTile(Class<? extends ItemSubTile> clazz, String aName, Material aMaterial)
	{
		super(clazz, aName, aMaterial);
	}
	
	public void registerSub(int index, String aName, IBlockTextureManager locate, IBlockWithTileBehaviour<BlockSubTile> blockBehavior)
	{
		blockBehaviors.register(index, blockBehavior, aName);
		textureNameMap.put(aName, locate);
	}
	
	public Register<IBlockWithTileBehaviour<BlockSubTile>> getRegister()
	{
		return blockBehaviors;
	}
	
	public IBlockTextureManager getTextureName(String meta)
	{
		return textureNameMap.get(meta);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		iconMap = new HashMap();
		for(String str : textureNameMap.keySet())
		{
			ITextureLocation locate = textureNameMap.get(str);
			IIcon[] icons = new IIcon[locate.getLocateSize()];
			for(int i = 0; i < icons.length; ++i)
				icons[i] = register.registerIcon(locate.getTextureFileName(i) + ":" + locate.getTextureName(i));
			iconMap.put(str, icons);
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int tMeta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 0);
		int tSide = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 1);
		try
		{
			return iconMap.get(blockBehaviors.name(tMeta))[textureNameMap.get(blockBehaviors.name(tMeta)).getIconID(ForgeDirection.VALID_DIRECTIONS[FleValue.MACHINE_FACING[tSide][side]])];
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return blockIcon;
		}
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		try
		{
			return iconMap.get(blockBehaviors.name(meta))[textureNameMap.get(blockBehaviors.name(meta)).getIconID(ForgeDirection.VALID_DIRECTIONS[side])];
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return blockIcon;
		}
	}
	
	@Override
	public int getDamageValue(World aWorld, int x, int y, int z)
	{
		int value =  FLE.fle.getWorldManager().getData(new BlockPos(aWorld, x, y, z), 0);
		return value == 0 ? aWorld.getBlockMetadata(x, y, z) : value;
	}
	
	@Override
	public void getSubBlocks(Item aItem, CreativeTabs aTab,	List aList) 
	{
		for(IBlockWithTileBehaviour<BlockSubTile> tB : blockBehaviors)
		{
			if(tB != null)
				aList.add(new ItemStack(aItem, 1, blockBehaviors.serial(tB)));
		}
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer, int aSide, float xPos, float yPos, float zPos)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			if(tBehaviour.onBlockActivated(this, aWorld, x, y, z, aPlayer, ForgeDirection.VALID_DIRECTIONS[aSide], xPos, yPos, zPos))
			{
				return true;
			}
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public void onBlockClicked(World aWorld, int x, int y, int z, EntityPlayer aPlayer)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockClicked(this, aWorld, x, y, z, aPlayer);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onBlockPlacedBy(World aWorld, int x,
			int y, int z, EntityLivingBase aEntity,
			ItemStack aStack)
	{
		FLE.fle.getWorldManager().setData(new BlockPos(aWorld, x, y, z), 1, FleAPI.getIndexFromDirection(getPointFacing(aWorld, x, y, z, aEntity)));
		super.onBlockPlacedBy(aWorld, x, y, z, aEntity, aStack);
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int x, int y, int z, int aSide, float xPos, float yPos, float zPos, int metadata)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockPlaced(this, aWorld, x, y, z, ForgeDirection.VALID_DIRECTIONS[aSide], xPos, yPos, zPos);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return metadata;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World aWorld, int x, int y, int z, Entity aEntity) 
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onEntityCollidedWithBlock(this, aWorld, x, y, z, aEntity);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onFallenUpon(World aWorld, int x, int y, int z, Entity aEntity, float aHeight)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onFallenUpon(this, aWorld, x, y, z, aEntity, aHeight);;
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
			int aMeta)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockBreak(this, aWorld, x, y, z, aBlock, aMeta);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public void addInformation(ItemStack aStack, List<String> aList,
			EntityPlayer aPlayer) 
	{
		super.addInformation(aStack, aList, aPlayer);
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) aStack.getItemDamage()));
		try
	    {
			tBehaviour.getAdditionalToolTips(this, aList, aStack);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta) 
	{
		IBlockWithTileBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) aMeta));
		try
		{
			return tBehaviour.createNewTileEntity(this, aWorld, aMeta);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(new ItemStack(this, 1, metaThread.get()));
		return drops;
	}

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		return ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(pos, 1)];
	}

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos)
	{
		
	}
	
	@Override
	public void dropBlockAsItem(World aWorld, int x,
			int y, int z, ItemStack aStack)
	{
		if(aStack != null)
			super.dropBlockAsItem(aWorld, x, y, z, aStack.copy());
	}
}