package farcore.lib.skill;

import farcore.lib.collection.Register;
import farcore.lib.util.LanguageManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

public class SkillAbstract implements ISkill
{
	protected static final Register<SkillAbstract> REGISTER = new Register();
	
	public static SkillAbstract getSkill(String name)
	{
		return REGISTER.get(name);
	}
	
	private final String name;
	private final int id;
	private float expIncrease;
	private float expBase;
	private float maxLevel;

	public SkillAbstract(String name)
	{
		this.name = name;
		id = REGISTER.register(name, this);
	}
	
	public SkillAbstract setExpInfo(int maxLv, float expBase, float expIncr)
	{
		maxLevel = maxLv;
		this.expBase = expBase;
		expIncrease = expIncr;
		return this;
	}

	public String getLocalName()
	{
		return LanguageManager.translateToLocal("skill." + name + ".name");
	}
	
	@Override
	public String getRegisteredName()
	{
		return name;
	}
	
	@Override
	public int level(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if(tag.hasKey("skill"))
			return tag.getCompoundTag("skill").getCompoundTag(name).getByte("lv");
		return 0;
	}
	
	@Override
	public void using(EntityPlayer player, float exp)
	{
		NBTTagCompound tag = player.getEntityData();
		if(!tag.hasKey("skill"))
		{
			tag.setTag("skill", new NBTTagCompound());
		}
		tag = tag.getCompoundTag("skill");
		NBTTagCompound tag1;
		if(!tag.hasKey(name))
		{
			tag1 = new NBTTagCompound();
			tag.setTag(name, tag1);
		}
		else
		{
			tag1 = tag.getCompoundTag(name);
		}
		byte level = tag1.getByte("lv");
		if(level < 0)
		{
			level = 0;
		}
		if(level < maxLevel)
		{
			float e = tag1.getFloat("exp");
			float eNeed = (float) Math.exp(level * expIncrease) * expBase;
			e += exp;
			if(e >= eNeed)
			{
				tag1.setByte("lv", (byte) (level + 1));
				tag1.setFloat("exp", e - eNeed);
				player.addChatComponentMessage(new TextComponentString(
						LanguageManager.translateToLocal("skill.upgrade.info", getLocalName(), level, level + 1)));
			}
			else
			{
				tag1.setFloat("exp", e);
			}
		}
	}

	@Override
	public void set(EntityPlayer player, int level)
	{
		NBTTagCompound tag = player.getEntityData();
		if(!tag.hasKey("skill"))
		{
			tag.setTag("skill", new NBTTagCompound());
		}
		tag = tag.getCompoundTag("skill");
		NBTTagCompound tag1;
		if(!tag.hasKey(name))
		{
			tag1 = new NBTTagCompound();
			tag.setTag(name, tag1);
		}
		else
		{
			tag1 = tag.getCompoundTag(name);
		}
		tag1.setByte("lv", (byte) level);
	}
}