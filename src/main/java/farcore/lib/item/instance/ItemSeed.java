package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.crop.CropAccessSimulated;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.util.UnlocalizedList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeed extends ItemMulti
{
	public ItemSeed()
	{
		super(FarCore.ID, MC.seed);
		EnumItem.seed.set(this);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Mat material = getMaterialFromItem(stack);
		if(material.isCrop)
		{
			IBlockState state = worldIn.getBlockState(pos);
			if(!state.getBlock().isReplaceable(worldIn, pos))
			{
				pos = pos.offset(facing);
			}
			if(!playerIn.canPlayerEdit(pos, facing, stack))
				return EnumActionResult.FAIL;
			if(!worldIn.isRemote)
			{
				CropAccessSimulated access = new CropAccessSimulated(worldIn, pos, material.crop, getDNAFromStack(stack));
				if(!material.crop.canPlantAt(access)) return EnumActionResult.SUCCESS;
				if(worldIn.setBlockState(pos, EnumBlock.crop.block.getDefaultState(), 3))
				{
					TECrop crop = (TECrop) worldIn.getTileEntity(pos);
					crop.initCrop(getGenerationFromStack(stack), getDNAFromStack(stack), material.crop);
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.register)
		{
			if(condition.isBelongTo(material))
			{
				ItemStack stack = applySeed(1, material, 0, material.crop.makeNativeDNA());
				subItems.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		unlocalizedList.add("info.crop.type", getMaterialFromItem(stack).crop.getLocalName(getDNAFromStack(stack)));
		unlocalizedList.add("info.crop.generation", getGenerationFromStack(stack) + 1);
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
	}
	
	public static ItemStack applySeed(int size, Mat material, int generation, String dna)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("generation", (short) generation);
		nbt.setString("dna", dna);
		return new ItemStack(EnumItem.seed.item, size, material.id, nbt);
	}
	
	public static String getDNAFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? "" : stack.getTagCompound().getString("dna");
	}
	
	public static int getGenerationFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getShort("generation");
	}
}