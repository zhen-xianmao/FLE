package nebula.client;

import java.util.ArrayList;
import java.util.List;

import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NebulaTextureHandler implements IIconRegister
{
	private static final List<IIconLoader> LIST = new ArrayList<>();
	
	public static void addIconLoader(IIconLoader loader)
	{
		LIST.add(loader);
	}
	
	private TextureMap map;
	
	@SubscribeEvent
	public void onTexturesReoload(TextureStitchEvent.Pre event)
	{
		this.map = event.getMap();
		for(IIconLoader loader : LIST)
		{
			loader.registerIcon(this);
		}
		this.map = null;
	}
	
	@Override
	public TextureAtlasSprite registerIcon(String domain, String path)
	{
		return registerIcon(new ResourceLocation(domain, path));
	}
	
	@Override
	public TextureAtlasSprite registerIcon(String key)
	{
		return registerIcon(new ResourceLocation(key));
	}
	
	@Override
	public TextureAtlasSprite registerIcon(ResourceLocation location)
	{
		return this.map.registerSprite(location);
	}
}