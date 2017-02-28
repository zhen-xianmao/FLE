package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeMorus extends Tree
{
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.08F, false);
	
	public TreeMorus()
	{
		this.generator1.setTreeLeavesShape(2, 5, 1, 3.2F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setTreeLogShape(4 + info.height / 4, 3 + info.height / 3);
		}
		else
		{
			this.generator1.setTreeLogShape(4, 3);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}