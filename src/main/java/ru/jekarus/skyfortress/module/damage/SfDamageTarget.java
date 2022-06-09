package ru.jekarus.skyfortress.module.damage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SfDamageTarget {

    private final Entity entity;
    public SfDamageTarget(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isBlocking() {
        if(entity instanceof HumanEntity human) return human.isBlocking();
        return false;
    }

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR || item.getAmount() <= 0; // Paper
    }

    public int getArmorValue() {
        if(entity instanceof LivingEntity living) {
            final var armorValue = living.getAttribute(Attribute.GENERIC_ARMOR);
            if(armorValue == null) return 0;
            return (int) Math.floor(armorValue.getValue());
        }
        return 0;
    }
    public double getArmorToughnessValue() {
        if(entity instanceof LivingEntity living) {
            final var toughnessValue = living.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
            if(toughnessValue == null) return 0;
            return Math.floor(toughnessValue.getValue());
        }
        return 0;
    }

    public boolean isDamageSourceBlocked(SfDamageSource source) {
        if(source.isBypassArmor()) return false;
        boolean hasPiercing = false;
        if(source.isProjectile() && source.projectile instanceof Arrow arrow) {
            if (arrow.getPierceLevel() > 0) {
                hasPiercing = true;
            }
        }
        if(!hasPiercing && this.isBlocking()) {
            final var location = source.getSourcePosition();
            if (location != null) {
                final var viewVector = entity.getLocation().getDirection();
                final var toAttacker = entity.getLocation().subtract(location).toVector().normalize();
                final var toAttackerHor = new Vector(toAttacker.getX(), 0.0, toAttacker.getZ());
                if(toAttackerHor.dot(viewVector) < 0.0d) {
                    return true;
                }
            }
        }
        return false;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    public static float getDamageAfterAbsorb(float damage, float armor, float armorToughness) {
        float f = 2.0F + armorToughness / 4.0F;
        float g = clamp(armor - damage / f, armor * 0.2F, 20.0F);
        return damage * (1.0F - g / 25.0F);
    }

    public int getDamageProtection(SfDamageSource source, Enchantment ench, int level) {
        if(source.isBypassInvul()) return 0;
        if(ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return level;
        if(ench.equals(Enchantment.PROTECTION_FIRE) && source.isFire()) return level * 2;
        if(ench.equals(Enchantment.PROTECTION_FALL) && source.isFall()) return level * 3;
        if(ench.equals(Enchantment.PROTECTION_EXPLOSIONS) && source.isExplosion()) return level * 2;
        if(ench.equals(Enchantment.PROTECTION_PROJECTILE) && source.isProjectile()) return level * 2;
        return 0;
    }

    public int getDamageProtection(SfDamageSource source) {
        int protection = 0;
        if(entity instanceof HumanEntity human) {
            for (@Nullable ItemStack stack : human.getEquipment().getArmorContents()) {
                if(stack == null) continue;
                final var enchantments = stack.getEnchantments();
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    protection += getDamageProtection(source, entry.getKey(), entry.getValue());
                }
            }
        }
        return protection;
    }


    public static float getDamageAfterMagicAbsorb(float damageDealt, float protection) {
        float f = clamp(protection, 0.0F, 20.0F);
        return damageDealt * (1.0F - f / 25.0F);
    }
    public float getDamageAfterMagicAbsorb(SfDamageSource source, float amount) {
        if (source.isBypassMagic()) return amount;

        int i;

        // CraftBukkit - Moved to damageEntity0(DamageSource, float)
        if (false &&
                this.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) &&
                source.cause != EntityDamageEvent.DamageCause.VOID
        ) {
            i = (this.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f1 = amount * (float) j;
            float f2 = amount;

            amount = Math.max(f1 / 25.0F, 0.0F);
            float f3 = f2 - amount;

            if (f3 > 0.0F && f3 < 3.4028235E37F) {
//                if (target instanceof Player player) {
//                    player.awardStat(Stats.DAMAGE_RESISTED, Math.round(f3 * 10.0F));
//                } else if (source.entity instanceof Player player) {
//                    player.awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f3 * 10.0F));
//                }
            }
        }

        if (amount <= 0.0F) {
            return 0.0F;
        } else {
            int protection = this.getDamageProtection(source);
            if (protection > 0) {
                amount = getDamageAfterMagicAbsorb(amount, (float) protection);
            }

            return amount;
        }
    }

    public @Nullable PotionEffect getPotionEffect(PotionEffectType effectType) {
        if(entity instanceof LivingEntity living) return living.getPotionEffect(effectType);
        return null;
    }

    public boolean hasPotionEffect(PotionEffectType effectType) {
        if(entity instanceof LivingEntity living) return living.hasPotionEffect(effectType);
        return false;
    }

    public float getDamageAfterArmorAbsorb(SfDamageSource source, float amount) {
        if (source.isBypassArmor()) return amount;

        // this.damageArmor(damagesource, f); // CraftBukkit - Moved into damageEntity0(DamageSource, float)
        return getDamageAfterAbsorb(amount, (float) this.getArmorValue(), (float) this.getArmorToughnessValue());
    }

    public ItemStack getEquipment(EquipmentSlot slot) {
        if(entity instanceof HumanEntity human) return human.getEquipment().getItem(slot);
        return null;
    }

    public double getAbsorptionAmount() {
        if(entity instanceof LivingEntity living) return living.getAbsorptionAmount();
        return 0;
    }
}
