package fle.core.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.api.crop.CropCard;
import fle.api.crop.ICropSeed;
import fle.api.world.BlockPos;
import fle.core.te.TileEntityCrop;

public class BlockFleCrop extends BlockHasTile implements IGrowable, IPlantable
{    
	private TileEntityCrop tile = new TileEntityCrop();
	private Map<String, IIcon[]> map = new HashMap();
	
	public BlockFleCrop()
	{
		super("fle.crop", Material.plants);
        setTickRandomly(true);
        float f = 0.5F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        setHardness(1.0F);
        setStepSound(soundTypeGrass);
        disableStats();
	}

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block block)
    {
        return block == Blocks.farmland;
    }

    @Override
    public void onBlockPlacedBy(World world, int x,
    		int y, int z, EntityLivingBase entity,
    		ItemStack stack)
    {
    	super.onBlockPlacedBy(world, x, y, z, entity, stack);
    	if(stack.getItem() instanceof ICropSeed)
    	{
    		tile.setupCrop(((ICropSeed) stack.getItem()).getCrop(stack));
    	}
    }

    @Override
    public void breakBlock(World world, int x, int y,
    		int z, Block block, int meta)
    {
    	TileEntityCrop tile = (TileEntityCrop) world.getTileEntity(x, y, z);
    	if(tile != null)
    	{
    		List<ItemStack> list = tile.getCropHarvestDrop();
    		for(ItemStack stack : list)
    		{
    			if(stack != null)
    				this.dropBlockAsItem(world, x, y, z, stack.copy());
    		}
    		world.func_147453_f(x, y, z, block);
    	}
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune)
    {
    	return new ArrayList();
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
    	super.registerBlockIcons(register);
    	Iterator<CropCard> itr = FLE.fle.getCropRegister().getCrops();
    	while (itr.hasNext())
    	{
			CropCard cropCard = (CropCard) itr.next();
			IIcon[] icons = new IIcon[cropCard.getMaturation()];
			for(int i = 0; i < icons.length; ++i)
				icons[i] = register.registerIcon(cropCard.getCropTextureName() + "_stage_" + i);
			map.put(cropCard.getCropName(), icons);
		}
    }

    public IIcon getIcon(CropCard crop, int stage)
    {
    	IIcon[] icons = map.get(crop.getCropName());
    	return icons[stage >= icons.length ? icons.length - 1 : stage];
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return FleValue.FLE_RENDER_ID;
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        this.checkAndDropBlock(world, x, y, z);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        this.checkAndDropBlock(world, x, y, z);
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    protected void checkAndDropBlock(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            world.removeTileEntity(x, y, z);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        return  world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
    @Override
	public boolean func_149851_a(World world, int x,
			int y, int z, boolean p_149851_5_) 
    {
		return true;
	}

	@Override
	public boolean func_149852_a(World world, Random rand,
			int x, int y, int z) 
	{
		return false;
	}
	
	@Override
	public void func_149853_b(World world, Random rand,
			int x, int y, int z) 
	{
		
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return tile.copy();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) 
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) 
	{
		return FLE.fle.getCropRegister().getCropID(((TileEntityCrop) world.getTileEntity(x, y, z)).getCrop());
	}

	@Override
	public boolean isNormalCube() 
	{
		return false;
	}

	@Override
	public boolean hasSubs() 
	{
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		return null;
	}
}