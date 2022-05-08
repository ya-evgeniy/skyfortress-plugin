package ru.jekarus.skyfortress.module.fight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

    private final SkyFortress sf;
    private final Map<UUID, ru.jekarus.skyfortress.module.fight.FightRecord> fightsRecord = new HashMap<>();

    public FightSystem(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        final var fightRecord = fightsRecord.get(event.getEntity().getUniqueId());
        if (fightRecord == null || !fightRecord.isActual()) return;

        final var killerRecord = fightRecord.getKillerRecord();
        if (killerRecord.getEntity() instanceof Player killer) {
            final var sfpState = sf.getPlayerState(killer);
            sfpState.kills++;

            final var sftState = sf.getTeamState(sfpState.team);
            sftState.experience += 2;
        }

        fightRecord.assistants()
                .forEach(player -> {
                    final var sfpState = sf.getPlayerState(player);
                    if (sfpState.team != null) {
                        sfpState.assists++;
                        final var sftState = sf.getTeamState(sfpState.team);
                        sftState.experience += 0.5;
                    }
                });

        if (event.getEntity() instanceof Player player) {
            final var sfpState = sf.getPlayerState(player);
            sfpState.deaths++;
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        var fightRecord = fightsRecord.computeIfAbsent(event.getEntity().getUniqueId(), k -> new FightRecord());
        if (!fightRecord.isActual()) {
            // save fight to history
            fightRecord = new FightRecord();
            fightsRecord.put(event.getEntity().getUniqueId(), fightRecord);
        }

        if (event.getDamager() instanceof Projectile projectile) {
            final var shooter = projectile.getShooter();
            if (shooter instanceof Entity entityShooter) {
                fightRecord.recordDamage(new DamageRecord(
                        System.currentTimeMillis(),
                        entityShooter.getType(), entityShooter.getUniqueId(),
                        event.getCause(), event.getDamager().getType(),
                        event.getFinalDamage()
                ));
                return;
            }

            fightRecord.recordDamage(new DamageRecord(
                    System.currentTimeMillis(),
                    event.getDamager().getType(), event.getDamager().getUniqueId(),
                    event.getCause(), event.getDamager().getType(),
                    event.getFinalDamage()
            ));
            return;
        }

        fightRecord.recordDamage(new DamageRecord(
                System.currentTimeMillis(),
                event.getDamager().getType(), event.getDamager().getUniqueId(),
                event.getCause(), event.getDamager().getType(),
                event.getFinalDamage()
        ));
    }

}
