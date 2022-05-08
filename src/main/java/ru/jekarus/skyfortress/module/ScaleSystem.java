package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.SkyFortress;
import ru.jekarus.skyfortress.config.SfTeam;

public class ScaleSystem implements Listener {

    private static ScaleSystem INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "ScaleSystem already registered";
        INS = new ScaleSystem(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "ScaleSystem is not registered";
        HandlerList.unregisterAll(INS);

        INS = null;
    }

    private final SkyFortress sf;

    public ScaleSystem(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
    }

    public double baseDamageModifier(double damage) {
        return damage * 0.4;
    }

    public double teamDamageModifier(Player player, double damage) {
        final var sft = SfTeam.get(player);
        if (sft != null) {
            final var state = sf.getTeamState(sft);
            damage += state.experience;
        }
        return damage;
    }

    private double teamDefenceModifier(Player player, double damage) {
        final var sft = SfTeam.get(player);
        if (sft != null) {
            final var state = sf.getTeamState(sft);
            damage -= state.experience;
        }
        return damage;
    }

    public double damagePlayerToMob(Player player, Entity entity, double damage) {
        double source = damage;
        damage = baseDamageModifier(damage);
        double scaled = damage;
        damage = teamDamageModifier(player, damage);
        System.out.printf("p2m %.2f -> base:%.2f -> team:%.2f%n", source, scaled, damage);
        return damage;
    }

    public double damagePlayerToPlayer(Player player, Player target, double damage) {
        double source = damage;
        damage = baseDamageModifier(damage);
        double scaled = damage;
        damage = teamDamageModifier(player, damage);
        double team1 = damage;
        damage = teamDefenceModifier(target, damage);
        System.out.printf("p2p %.2f -> base:%.2f -> team:%.2f -> final:%.2f%n", source, scaled, team1, damage);
        return damage;
    }

    public double damageOtherToPlayer(Player target, double damage) {
        double source = damage;
        damage = baseDamageModifier(damage);
        double team1 = damage;
        damage = teamDefenceModifier(target, damage);
        System.out.printf("o2p %.2f -> base:%.2f -> teamDef:%.2f%n", source, team1, damage);
        return damage;
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {  // scale damage
            if(event.getEntity().getType() == EntityType.PLAYER) {
                double damage = damagePlayerToPlayer((Player) event.getDamager(), (Player) event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
            } else {
                double damage = damagePlayerToMob((Player) event.getDamager(), event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
            }
        } else {
            if(event.getEntity().getType() == EntityType.PLAYER) {  // scale defence
                double damage = damageOtherToPlayer((Player) event.getEntity(), event.getDamage(EntityDamageEvent.DamageModifier.BASE));
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
            }
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) return;
        if(event.getEntity().getType() == EntityType.PLAYER) {  // scale defence
            double damage = damageOtherToPlayer((Player) event.getEntity(), event.getDamage());
            event.setDamage(damage);
        }
    }

}
