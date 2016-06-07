package fle.core.block;

import farcore.lib.collection.IRegister;
import farcore.lib.substance.SubstanceBlockAbstract;
import farcore.lib.substance.SubstanceRock;
import farcore.util.LanguageManager;
import fle.api.block.BlockSubstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockBrick extends BlockSubstance<SubstanceRock>
{	
	public BlockBrick(SubstanceRock rock, IRegister<Block> register)
	{
		super("brick", Material.rock, rock, register);
		setBlockTextureName("fle:rock");
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public Object[] getTranslateObject(ItemStack stack)
	{
		return new Object[]{substance.getLocalName()};
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal(getUnlocalizedName() + ".name", "%s Brick");
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(getTextureName() + "/" + substance.getName() + "/brick");
	}

	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return spawn(world, x, y, z, register.get((String) objects[0]));
			}
			else if(objects[0] instanceof SubstanceRock)
			{
				return world.setBlock(x, y, z, item.block((SubstanceBlockAbstract) objects[0]));
			}
		}
		return false;
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return substance.harvestLevel / 2 + 1;
	}
}