package farcore.lib.model.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import farcore.lib.entity.EntityProjectileItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectileItem extends Render<EntityProjectileItem>
{
	@SideOnly(Side.CLIENT)
	public static enum Factory implements IRenderFactory
	{
		instance;
		
		@Override
		public Render createRenderFor(RenderManager manager)
		{
			return new RenderProjectileItem(manager);
		}
	}
	
	private Minecraft minecraft = Minecraft.getMinecraft();
	private final Random random = new Random();
	private RenderItem render;

	protected RenderProjectileItem(RenderManager renderManager)
	{
		super(renderManager);
		render = minecraft.getRenderItem();
	}
	
	@Override
	public void doRender(EntityProjectileItem entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		ItemStack stack = entity.currentItem;
		if(stack != null)
		{
			bindEntityTexture(entity);
			EntityItem item = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack.copy());
			item.getEntityItem().stackSize = 1;
			item.hoverStart = 1.0F;
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glRotated(1D, 0D, 180D - entity.rotationYaw, entity.rotationPitch);
			GL11.glScalef(.5F, .5F, .5F);
			GlStateManager.disableLighting();

			if (!render.shouldRenderItemIn3D(stack))
			{
				GL11.glRotatef(180F, 0, 1, 0);
			}

			GlStateManager.pushAttrib();
			RenderHelper.enableStandardItemLighting();
			render.renderItem(stack, TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popAttrib();
			GlStateManager.enableLighting();
			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityProjectileItem entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}