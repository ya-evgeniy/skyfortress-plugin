package ru.jekarus.skyfortress.module.damage;

import com.google.common.base.Function;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public abstract class SfDamageProcessor {

    public static class SfDamageModifier {

        public final EntityDamageEvent.DamageModifier mod;
        public final Function<Double, Double> function;
        public double damage;

        public SfDamageModifier(EntityDamageEvent.DamageModifier mod, Function<Double, Double> function, double damage) {
            this.mod = mod;
            this.function = function;
            this.damage = damage;
        }

    }

    protected final EntityDamageEvent event;
    protected final SfDamageSource source;
    protected final SfDamageTarget target;

    private final Map<EntityDamageEvent.DamageModifier, Function<Double, Double>> modifiers = new EnumMap<>(EntityDamageEvent.DamageModifier.class);


    public SfDamageProcessor(EntityDamageEvent event, SfDamageSource source, SfDamageTarget target) {
        this.event = event;
        this.source = source;
        this.target = target;

        modifiers.put(EntityDamageEvent.DamageModifier.HARD_HAT, this::hardHat);
        modifiers.put(EntityDamageEvent.DamageModifier.BLOCKING, this::blocking);
        modifiers.put(EntityDamageEvent.DamageModifier.ARMOR, this::armor);
        modifiers.put(EntityDamageEvent.DamageModifier.RESISTANCE, this::resistance);
        modifiers.put(EntityDamageEvent.DamageModifier.MAGIC, this::magic);
        modifiers.put(EntityDamageEvent.DamageModifier.ABSORPTION, this::absorption);
    }

    public void compute() {
        var base = event.getDamage(EntityDamageEvent.DamageModifier.BASE);

        final var modifiers = Arrays
                .stream(EntityDamageEvent.DamageModifier.values())
                .filter(event::isApplicable)
                .filter(mod -> mod != EntityDamageEvent.DamageModifier.BASE)
                .map(mod -> new SfDamageModifier(mod, this.modifiers.get(mod), event.getDamage(mod)))
                .toList();

        // full replace damage system
        var msg = new StringBuilder();
        msg.append("%.2f".formatted(base));
        for (SfDamageModifier modifier : modifiers) {
            final var damage = modifier.function.apply(base);
            if(damage < -0.00001 || damage > 0.00001) {
                if(damage > 0) {
                    msg.append(" + ");
                    msg.append("%.2f".formatted(damage));
                } else {
                    msg.append(" - ");
                    msg.append("%.2f".formatted(-damage));
                }
                msg.append(modifier.mod.name().toLowerCase(), 0, 2);
            }
            event.setDamage(modifier.mod, damage);
        }
        msg.append(" = ");
        msg.append("%.2f".formatted(event.getFinalDamage()));
        msg.append(" ");
        msg.append(source.toString());
        msg.append(" ");
        msg.append(event.getClass().getSimpleName());
        Bukkit.broadcastMessage(msg.toString());
    }

    public abstract double hardHat(double f);

    public abstract double blocking(double f);

    public abstract double armor(double f);

    public abstract double resistance(double f);

    public abstract double magic(double f);

    public abstract double absorption(double f);

}
