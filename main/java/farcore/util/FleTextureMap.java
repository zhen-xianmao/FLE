package farcore.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.ITextureLoadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class FleTextureMap extends AbstractTexture implements IIconRegister, ITextureObject
{
	@SideOnly(Side.CLIENT)
	public static class TextureMapRegistry implements IResourceManagerReloadListener
	{
		private static final List<FleTextureMap> register = new ArrayList();

		public static FleTextureMap newTextureMap(String name)
		{
			FleTextureMap map = new FleTextureMap(name);
			register.add(map);
			return map;
		}
		public static FleTextureMap newTextureMap(String name, boolean skipFirst)
		{
			FleTextureMap map = new FleTextureMap(name, skipFirst);
			register.add(map);
			return map;
		}
		
		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			if(minecraft.renderEngine == null) return;
			for(FleTextureMap map : register)
			{
				minecraft.renderEngine.loadTexture(map.location, map);
			}
		}
	}
	
	private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
	
	public class FleTextureAtlasIcon extends TextureAtlasSprite
	{
		public FleTextureAtlasIcon(String name)
		{
			super(name);
		}
	}

    public final ResourceLocation location;
    private final List<ITextureLoadListener> listeners = new ArrayList();
    private final List<IIcon> listAnimatedSprites = new ArrayList();
    private final Map<String, IIcon> mapRegisteredSprites = new HashMap();
    private final Map<String, IIcon> mapUploadedSprites = new HashMap();
    private final String basePath;
    private int mipmapLevels;
    private int anisotropicFiltering = 1;
    private final TextureAtlasSprite missingImage = new FleTextureAtlasIcon("missingno");
    private boolean skipFirst = false;

    public FleTextureMap(String path)
    {
        this(path, false);
    }
    public FleTextureMap(String path, boolean skipFirst)
    {
        this.basePath = "textures/" + path;
        this.skipFirst = skipFirst && ENABLE_SKIP;
        this.location = new ResourceLocation("textures/atlas/" + path + ".png");
    }
    
    public void loadTextureAtlas(IResourceManager manager)
    {
    	registerIcons();
    	
        int i = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(i, i, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        Iterator iterator = this.mapRegisteredSprites.entrySet().iterator();
        TextureAtlasSprite textureatlassprite;

        while (!skipFirst && iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();
            ResourceLocation resourcelocation = new ResourceLocation((String)entry.getKey());
            textureatlassprite = (TextureAtlasSprite)entry.getValue();
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

            if (textureatlassprite.hasCustomLoader(manager, resourcelocation))
            {
                if (!textureatlassprite.load(manager, resourcelocation))
                {
                    j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
                    stitcher.addSprite(textureatlassprite);
                }
                continue;
            }

            try
            {
                IResource iresource = manager.getResource(resourcelocation1);
                BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
                abufferedimage[0] = ImageIO.read(iresource.getInputStream());
                TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

                if (texturemetadatasection != null)
                {
                    List list = texturemetadatasection.getListMipmaps();
                    int l;

                    if (!list.isEmpty())
                    {
                        int k = abufferedimage[0].getWidth();
                        l = abufferedimage[0].getHeight();

                        if (MathHelper.roundUpToPowerOfTwo(k) != k || MathHelper.roundUpToPowerOfTwo(l) != l)
                        {
                            throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                        }
                    }

                    Iterator iterator3 = list.iterator();

                    while (iterator3.hasNext())
                    {
                        l = ((Integer)iterator3.next()).intValue();

                        if (l > 0 && l < abufferedimage.length - 1 && abufferedimage[l] == null)
                        {
                            ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, l);

                            try
                            {
                                abufferedimage[l] = ImageIO.read(manager.getResource(resourcelocation2).getInputStream());
                            }
                            catch (IOException ioexception)
                            {
                                FleLog.getLogger().error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(l), resourcelocation2, ioexception});
                            }
                        }
                    }
                }

                AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
                textureatlassprite.loadSprite(abufferedimage, animationmetadatasection, (float)this.anisotropicFiltering > 1.0F);
            }
            catch (RuntimeException runtimeexception)
            {
            	//logger.error("Unable to parse metadata from " + resourcelocation1, runtimeexception);
                cpw.mods.fml.client.FMLClientHandler.instance().trackBrokenTexture(resourcelocation1, runtimeexception.getMessage());
                continue;
            }
            catch (IOException ioexception1)
            {
            	//logger.error("Using missing texture, unable to load " + resourcelocation1, ioexception1);
                cpw.mods.fml.client.FMLClientHandler.instance().trackMissingTexture(resourcelocation1);
                continue;
            }

            j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
            stitcher.addSprite(textureatlassprite);
        }

        int i1 = MathHelper.calculateLogBaseTwo(j);

        if (i1 < this.mipmapLevels)
        {
        	FleLog.getLogger().debug("{}: dropping miplevel from {} to {}, because of minTexel: {}", new Object[] {this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(i1), Integer.valueOf(j)});
            this.mipmapLevels = i1;
        }

        Iterator iterator1 = this.mapRegisteredSprites.values().iterator();

        while (!skipFirst && iterator1.hasNext())
        {
            final TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)iterator1.next();

            try
            {
                textureatlassprite1.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable throwable1)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
                crashreportcategory.addCrashSectionCallable("Sprite name", ()->textureatlassprite1.getIconName());
                crashreportcategory.addCrashSectionCallable("Sprite size", ()->textureatlassprite1.getIconWidth() + " x " + textureatlassprite1.getIconHeight());
                crashreportcategory.addCrashSectionCallable("Sprite frames", ()->textureatlassprite1.getFrameCount() + " frames");
                crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
                throw new ReportedException(crashreport);
            }
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        skipFirst = false;

        try
        {
            stitcher.doStitch();
        }
        catch (StitcherException stitcherexception)
        {
            throw stitcherexception;
        }

        FleLog.getLogger().info("Created: {}x{} {}-atlas", new Object[] {Integer.valueOf(stitcher.getCurrentWidth()), Integer.valueOf(stitcher.getCurrentHeight()), this.basePath});
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), (float)this.anisotropicFiltering);
        HashMap hashmap = Maps.newHashMap(this.mapRegisteredSprites);
        Iterator iterator2 = stitcher.getStichSlots().iterator();

        while (iterator2.hasNext())
        {
            textureatlassprite = (TextureAtlasSprite)iterator2.next();
            String s = textureatlassprite.getIconName();
            hashmap.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite);

            try
            {
                TextureUtil.uploadTextureMipmap(textureatlassprite.getFrameTextureData(0), textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight(), textureatlassprite.getOriginX(), textureatlassprite.getOriginY(), false, false);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
                crashreportcategory1.addCrashSection("Atlas path", this.basePath);
                crashreportcategory1.addCrashSection("Sprite", textureatlassprite);
                throw new ReportedException(crashreport1);
            }

            if (textureatlassprite.hasAnimationMetadata())
            {
                this.listAnimatedSprites.add(textureatlassprite);
            }
            else
            {
                textureatlassprite.clearFramesTextureData();
            }
        }

        iterator2 = hashmap.values().iterator();

        while (iterator2.hasNext())
        {
            textureatlassprite = (TextureAtlasSprite)iterator2.next();
            textureatlassprite.copyFrom(this.missingImage);
        }
        //cpw.mods.fml.client.FMLClientHandler.instance().logMissingTextureErrors();
    }
        
    public void registerTextureListener(ITextureLoadListener listener)
    {
    	listeners.add(listener);
    }
    
    private void registerIcons()
    {
		mapRegisteredSprites.clear();
		for(ITextureLoadListener listener : listeners)
		{
			listener.registerIcons(this);
		}
	}
    
	private ResourceLocation completeResourceLocation(ResourceLocation location, int type)
    {
        return type == 0 ? 
        		new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath, location.getResourcePath(), ".png"})): 
        			new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] {this.basePath, location.getResourcePath(), Integer.valueOf(type), ".png"}));
    }
    
	@Override
	public IIcon registerIcon(String string) 
	{
        if (string == null)
        {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        else if (string.indexOf('\\') == -1) // Disable backslashes (\) in texture asset paths.
        {
            IIcon icon = mapRegisteredSprites.get(string);

            if (icon == null)
            {
            	icon = new FleTextureAtlasIcon(string);
            	mapRegisteredSprites.put(string, icon);
            }
            
            return icon;
        }
        else
        {
            throw new IllegalArgumentException("Name cannot contain slashes!");
        }
	}

    private void initMissingImage()
    {
        int[] aint;

        if ((float)this.anisotropicFiltering > 1.0F)
        {
            this.missingImage.setIconWidth(16);//Missing texture use 16x16
            this.missingImage.setIconHeight(16);
            aint = new int[1024];
            System.arraycopy(TextureUtil.missingTextureData, 0, aint, 0, TextureUtil.missingTextureData.length);
            TextureUtil.prepareAnisotropicData(aint, 16, 16, 8);
        }
        else
        {
            aint = TextureUtil.missingTextureData;
            this.missingImage.setIconWidth(16);
            this.missingImage.setIconHeight(16);
        }

        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] {aint1}));
    }

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException
	{
        initMissingImage();
        deleteGlTexture();
        loadTextureAtlas(resourceManager);
	}
}