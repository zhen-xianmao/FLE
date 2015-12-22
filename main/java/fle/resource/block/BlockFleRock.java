package fle.resource.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.block.old.BlockFle;
import flapi.material.MaterialRock;
import flapi.util.FleValue;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;

public class BlockFleRock extends BlockFle
{
	private static final Map<MaterialRock, BlockFleRock> map = new HashMap();
	
	public static void init()
	{
		for(MaterialRock rock : MaterialRock.getRocks())
		{
			BlockFleRock block = new BlockFleRock(rock);
			if(rock.getRockType() != null) rock.getRockType().normal = block;
			map.put(rock, block);
		}
	}
	
	final MaterialRock m;
	
	private ItemStack drop;
	
	public BlockFleRock(MaterialRock material)
	{
		super(material.getRockName().toLowerCase(), material.getRockName(), Material.rock);
		setHardness((float) Math.sqrt(material.getPropertyInfo().getHardness() + 1));
		setResistance(2F * (float)Math.sqrt(material.getPropertyInfo().getHardness() + 1));
		setStepSound(soundTypeStone);
		setCreativeTab(FleValue.tabFLE);
		setBlockTextureName(FleValue.TEXTURE_FILE + ":rock/" + material.getRockName().toLowerCase());
		drop = ItemFleSub.a("chip_" + material.getRockName().toLowerCase());
		m = material;
	}

	public String getHarvestTool(int aMeta)
	{
		return "pickaxe";
	}
	
	@Override
	public float getBlockHardness(World world, int x,
			int y, int z)
	{
		return 1.0F + m.getPropertyInfo().getHardness();
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return m.getPropertyInfo().getHarvestLevel() - 3;
	}
	
	public MaterialRock getRockMaterial()
	{
		return m;
	}
	
	public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(m == Materials.Stone)
		{
			list.add(new ItemStack(Blocks.stone));
		}
		else if(drop != null)
		{
			ItemStack s = drop.copy();
			s.stackSize = 4;
			list.add(s);
		}
		else
		{
			list.add(new ItemStack(this));
		}
	    return list;
	}
	
	@Override
	public boolean isReplaceableOreGen(World world, int x, int y, int z,
			Block target)
	{
		return super.isReplaceableOreGen(world, x, y, z, target) || target == Blocks.stone;
	}
	
	public static BlockFleRock a(MaterialRock rock)
	{
		return map.get(rock);
	}
}