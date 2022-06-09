package ru.jekarus.skyfortress.module.damage;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class SfDamageSource {

    public final EntityDamageEvent.DamageCause cause;
    public final Entity entity;
    public final Entity projectile;

    public SfDamageSource(EntityDamageEvent.DamageCause cause, Entity damager) {
        this(cause, damager, null);
    }

    public SfDamageSource(EntityDamageEvent.DamageCause cause, @Nullable Entity damager, Projectile projectile) {
        this.cause = cause;
        this.projectile = projectile;
        this.entity = damager;
    }

    public SfDamageSource(EntityDamageEvent.DamageCause cause) {
        this.cause = cause;
        this.entity = null;
        this.projectile = null;
    }

    @Override
    public String toString() {
        if(isProjectile()) return this.cause + " " + this.entity + " using " + this.projectile;
        if(!isEnvironment()) return this.cause + " " + this.entity;
        return this.cause.toString();
    }

    public boolean isPlayer() {
        return entity != null && entity.getType() == EntityType.PLAYER;
    }

    public boolean isObject() {
        return entity != null && entity.getType() != EntityType.PLAYER;
    }
    public boolean isProjectile() {
        return projectile != null;
    }

    public boolean isEnvironment() {
        return entity == null;
    }

    boolean isDamageHelmet() {
        return cause == EntityDamageEvent.DamageCause.FALLING_BLOCK;
    }

    boolean isBypassMagic() {
        return cause == EntityDamageEvent.DamageCause.STARVATION;
    }
    public boolean isBypassInvul() {
        return cause == EntityDamageEvent.DamageCause.VOID;
    }
    boolean isBypassArmor() {
        if(cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) return false;
        if(cause == EntityDamageEvent.DamageCause.HOT_FLOOR) return false;
        if(cause == EntityDamageEvent.DamageCause.LAVA) return false;
        if(cause == EntityDamageEvent.DamageCause.LIGHTNING) return false;
        if(cause == EntityDamageEvent.DamageCause.CONTACT) return false;  // fixme STALAGMITE
        if(cause == EntityDamageEvent.DamageCause.PROJECTILE) return false;
        return true;
    }

    public Location getSourcePosition() {
        if(entity != null) return entity.getLocation();
        return null;
    }

    public boolean hasEffect(PotionEffectType effectType) {
        if(entity == null) return false;
        if(entity instanceof LivingEntity living) {
            return living.hasPotionEffect(effectType);
        }
        return false;
    }

    public @Nullable PotionEffect getEffect(PotionEffectType effectType) {
        if(entity == null) return null;
        if(entity instanceof LivingEntity living) {
            return living.getPotionEffect(effectType);
        }
        return null;
    }

    public boolean isFire() {
        if(cause == EntityDamageEvent.DamageCause.FIRE) return true;
        if(cause == EntityDamageEvent.DamageCause.FIRE_TICK) return true;
        if(cause == EntityDamageEvent.DamageCause.LAVA) return true;
        if(cause == EntityDamageEvent.DamageCause.HOT_FLOOR) return true;
        return false;
    }

    public boolean isFall() {
        if(cause == EntityDamageEvent.DamageCause.FALL) return true;
//        if(cause == EntityDamageEvent.DamageCause.STALAGMITE) return true;
        return false;
    }

    public boolean isExplosion() {
//        if(cause == EntityDamageEvent.DamageCause.FIRE) return true;
        if(cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return true;
        if(cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return true;
        return false;
    }
}
