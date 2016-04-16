package fle.load;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.DropHandler;
import farcore.lib.recipe.ToolDestoryDropRecipes;
import farcore.lib.stack.BaseStack;
import farcore.lib.substance.SubstanceRock;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U;
import fle.api.util.BlockConditionMatcherIS;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Drops
{
	public static void init()
	{
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			ItemStack drop = EnumItem.stone_chip.instance(3, rock.getName());
			ItemStack drop2 = EnumItem.stone_fragment.instance(1, rock.getName());
			if(drop == null)
			{
				drop = EnumItem.cobble_block.instance(1, rock);
				drop2 = null;
			}
			if(drop2 != null)
			{
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("stone" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(2, new Ety(new BaseStack(drop, 2), 3), new Ety(new BaseStack(drop2), 1)));
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(1, new Ety(new BaseStack(drop2), 4)));
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable_basic.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(1, new Ety(new BaseStack(drop2), 4)));
			}
			else
			{
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("stone" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(1, new Ety(new BaseStack(drop), 1)));
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(1, new Ety(new BaseStack(drop), 1)));
				ToolDestoryDropRecipes.add(EnumToolType.hammer_digable_basic.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
						new DropHandler(1, new Ety(new BaseStack(drop), 1)));
			}
		}
		SubstanceWood.getSubstance("oak").setLeafDrop(
				new DropHandler(0.125F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_oak")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 3),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_oak")), 2)));
		SubstanceWood.getSubstance("spruce").setLeafDrop(
				new DropHandler(0.125F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_spruce")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 3),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_spruce")), 2)));
		SubstanceWood.getSubstance("birch").setLeafDrop(
				new DropHandler(0.125F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_birch")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 3),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_birch")), 2)));
		SubstanceWood.getSubstance("ceiba").setLeafDrop(
				new DropHandler(0.0625F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_ceiba")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 5),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_ceiba")), 2)));
		SubstanceWood.getSubstance("acacia").setLeafDrop(
				new DropHandler(0.125F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_acacia")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 3),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_acacia")), 2)));
		SubstanceWood.getSubstance("oak-black").setLeafDrop(
				new DropHandler(0.125F, 1, 1, 
						new Ety(new BaseStack(EnumItem.plant.instance(1, "seed_oak-black")), 1),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 3),
						new Ety(new BaseStack(EnumItem.plant.instance(1, "branch_oak-black")), 2)));
	}
}