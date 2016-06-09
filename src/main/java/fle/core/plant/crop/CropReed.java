package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropReed extends CropFle
{
	public CropReed()
	{
		super("reed");
		setTextureName("fle:crop/reed");
		waterWaste = 4;
		maxGrowWaterUse = 22;
		maxStage = 6;
		maxTemp = 421;
		minAliveTemp = 274;
		minGrowTemp = 283;
		minAliveWaterReq = 300;
		minGrowWaterReq = 800;
		growReq = 1400;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.hotResistance = 1;
	}
	
	@Override
	protected void applyEffect(CropInfo info, String prop)
	{
		switch (prop)
		{
		case "xerophily" : info.dryResistance += 2;
		break;
		case "grain" : info.grain += 1;
		break;
		case "growth" : info.growth += 2;
		break;
		case "resistance" : info.weedResistance += 2;
		break;
		case "cold" : info.coldResistance += 2;
		break;
		case "hot" : info.hotResistance += 3;
		break;
		}
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "hot" : "xerophily";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 4)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			list.add(applyChildSeed(access.rng().nextInt(grain / 2 + 1) + 2, info));
		}
	}
}