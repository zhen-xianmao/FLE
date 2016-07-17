package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenTaiga extends TreeGenAbstract
{
	protected int minHeight = 6;
	protected int randHeight = 4;
	protected float generateCoreLeavesChance;

	public TreeGenTaiga(TreeBase tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}

	public void setHeight(int minHeight, int randHeight)
	{
		this.minHeight = minHeight;
		this.randHeight = randHeight;
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
        int l = U.L.nextInt(randHeight, random) + minHeight;
        int i1 = 1 + random.nextInt(2);
        int j1 = l - i1;
        int k1 = 2 + random.nextInt(2);

        if (y >= 1 && y + l + 1 <= 256)
        {
        	if (!checkEmpty(world, x, y, z, 0, i1, 0, false))
				return false;
        	if (!checkEmpty(world, x, y + i1 + 1, z, k1, j1 - 1, k1, true))
				return false;
            int i2;
            int l3;

            Block block1 = world.getBlock(x, y - 1, z);

            boolean isSoil = block1.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
            if (isSoil && y < 256 - l - 1)
            {
                block1.onPlantGrow(world, x, y - 1, z, x, y, z);
                l3 = random.nextInt(2);
                i2 = 1;
                byte b0 = 0;
                int k2;
                int i4;

                for (i4 = 0; i4 <= j1; ++i4)
                {
                    k2 = y + l - i4;

                    for (int l2 = x - l3; l2 <= x + l3; ++l2)
                    {
                        int i3 = l2 - x;

                        for (int j3 = z - l3; j3 <= z + l3; ++j3)
                        {
                            int k3 = j3 - z;

                            if ((Math.abs(i3) != l3 || Math.abs(k3) != l3 || l3 <= 0) && world.getBlock(l2, k2, j3).canBeReplacedByLeaves(world, l2, k2, j3))
								generateTreeLeaves(world, x, y, z, 0, generateCoreLeavesChance, random, info);
                        }
                    }

                    if (l3 >= i2)
                    {
                        l3 = b0;
                        b0 = 1;
                        ++i2;

                        if (i2 > k1)
							i2 = k1;
                    } else
						++l3;
                }

                i4 = random.nextInt(3);

                for (k2 = 0; k2 < l - i4; ++k2)
					if (isReplaceable(world, x, y + k2, z))
						generateLog(world, x, y, z, 0);

                return true;
            } else
				return false;
        } else
			return false;
	}
}