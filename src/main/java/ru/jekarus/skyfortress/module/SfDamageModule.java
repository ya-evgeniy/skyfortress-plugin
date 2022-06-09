package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.module.damage.*;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfDamageModule implements Listener {

    private static SfDamageModule INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "ScaleSystem already registered";
        INS = new SfDamageModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "ScaleSystem is not registered";
        HandlerList.unregisterAll(INS);

        INS = null;
    }

    private final SkyFortress sf;

    public SfDamageModule(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
    }

    public SfDamageProcessor impl(EntityDamageEvent event, SfDamageSource source, SfDamageTarget target) {
        return new SfDamageImpl(sf, event, source, target);
//        return new VanillaDamageReimplementation(event, source, target);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        final var target = new SfDamageTarget(event.getEntity());
        if (event.getDamager() instanceof Projectile projectile) {
            final var shooter = projectile.getShooter();
            final var source = new SfDamageSource(event.getCause(), (Entity) shooter, projectile);
            impl(event, source, target).compute();
        } else {
            final var source = new SfDamageSource(event.getCause(), event.getDamager());
            impl(event, source, target).compute();
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) return;
        final var source = new SfDamageSource(event.getCause());
        final var target = new SfDamageTarget(event.getEntity());
        impl(event, source, target).compute();
    }

//    public double baseDamageModifier(double damage) {
//        return damage * 0.4;
//    }
//
//    public double teamDamageModifier(Player player, double damage) {
//        final var sft = SfTeam.get(player);
//        if (sft != null) {
//            final var state = sf.getTeamState(sft);
//            damage += Math.pow(state.getLevel() / 32.0, 1.6) * 40.0;
//        }
//        return damage;
//    }
//
//    private double teamDefenceModifier(Player player, double damage) {
//        final var sft = SfTeam.get(player);
//        if (sft != null) {
//            final var state = sf.getTeamState(sft);
//            damage *= 1 - Math.pow(state.getLevel() / 32.0, 1.6);
//        }
//        return damage;
//    }
//
//    public double damagePlayerToMob(Player player, Entity entity, double damage) {
//        double source = damage;
//        damage = baseDamageModifier(damage);
//        double scaled = damage;
//        damage = teamDamageModifier(player, damage);
//        Bukkit.broadcastMessage("p2m %.2f -> base:%.2f -> team:%.2f".formatted(source, scaled, damage));
//        return damage;
//    }
//
//    public double damagePlayerToPlayer(Player player, Player target, double damage) {
//        double source = damage;
//        damage = baseDamageModifier(damage);
//        double scaled = damage;
//        damage = teamDamageModifier(player, damage);
//        double team1 = damage;
//        damage = teamDefenceModifier(target, damage);
//        Bukkit.broadcastMessage("p2p %.2f -> base:%.2f -> team:%.2f -> final:%.2f".formatted(source, scaled, team1, damage));
//        return damage;
//    }
//
//    public double damageOtherToPlayer(Player target, double damage) {
//        double source = damage;
//        damage = baseDamageModifier(damage);
//        double team1 = damage;
//        damage = teamDefenceModifier(target, damage);
//        Bukkit.broadcastMessage("o2p %.2f -> base:%.2f -> teamDef:%.2f".formatted(source, team1, damage));
//        return damage;
//    }
//
//    @EventHandler
//    public void on(EntityDamageByEntityEvent event) {
//        if (event.getDamager().getType() == EntityType.PLAYER) {  // scale damage
//            if(event.getEntity().getType() == EntityType.PLAYER) {
//                double damage = damagePlayerToPlayer((Player) event.getDamager(), (Player) event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
//            } else {
//                double damage = damagePlayerToMob((Player) event.getDamager(), event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
//            }
//        } else {
//            if(event.getEntity().getType() == EntityType.PLAYER) {  // scale defence
//                double damage = damageOtherToPlayer((Player) event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
//            }
//        }
//    }
//
//    @EventHandler
//    public void on(EntityDamageEvent event) {
//        if(event.getEntity().getType() == EntityType.PLAYER) {  // scale defence
//            double damage = damageOtherToPlayer((Player) event.getEntity(), event.getDamage());
//            event.setDamage(damage);
//        }
//    }

}
