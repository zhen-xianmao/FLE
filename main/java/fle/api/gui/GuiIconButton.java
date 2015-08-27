package fle.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fle.api.FleValue;

public class GuiIconButton extends GuiButton
{
	private static final ResourceLocation void_texture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/" + FleValue.VOID_ICON_FILE + ".png");
	private static final ResourceLocation back_ground_texture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/button_1.png");
	public static final ResourceLocation buttonLocate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/button.png");
	
	public static enum ButtonSize
	{
		Standard(18),
		Slot(16),
		Small(10);
		
		final int size;
		
		private ButtonSize(int aSize) 
		{
			size = aSize;
		}
	}
	
	private ButtonSize size;
	private ResourceLocation texture;
	private int textureX;
	private int textureY;
	private ItemStack itemStack;
	private boolean drawQuantity;
	private RenderItem renderItem;
	private boolean emptyDraw = false;

	public GuiIconButton(int id1, int x, int y, ButtonSize aSize)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		emptyDraw = true;
	}
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, int u, int v)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		itemStack = null;
		size = aSize;
		textureX = u;
		textureY = v;
	}
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, ResourceLocation texture1, int textureX1, 
			int textureY1)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		itemStack = null;
		size = aSize;
		texture = texture1;
		textureX = textureX1;
		textureY = textureY1;
	}
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, ItemStack icon, boolean drawQuantity1)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		itemStack = icon;
		size = aSize;
		drawQuantity = drawQuantity1;
	}
	
	public GuiIconButton setVisible(boolean iv)
	{
		visible = iv;
		return this;
	}

	public void drawButton(Minecraft minecraft, int i, int j)
	{
		if(visible)
		{
			this.field_146123_n = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
			minecraft.getTextureManager().bindTexture(back_ground_texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(emptyDraw)
			{
				if(k != 2) return;
				minecraft.getTextureManager().bindTexture(void_texture);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
				drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
				return;
			}
            switch(size)
            {
			case Standard:
			{
				drawTexturedModalRect(xPosition, yPosition, 0, k * size.size, width, height);
				break;
			}
			case Small:
			{
				drawTexturedModalRect(xPosition, yPosition, 18, k * size.size, width, height);
				break;
			}				
			default:
				break;
            }
            this.mouseDragged(minecraft, i, j);
    		if (itemStack == null)
    		{
    			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    			if(texture != null)
    			{
    				minecraft.getTextureManager().bindTexture(texture);
    			}
    			drawTexturedModalRect(xPosition + 1, yPosition + 1, textureX, textureY, width - 2, height - 2);
    		} 
    		else
    		{
    			if (renderItem == null)
    				renderItem = new RenderItem();
    			renderItem.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, xPosition + 2, yPosition + 1);
    			if (drawQuantity)
    				renderItem.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, xPosition + 2, xPosition + 1);
    		}
		}
	}
}