/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeSpruce extends Tree
{
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.05F);
	
	public TreeSpruce(Mat material)
	{
		super(material);
		this.isBroadLeaf = false;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setHeight(6 + info.height / 2, 4 + info.height / 4);
		}
		else
		{
			this.generator1.setHeight(6, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}