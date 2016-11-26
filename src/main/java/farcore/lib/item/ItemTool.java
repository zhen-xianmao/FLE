package farcore.lib.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import farcore.data.EnumToolType;
import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.item.IItemBehaviorsAndProperties.IIB_BlockHarvested;
import farcore.lib.item.IItemBehaviorsAndProperties.IIP_DigSpeed;
import farcore.lib.item.behavior.IBehavior;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.skill.ISkill;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.lib.world.IEnvironment;
import farcore.util.Localization;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTool extends ItemSubBehavior
implements ITool, IUpdatableItem, IIB_BlockHarvested, IIP_DigSpeed
{
	public static Mat getMaterial(ItemStack stack, String part)
	{
		if(stack != null && stack.getItem() instanceof ItemTool)
			return ((ItemTool) stack.getItem()).getMaterialFromItem(stack, part);
		else
			return Mat.VOID;
	}
	
	@SideOnly(Side.CLIENT)
	public static int getColor(ItemStack stack, int pass)
	{
		if(stack != null && stack.getItem() instanceof ItemTool)
			return ((ItemTool) stack.getItem()).getToolProp(stack).stat.getColor(stack, pass);
		else
			return -1;
	}
	
	public static float getToolDamage(ItemStack stack)
	{
		return U.ItemStacks.getOrSetupNBT(stack, false).getCompoundTag("durability").getFloat("damage");
	}
	
	public static int getDefaultMaxDurability(ToolProp prop, ItemStack stack)
	{
		//		if(prop.hasHandle)
		//		{
		//			Mat head = getMaterial(stack, "head");
		//			Mat handle = getMaterial(stack, "handle");
		//			return (int) (head.toolMaxUse * handle.handleToughness);
		//		}
		//		else
		return getMaterial(stack, "head").getProperty(MP.property_tool).maxUse;
	}
	
	public static int getMaxDurability(ItemStack stack)
	{
		ToolProp prop = ((ItemTool) stack.getItem()).toolPropMap.get(((ItemTool) stack.getItem()).getBaseDamage(stack));
		NBTTagCompound tag = stack.getSubCompound("durability", true);
		if(!tag.hasKey("maxDurability"))
		{
			if(U.Sides.isServer() || !U.Sides.isSimulating())
			{
				tag.setInteger("maxDurability", (int) (prop.stat.getMaxDurabilityMultiplier() * getDefaultMaxDurability(prop, stack)));
			}
			else
				return getDefaultMaxDurability(prop, stack);
		}
		return tag.getInteger("maxDurability");
	}
	
	public static float getDurability(ItemStack stack)
	{
		return getMaxDurability(stack) - getToolDamage(stack);
	}
	
	public static void setToolDamage(ItemStack stack, float damage)
	{
		stack.getSubCompound("durability", true).setFloat("damage", damage);
	}
	
	public static final ToolProp EMPTY_PROP;
	
	static
	{
		EMPTY_PROP = new ToolProp();
		EMPTY_PROP.id = -1;
		EMPTY_PROP.hasHandle = false;
		EMPTY_PROP.hasTie = false;
		EMPTY_PROP.filterHead = IDataChecker.FALSE;
		EMPTY_PROP.filterHandle = IDataChecker.FALSE;
		EMPTY_PROP.filterTie = IDataChecker.FALSE;
		EMPTY_PROP.customToolInformation = null;
		EMPTY_PROP.condition = MC.LATTICE;
		EMPTY_PROP.toolTypes = ImmutableList.of();
	}
	
	public static class ToolProp
	{
		int id;
		boolean hasTie;
		boolean hasHandle;
		MatCondition condition;
		IToolStat stat;
		IDataChecker<? extends ISubTagContainer> filterHead;
		IDataChecker<? extends ISubTagContainer> filterTie;
		IDataChecker<? extends ISubTagContainer> filterHandle;
		List<EnumToolType> toolTypes;
		String customToolInformation;
		public ISkill skillEfficiency;
		public ISkill skillAttack;
	}
	
	Map<Integer, ToolProp> toolPropMap = new HashMap();
	protected String textureFileName = "tool/";
	
	protected boolean modelFlag = true;
	
	protected ItemTool(String name)
	{
		super(name);
	}
	protected ItemTool(String modid, String name)
	{
		super(modid, name);
	}
	
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation,
			MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle,
			IDataChecker<? extends ISubTagContainer> filterHead,
			IDataChecker<? extends ISubTagContainer> filterTie, IDataChecker<? extends ISubTagContainer> filterHandle,
			List<EnumToolType> toolTypes,
			IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, stat, behaviors);
		if(this.modelFlag)
		{
			U.Mod.registerItemModel(this, id, this.modid, this.textureFileName + name);
		}
		ToolProp prop = new ToolProp();
		prop.id = id;
		prop.condition = condition;
		prop.hasTie = hasTie;
		prop.hasHandle = hasHandle;
		prop.stat = stat;
		prop.filterHead = filterHead;
		prop.filterHandle = filterHandle;
		prop.filterTie = filterTie;
		prop.customToolInformation = customToolInformation;
		prop.toolTypes = toolTypes;
		this.toolPropMap.put(id, prop);
		if(customToolInformation != null)
		{
			LanguageManager.registerLocal("info.tool." + getUnlocalizedName() + "@" + id, customToolInformation);
		}
		return prop;
	}
	
	protected Mat getMaterialFromItem(ItemStack stack, String part)
	{
		NBTTagCompound nbt = U.ItemStacks.getOrSetupNBT(stack, false).getCompoundTag("tool");
		return Mat.material(nbt.getString(part));
	}
	
	public ItemStack setMaterialToItem(ItemStack stack, String part, Mat material)
	{
		stack.getSubCompound("tool", true).setString(part, material.name);
		return stack;
	}
	
	protected ToolProp getToolProp(ItemStack stack)
	{
		return this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
	}
	
	@Override
	public List<EnumToolType> getToolTypes(ItemStack stack)
	{
		return this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).toolTypes;
	}
	
	@Override
	public int getToolLevel(ItemStack stack, EnumToolType type)
	{
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		int level = prop.stat.getToolHarvestLevel(stack, type.name(), getMaterial(stack, "head"));
		return level == -1 ?
				prop.toolTypes.contains(type) ? getMaterial(stack, "head").getProperty(MP.property_tool).harvestLevel : -1 :
					level;
	}
	
	@Override
	public void onToolUse(EntityLivingBase user, ItemStack stack, EnumToolType toolType, float amount)
	{
		if(!isItemUsable(stack)) return;
		if(user instanceof EntityPlayer && ((EntityPlayer) user).capabilities.isCreativeMode)
			return;
		int max = getMaxDurability(stack);
		float now = getToolDamage(stack);
		if(now + amount >= max)
		{
			stack.stackSize --;
			if(user != null)
			{
				user.renderBrokenItemStack(stack);
			}
			if(stack.stackSize != 0)
			{
				setToolDamage(stack, 0F);
			}
		}
		else
		{
			setToolDamage(stack, now + amount);
		}
	}
	
	protected float getPlayerRelatedAttackDamage(ToolProp prop, ItemStack stack, EntityPlayer player, float baseAttack, float attackSpeed, int cooldown, boolean isAttackerFalling)
	{
		float multiple;
		switch (prop.stat.getPhysicalDamageType())
		{
		case SMASH :
		default :
			multiple = .9F * farcore.util.L.range(0F, 1F, cooldown * attackSpeed / 100F) + .1F;
			if(isAttackerFalling)
			{
				baseAttack *= 1.5F;
			}
			break;
		case PUNCTURE :
			multiple = .75F * farcore.util.L.range(0F, 1F, cooldown * cooldown * cooldown * attackSpeed / 100F) + .25F;
			if(isAttackerFalling)
			{
				baseAttack *= 1.25F;
			}
			break;
		case CUT :
			multiple = .8F * farcore.util.L.range(0F, 1F, cooldown * cooldown * attackSpeed / 100F) + .2F;
			if(isAttackerFalling)
			{
				baseAttack *= 1.75F;
			}
			break;
		}
		baseAttack *= multiple;
		return baseAttack;
	}
	
	@Override
	public float replaceDigSpeed(ItemStack stack, BreakSpeed event)
	{
		return !isItemUsable(stack) ? 0.0F : this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getMiningSpeed(stack, event.getEntityPlayer(), event.getEntityPlayer().worldObj, event.getPos(), event.getState(), event.getOriginalSpeed());
	}
	
	@Override
	public void onBlockHarvested(ItemStack stack, HarvestDropsEvent event)
	{
		if(!isItemUsable(stack)) return;
		IToolStat stat = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat;
		if(stat.canHarvestDrop(stack, event.getState()))
		{
			onToolUse(event.getHarvester(), stack, stat.getToolType(), stat.getToolDamagePerBreak(stack, event.getHarvester(), event.getWorld(), event.getPos(), event.getState()));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		EnumActionResult result = super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.PASS)
			return U.ItemStacks.onUseOnBlock(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ);
		return result;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(super.onLeftClickEntity(stack, player, entity))
			return true;
		WeaponHelper.onToolUsedToAttack(this, stack, player, entity);
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		ActionResult<ItemStack> result;
		if((result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)).getType() != EnumActionResult.PASS)
			return result;
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(itemStackIn), EMPTY_PROP);
		if(prop.stat.canBlock())
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return super.canHarvestBlock(state, stack);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getToolType().getToolClasses();
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getToolHarvestLevel(stack, toolClass, getMaterial(stack, "head"));
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getSpeedMultiplier(stack);
	}
	
	@Override
	public boolean isItemTool(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		//		if(slot == EntityEquipmentSlot.MAINHAND)
		//		{
		//			Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		//			Mat material = getMaterial(stack, "head");
		//			ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		//			float speed = prop.stat.getAttackSpeed(stack, material);
		//			if(speed != 0)
		//			{
		//				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", speed, 0));
		//			}
		//			return multimap;
		//		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		return prop.stat.canBlock() ? EnumAction.BLOCK :
			prop.stat.isShootable() ? EnumAction.BOW : EnumAction.NONE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		subItems.add(setMaterialToItem(new ItemStack(this, 1, meta), "head", Mat.VOID));
	}
	
	@Override
	public int getMaxDamage()
	{
		return 0;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) getToolDamage(stack) / (double) getMaxDurability(stack);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getToolDamage(stack) != 0;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		ItemStack stack2 = stack;
		if(!entityIn.worldObj.isRemote)
		{
			stack = ((IUpdatableItem) this).updateItem(null, stack);
			if(entityIn instanceof EntityPlayer)
			{
				if(stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
			}
			if(stack.getItem() != this)
				return;
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		boolean flag = super.onEntityItemUpdate(entityItem);
		if(!entityItem.worldObj.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(null, entityItem.getEntityItem());
			if(stack == null)
			{
				entityItem.setDead();
				return false;
			}
			else if(stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if(stack.getItem() != this)
				return true;
		}
		return flag;
	}
	
	@Override
	public ItemStack updateItem(IEnvironment environment, ItemStack stack)
	{
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		Mat material = getMaterialFromItem(stack, "head");
		if(material.itemProp != null)
		{
			stack = material.itemProp.updateItem(stack, material, prop.condition, environment, "");
		}
		if(stack != null && prop.hasTie)
		{
			material = getMaterialFromItem(stack, "tie");
			if(material.itemProp != null)
			{
				stack = material.itemProp.updateItem(stack, material, MC.tie, environment);
			}
		}
		if(stack != null && prop.hasHandle)
		{
			material = getMaterialFromItem(stack, "handle");
			if(material.itemProp != null)
			{
				stack = material.itemProp.updateItem(stack, material, MC.handle, environment);
			}
		}
		return stack;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public int getStackMetaOffset(ItemStack stack)
	{
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		Mat material = getMaterialFromItem(stack, "head");
		if(material != null && material.itemProp != null)
			return material.itemProp.getMetaOffset(stack, material, prop.condition, "head");
		return 0;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		int base = damage & 0x7FFF;
		super.setDamage(stack, base);
		Mat material = getMaterial(stack, "head");
		if(material.itemProp != null)
		{
			material.itemProp.setInstanceFromMeta(stack, damage >> 15, material, this.toolPropMap.getOrDefault(base, EMPTY_PROP).condition);
		}
	}
	
	protected String getBaseTranslateInformation(ItemStack stack)
	{
		return "tool." + getUnlocalizedName() + "@" + getBaseDamage(stack) + ".info";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		int max = getMaxDurability(stack);
		float now = getDurability(stack);
		ToolProp prop = this.toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		unlocalizedList.addToolTip(getBaseTranslateInformation(stack));
		Localization.addDamageInformation((int) (now * 100), max * 100, unlocalizedList);
		Mat material = getMaterialFromItem(stack, "head");
		Localization.addToolMaterialInformation(material, prop.stat, unlocalizedList);
		if(material.itemProp != null)
		{
			material.itemProp.addInformation(stack, material, prop.condition, unlocalizedList, "head");
		}
		if(prop.hasHandle)
		{
			material = getMaterialFromItem(stack, "handle");
			unlocalizedList.add("info.tool.handle.name", material.getLocalName());
			unlocalizedList.addToolTip("info.material.custom." + material.getLocalName());
			if(material.itemProp != null)
			{
				material.itemProp.addInformation(stack, material, MC.handle, unlocalizedList, "handle");
			}
		}
		if(prop.hasTie)
		{
			material = getMaterialFromItem(stack, "tie");
			unlocalizedList.add("info.tool.tie.name", material.getLocalName());
			unlocalizedList.addToolTip("info.material.custom." + material.getLocalName());
			if(material.itemProp != null)
			{
				material.itemProp.addInformation(stack, material, MC.tie, unlocalizedList, "tie");
			}
		}
		unlocalizedList.add(prop.stat.getPhysicalDamageType().getTranslation());
	}
}