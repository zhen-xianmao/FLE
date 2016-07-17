package farcore.lib.material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import farcore.FarCore;
import farcore.lib.collection.Register;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatCondition
{
	public static final Register<MatCondition> register = new Register();

	public final String orePrefix;
	public final String localName;
	public final String withOreLocalName;

	public int stackLimit = 64;

	public long size = 1296L;
	public long latticeSize = 1296L;
	public float specificArea = 1.0F;

	public boolean isFluid = false;
	public float fluidViscosityMultiply = 1.0F;

	public float maxTemp = 1E16F;
	public float minTemp = 0F;

	public IDataChecker<ISubTagContainer> filter = IDataChecker.FALSE;
	public Set<Mat> blacklist = new HashSet();

	public MatCondition(String prefix, String localName, String withOreLocalName)
	{
		orePrefix = prefix;
		this.localName = localName;
		this.withOreLocalName = withOreLocalName;
		register.register(prefix, this);
		LanguageManager.registerLocal(getTranslateName(), localName);
		LanguageManager.registerLocal(getWithOreTranslateName(), withOreLocalName);
	}

	public MatCondition setFilter(IDataChecker<ISubTagContainer> filter)
	{
		this.filter = filter;
		return this;
	}

	public MatCondition addToBlackList(Mat...mats)
	{
		if(mats.length == 1)
			blacklist.add(mats[0]);
		else
			blacklist.addAll(Arrays.asList(mats));
		return this;
	}

	public MatCondition setStackLimit(int stackLimit)
	{
		this.stackLimit = stackLimit;
		return this;
	}

	public MatCondition setSize(long size)
	{
		return setSize(size, size, 1F);
	}
	public MatCondition setSize(long size, long latticSize)
	{
		return setSize(size, latticSize, (float) ((double) size / (double) latticSize));
	}
	public MatCondition setSize(long size, long latticSize, float specificArea)
	{
		this.size = size;
		latticeSize = latticSize;
		this.specificArea = specificArea;
		return this;
	}

	public MatCondition setFluid()
	{
		return setFluid(1.0F);
	}
	public MatCondition setFluid(float viscosityMultiply)
	{
		isFluid = true;
		fluidViscosityMultiply = viscosityMultiply;
		size = 1;
		latticeSize = 1L;
		specificArea = 1296F;
		return this;
	}

	public MatCondition setTemp(float maxTemp, float minTemp)
	{
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		return this;
	}

	public boolean isBelongTo(Mat material)
	{
		return filter.isTrue(material) && !blacklist.contains(material);
	}

	String getWithOreTranslateName()
	{
		return "matcondition." + withOreLocalName + ".a.name";
	}

	String getTranslateName()
	{
		return "matcondition." + localName + ".name";
	}

	public void registerOre(Mat material, Item stack)
	{
		OreDict.registerValid(orePrefix + material.oreDictName, stack);
	}

	public void registerOre(Mat material, Block stack)
	{
		OreDict.registerValid(orePrefix + material.oreDictName, stack);
	}

	public void registerOre(Mat material, ItemStack stack)
	{
		OreDict.registerValid(orePrefix + material.oreDictName, stack);
	}

	public void registerOre(String prefix1, Mat material, ItemStack stack)
	{
		OreDict.registerValid(orePrefix + prefix1 + material.oreDictName, stack);
	}

	public String translateToLocal()
	{
		return FarCore.translateToLocal(getTranslateName());
	}

	public String translateToLocal(Mat material)
	{
		return FarCore.translateToLocal(getWithOreTranslateName(), material.getLocalName());
	}

	public String getLocal(Mat material)
	{
		return String.format(withOreLocalName, material.localName);
	}

	public String translateToLocal(String ore)
	{
		return FarCore.translateToLocal(getWithOreTranslateName(), ore);
	}
}