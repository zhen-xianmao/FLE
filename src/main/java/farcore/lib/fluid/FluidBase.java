package farcore.lib.fluid;

import farcore.lib.util.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidBase extends Fluid
{
	public float fireAttackDamage;

	public FluidBase(String fluidName, String localName, ResourceLocation still, ResourceLocation flowing)
	{
		super(fluidName, still, flowing);
		FluidRegistry.registerFluid(this);
		if(localName != null)
		{
			LanguageManager.registerLocal(getTranslateName(new FluidStack(this, 1)), localName);
		}
	}
	
	protected String getTranslateName(FluidStack stack)
	{
		return getUnlocalizedName(stack) + ".name";
	}
	
	@Override
	public String getLocalizedName(FluidStack stack)
	{
		return LanguageManager.translateToLocal(getTranslateName(stack));
	}
}