/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public final class IO
{
	private IO() {}
	
	/**
	 * Copy resource to byte array, this method only take effect in client side.
	 * @param manager
	 * @param location
	 * @return The copied of source.
	 * @throws IOException If fail to copy resource or get resource failed.
	 */
	@SideOnly(Side.CLIENT)
	public static byte[] copyResource(IResourceManager manager, ResourceLocation location) throws IOException
	{
		IResource resource = null;
		byte[] code = null;
		try
		{
			resource = manager.getResource(location);
			code = IOUtils.toByteArray(resource.getInputStream());
		}
		catch (IOException exception)
		{
			throw exception;
		}
		finally
		{
			if(resource != null)
			{
				try
				{
					resource.close();
				}
				catch (IOException exception)
				{
					exception.printStackTrace();
				}
			}
		}
		return code;
	}
}