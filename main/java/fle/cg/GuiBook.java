package fle.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fluids.FluidStack;
import fle.api.gui.GuiIconButton;
import fle.api.gui.GuiIconButton.ButtonSize;
import fle.api.recipe.ItemAbstractStack;

public class GuiBook extends GuiBookBase
{
	protected int selectRecipe;
	
	protected RecipeHandler[] recipes;
	
	public GuiBook()
	{
		recipes = CraftGuide.instance.getAllRecipes();
		selectRecipe = 0;
	}
	
	@Override
	public RecipeHandler[] getShowingRecipe()
	{
		List<RecipeHandler> handlers = new ArrayList(6);
		for(int i = 0; i < 6 && selectRecipe * 6 + i < recipes.length; ++i)
		{
			handlers.add(recipes[selectRecipe * 6 + i]);
		}
		return handlers.toArray(new RecipeHandler[handlers.size()]);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 14, yoffset + 17, ButtonSize.Small, GuiIconButton.buttonLocate, 64, 8));
		buttonList.add(new GuiIconButton(1, xoffset + 14, yoffset + 27, ButtonSize.Small, GuiIconButton.buttonLocate, 72, 8));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		if(button.id == 0)
		{
			if(selectRecipe > 0) --selectRecipe;
		}
		else if(button.id == 1)
		{
			if(selectRecipe * 6 + 6 < recipes.length) ++selectRecipe;
		}
	}
	
	protected void markRecipeForUpdate()
	{
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type)
	{
		super.mouseClicked(mouseX, mouseY, type);
		if(type == 0)
		{
			RecipeHandler[] handlers = getShowingRecipe();
			for(int i = 0; i < handlers.length; ++i)
			{
				int xoffset = this.xoffset + 27 + 80 * (i % 2);
				int yoffset = this.yoffset + 17 + 60 * (i / 2);
				Rectangle rect = new Rectangle(xoffset, yoffset, 80, 60);
				if(rect.contains(mouseX, mouseY))
				{
					int j;
					for(j = 0; j < handlers[i].getSlotContain(); ++j)
					{
						if(handlers[i].getSlotPosition(j).contains(mouseX - xoffset, mouseY - yoffset))
						{
							switchRecipeFromItem(handlers[i].getStackInSlot(j));
						}
					}
					for(j = 0; j < handlers[i].getTankContain(); ++j)
					{
						if(handlers[i].getTankPosition(j).contains(mouseX - xoffset, mouseY - yoffset))
						{
							switchRecipeFromFluid(handlers[i].getFluidInTank(j));
						}
					}
				}
			}
		}
	}

	protected void switchRecipeFromItem(ItemAbstractStack aStack)
	{
		RecipeHandler[] rhs = CraftGuide.instance.getRecipes(aStack);
		if(rhs.length != 0)
		{
			recipes = rhs;
			selectRecipe = 0;
			markRecipeForUpdate();
		}
	}
	protected void switchRecipeFromFluid(FluidStack aStack)
	{
		RecipeHandler[] rhs = CraftGuide.instance.getRecipes(aStack);
		if(rhs.length != 0)
		{
			recipes = rhs;
			selectRecipe = 0;
			markRecipeForUpdate();
		}
	}
}