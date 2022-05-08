package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.SkyFortress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FightSystem implements Listener {

    private static FightSystem INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "FightSystem already registered";
        INS = new FightSystem(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "FightSystem is not registered";
        HandlerList.unregisterAll(INS);

        INS = null;
    }

    static class DamageRegistry {
        double meleeDamage = 0;
        double arrowDamage = 0;
        double potionDamage = 0;
        double explosionDamage = 0;
        double otherDamage = 0;
    }

    static class FightRecord {
        public UUID lastPlayerDamager = null;
        Map<UUID, DamageRegistry> damagers = new HashMap<>();
        long lastDamageTick = Bukkit.getCurrentTick();

        public boolean isActual() {  // 10 seconds passed. assuming start new fight
            return (lastDamageTick + 10 * 20) >= Bukkit.getCurrentTick();
        }

    }

    private final SkyFortress sf;
    private final Map<UUID, FightRecord> fights = new HashMap<>();

    public FightSystem(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        final var fight = fights.get(event.getEntity().getUniqueId());
        if (fight != null && fight.isActual()) {
            for (UUID uuid : fight.damagers.keySet()) {
                final var edamager = Bukkit.getEntity(uuid);
                if(edamager != null && edamager.getType() == EntityType.PLAYER) {
                    final var damager = (Player) edamager;
                    if(damager.getUniqueId().equals(fight.lastPlayerDamager)) {
                        // kill
                    } else {
                        // assist
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {  // scale damage
            Player player = (Player) event.getDamager();
            Entity target = event.getEntity();
            var fight = fights.computeIfAbsent(target.getUniqueId(), k -> new FightRecord());
            if(!fight.isActual()) {
                // todo: save fight to fight history
                fight = new FightRecord();
                fights.put(target.getUniqueId(), fight);
            }
            final var registry = fight.damagers.computeIfAbsent(player.getUniqueId(), k -> new DamageRegistry());
            switch (event.getCause()) {
                case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> {
                    registry.meleeDamage += event.getDamage();
                }
                case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> {
                    registry.explosionDamage += event.getDamage();
                }
                case POISON, MAGIC, WITHER -> {
                    registry.potionDamage += event.getDamage();
                }
                case PROJECTILE -> {
                    registry.arrowDamage += event.getDamage();
                }
                default -> {
                    registry.otherDamage += event.getDamage();
                }
            }
            fight.lastPlayerDamager = player.getUniqueId();
            fight.lastDamageTick = Bukkit.getCurrentTick();
        }
    }

}
