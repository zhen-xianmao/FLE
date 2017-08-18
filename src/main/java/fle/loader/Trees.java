/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import static farcore.data.M.acacia;
import static farcore.data.M.aspen;
import static farcore.data.M.birch;
import static farcore.data.M.ceiba;
import static farcore.data.M.lacquer;
import static farcore.data.M.morus;
import static farcore.data.M.oak;
import static farcore.data.M.oak_black;
import static farcore.data.M.spruce;
import static farcore.data.M.willow;

import fle.core.tree.TreeAcacia;
import fle.core.tree.TreeAspen;
import fle.core.tree.TreeBirch;
import fle.core.tree.TreeCeiba;
import fle.core.tree.TreeLacquer;
import fle.core.tree.TreeMorus;
import fle.core.tree.TreeOak;
import fle.core.tree.TreeOakBlack;
import fle.core.tree.TreeSpruce;
import fle.core.tree.TreeWillow;

/**
 * @author ueyudiud
 */
public class Trees
{
	public static void init()
	{
		oak			.setTree(new TreeOak().setMaterial(oak).setDefFamily());
		oak_black	.setTree(new TreeOakBlack().setMaterial(oak_black).setDefFamily());
		spruce		.setTree(new TreeSpruce().setMaterial(spruce).setDefFamily());
		birch		.setTree(new TreeBirch().setMaterial(birch).setDefFamily());
		ceiba		.setTree(new TreeCeiba().setMaterial(ceiba).setDefFamily());
		acacia		.setTree(new TreeAcacia().setMaterial(acacia).setDefFamily());
		aspen		.setTree(new TreeAspen().setMaterial(aspen).setDefFamily());
		morus		.setTree(new TreeMorus().setMaterial(morus).setDefFamily());
		willow		.setTree(new TreeWillow().setMaterial(willow).setDefFamily());
		lacquer		.setTree(new TreeLacquer(lacquer).setDefFamily());
	}
}