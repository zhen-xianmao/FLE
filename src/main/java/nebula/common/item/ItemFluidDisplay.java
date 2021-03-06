/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.item;

import java.util.List;
import java.util.Map.Entry;

import com.mojang.realmsclient.gui.ChatFormatting;

import nebula.Log;
import nebula.Nebula;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.util.UnlocalizedList;
import nebula.common.NebulaConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A item to let fluid display as item.<p>
 * You can generate fluid block in world by use this
 * item in world, or find fluid data in game.
 * @author ueyudiud
 */
public class ItemFluidDisplay extends ItemBase
{
	public ItemFluidDisplay()
	{
		super("nebula", "display.fluid");
		this.hasSubtypes = true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation("nebula", "fluid"));
		NebulaModelLoader.registerItemMetaGenerator(
				new ResourceLocation(Nebula.MODID, "display_fluid"), stack->getFluid(stack).getName());
	}
	
	public static Fluid getFluid(ItemStack stack)
	{
		return FluidRegistry.getFluid(stack.getItemDamage());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		Fluid fluid = FluidRegistry.getFluid(getDamage(stack));
		return fluid == null ? "unknown" : fluid.getUnlocalizedName();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		Fluid fluid = FluidRegistry.getFluid(getDamage(stack));
		return fluid == null ? "Unknown" : fluid.getLocalizedName(new FluidStack(fluid, 1000));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		if(NebulaConfig.displayFluidInTab)
		{
			for(Entry<Fluid, Integer> entry : FluidRegistry.getRegisteredFluidIDs().entrySet())
			{
				subItems.add(new ItemStack(itemIn, 1, entry.getValue()));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList list,
			boolean advanced)
	{
		Fluid fluid = FluidRegistry.getFluid(getDamage(stack));
		int amount = stack.hasTagCompound() ? stack.getTagCompound().getInteger("amount") : 0;
		if(fluid != null)
		{
			list.add(ChatFormatting.WHITE + "Name : " + fluid.getLocalizedName(new FluidStack(fluid, 1)));
		}
		if(amount > 0)
		{
			list.add(ChatFormatting.BLUE + "Amount : " + amount + "L");
		}
		list.add(ChatFormatting.RED + "Temperature : " + fluid.getTemperature() + "K");
		list.add(ChatFormatting.GREEN + "Viscosity : " + fluid.getViscosity() + "mm/s^2");
		list.add(ChatFormatting.YELLOW + "State : " + (fluid.isGaseous() ? "Gas" : "Liquid"));
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Fluid fluid = FluidRegistry.getFluid(getDamage(stack));
		if(fluid != null && fluid.getBlock() != null)
		{
			try
			{
				return worldIn.setBlockState(pos.offset(facing), fluid.getBlock().getDefaultState(), 3) ?
						EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}
			catch(Exception exception)
			{
				Log.warn("Catching an exception during put fluid block on ground.", exception);
				return EnumActionResult.FAIL;
			}
		}
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}