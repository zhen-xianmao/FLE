/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import nebula.Log;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class PolishingStateIconLoader implements IIconLoader
{
	private static final Map<Character, String> stateMap = new HashMap<>();
	
	public static void addState(char chr, String location)
	{
		if (stateMap.containsKey(chr))
		{
			Log.warn("The state with character '{}' is already exist.", chr);
		}
		else
		{
			stateMap.put(chr, location);
		}
	}
	
	private static Map<Character, TextureAtlasSprite> map;
	
	public static TextureAtlasSprite getIconFromChr(char chr)
	{
		return map.getOrDefault(chr, Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		ImmutableMap.Builder<Character, TextureAtlasSprite> builder = ImmutableMap.builder();
		for (Entry<Character, String> entry : stateMap.entrySet())
		{
			builder.put(entry.getKey(), register.registerIcon(entry.getValue()));
		}
		map = builder.build();
	}
}