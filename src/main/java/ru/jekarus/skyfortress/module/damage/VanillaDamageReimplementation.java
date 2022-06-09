package ru.jekarus.skyfortress.module.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

public class VanillaDamageReimplementation extends SfDamageProcessor {


    public VanillaDamageReimplementation(EntityDamageEvent event, SfDamageSource source, SfDamageTarget target) {
        super(event, source, target);
    }

    @Override
    public double hardHat(double f) {
        if(!source.isDamageHelmet()) return -0.0;
        if (!SfDamageTarget.isEmpty(target.getEquipment(EquipmentSlot.HEAD))) {
            return -(f - (f * 0.75F));
        }
        return -0.0;
    }

    @Override
    public double blocking(double f) {  // shields
        if (target.isDamageSourceBlocked(source)) return -f;
        return -0.0;
    }

    @Override
    public double armor(double f) {
        final double newF = target.getDamageAfterArmorAbsorb(source, (float) f);
        return -(f - newF);
    }

    @Override
    public double resistance(double f) {  // resistance effect
        if(source.isBypassMagic()) return -0.0;
        if(source.cause == EntityDamageEvent.DamageCause.VOID) return -0.0;
        if (target.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            final var damageRes = target.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier();
            int i = (damageRes + 1) * 5;
            int j = 25 - i;
            float f1 = (float) f * (float) j;
            return -(f - (f1 / 25.0F));
        }
        return -0.0;
    }

    @Override
    public double magic(double f) {  // enchantments
        final var dmg = target.getDamageAfterMagicAbsorb(source, (float) f);
        return -(f - dmg);
    }

    @Override
    public double absorption(double f) {
        var value = target.getAbsorptionAmount();
        value = Math.max(f - Math.max(f - value, 0.0F), 0.0F);
        return -value;
    }

}
