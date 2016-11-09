/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.item;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.M;
import farcore.event.AttackEvent;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.IItemMatProp;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyTool;
import farcore.util.U;
import farcore.util.U.L;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.math.MathHelper;

/**
 * The weapon has too many optional actions, split to a class type.
 * @author ueyudiud
 */
public class WeaponHelper
{
	public static void onToolUsedToAttack(ItemTool tool, ItemStack stack, EntityPlayer player, Entity entity)
	{
		ItemTool.ToolProp prop = tool.toolPropMap.getOrDefault(tool.getBaseDamage(stack), ItemTool.EMPTY_PROP);
		EnumPhysicalDamageType type;
		AttackEvent event = AttackEvent.post(player, entity, stack, type = prop.stat.getPhysicalDamageType());
		if(event.isCanceled())
			return;
		stack = event.newWeapon;
		Mat material = ItemTool.getMaterial(stack, "head");
		PropertyTool toolProperty = material.getProperty(M.property_tool);
		IItemMatProp materialProperty = material.itemProp;
		if(entity.canBeAttackedWithItem() && !entity.hitByEntity(player))// && !entity.isInvisibleToPlayer(player))
		{
			float baseMultiple = 1F;
			switch (type)
			{
			case SMASH :
				if(player.isPotionActive(MobEffects.STRENGTH))
				{
					baseMultiple += (player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() + 1) * 0.25F;
				}
				if(player.isPotionActive(MobEffects.WEAKNESS))
				{
					baseMultiple -= (player.getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier() + 1) * 0.25F;
				}
				break;
			case CUT :
				break;
			case PUNCTURE :
				baseMultiple = 0.8F + L.range(0F, 0.4F, (float) (player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ * player.motionZ) / 20F);
				break;
			default:
				break;
			}
			if(materialProperty != null)
			{
				baseMultiple += materialProperty.entityAttackDamageMultiple(stack, material, entity);
			}
			float attack = prop.stat.getDamageVsEntity(stack, entity) * toolProperty.damageToEntity * baseMultiple;
			boolean flagCritical = matchCritical(player) && (entity instanceof EntityLivingBase);
			float speed = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() + prop.stat.getAttackSpeed(stack) + toolProperty.attackSpeed;
			speed = L.range(0.1F, 20.0F, speed);
			int cooldown = (int) U.R.getValue(EntityLivingBase.class, "ticksSinceLastSwing", "field_184617_aD", player, false);

			int fire = EnchantmentHelper.getFireAspectModifier(player);

			if (entity instanceof EntityLivingBase && fire > 0 && !entity.isBurning())
			{
				entity.setFire(fire);
			}

			attack = tool.getPlayerRelatedAttackDamage(prop, stack, player, attack, speed, cooldown, flagCritical);

			double d1 = entity.motionX;
			double d2 = entity.motionY;
			double d3 = entity.motionZ;

			player.resetCooldown();

			if(attack > 0 && entity.attackEntityFrom(prop.stat.getDamageSource(player, entity), attack))
			{
				float knockback = prop.stat.getKnockback(stack, material, entity) + EnchantmentHelper.getKnockbackModifier(player);
				if(player.isSprinting())
				{
					player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
					knockback += 1F;
				}
				if(knockback > 0)
				{
					entity.addVelocity(-Math.sin(player.rotationYaw * Math.PI / 180D) * knockback * .5, 0.05F, Math.cos(player.rotationYaw * Math.PI / 180D) * knockback * .5);
				}
				player.motionX *= 0.6F;
				player.motionZ *= 0.6F;
				player.setSprinting(false);

				float[] box = prop.stat.getAttackExpandBoxing(stack, material);
				if(box != null)
				{
					causeAOEAttack(prop.stat, player, entity, box);
					player.worldObj.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
					player.spawnSweepParticles();
				}

				if (entity instanceof EntityPlayerMP && entity.velocityChanged)
				{
					((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity(entity));
					entity.velocityChanged = false;
					entity.motionX = d1;
					entity.motionY = d2;
					entity.motionZ = d3;
				}
				else if(!flagCritical)
				{
					player.worldObj.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
				}
				else
				{
					player.worldObj.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
					player.onCriticalHit(entity);
				}

				if(attack >= 18.0F)
				{
					player.addStat(AchievementList.OVERKILL);
				}

				entity.hurtResistantTime = Math.max(1, entity.hurtResistantTime + 1);
				player.setLastAttacker(entity);
				if (entity instanceof EntityLivingBase)
				{
					EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, player);
				}
				EnchantmentHelper.applyArthropodEnchantments(player, entity);
				if(prop.stat.isWeapon() && prop.skillAttack != null)
				{
					prop.skillAttack.using(player, 1.0F);
				}
				tool.onToolUse(player, stack, prop.stat.getToolType(), prop.stat.getToolDamagePerAttack(stack, player, entity));
				player.addExhaustion(0.3F);
			}
		}
		U.Players.destoryPlayerCurrentItem(player);
	}
	
	public static boolean matchCritical(EntityPlayer player)
	{
		return player.fallDistance > 0.0F &&
				!player.onGround && !player.isOnLadder() &&
				!player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) &&
				player.getRidingEntity() == null;
	}
	
	public static void causeAOEAttack(IToolStat stat, EntityLivingBase attacker, Entity target, float[] boundBox)
	{
		for (EntityLivingBase entitylivingbase : attacker.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().expand(boundBox[0], boundBox[1], boundBox[0])))
		{
			if (entitylivingbase != attacker && entitylivingbase != target && !attacker.isOnSameTeam(entitylivingbase) && attacker.getDistanceSqToEntity(entitylivingbase) < 9.0D)
			{
				entitylivingbase.knockBack(attacker, 0.4F, MathHelper.sin(attacker.rotationYaw * 0.017453292F), (-MathHelper.cos(attacker.rotationYaw * 0.017453292F)));
				entitylivingbase.attackEntityFrom(stat.getDamageSource(attacker, entitylivingbase), 1.0F);
			}
		}
	}
}