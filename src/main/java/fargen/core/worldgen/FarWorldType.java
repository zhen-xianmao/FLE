package fargen.core.worldgen;

import farcore.lib.util.LanguageManager;
import farcore.util.U.R;
import fargen.core.worldgen.surface.FarSurfaceChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FarWorldType extends WorldType
{
	public static FarWorldType DEFAULT;
	public static FarWorldType FLAT;
	public static FarWorldType LARGE_BIOMES;
	
	public FarWorldType(int index, String name, String localName)
	{
		super(name);
		WORLD_TYPES[index] = this;
		int oldIndex = getWorldTypeID();
		WORLD_TYPES[oldIndex] = null;
		try
		{
			R.overrideFinalField(WorldType.class, "worldTypeId", "field_82748_f", this, index, true, false);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		LanguageManager.registerLocal("generator." + name, localName);
	}
	public FarWorldType(String name, String localName)
	{
		super(name);
		LanguageManager.registerLocal("generator." + name, localName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslateName()
	{
		return LanguageManager.translateToLocal(super.getTranslateName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedInfo()
	{
		return LanguageManager.translateToLocal(super.getTranslateName() + ".info");
	}
	
	@Override
	public double getHorizon(World world)
	{
		return 127.0D;
	}

	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return world.provider.isSurfaceWorld() ? 128 : 64;
	}
	
	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
	{
		switch (world.provider.getDimension())
		{
		case 0 : return new FarSurfaceChunkGenerator(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		default: break;
		}
		return super.getChunkGenerator(world, generatorOptions);
	}
	
	@Override
	public float getCloudHeight()
	{
		return 220F;
	}
	
	@Override
	public BiomeProvider getBiomeProvider(World world)
	{
		return super.getBiomeProvider(world);
	}
}