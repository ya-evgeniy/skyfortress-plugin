package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.object.SfDragon;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.*;

public class DragonObject implements Listener {

    private static DragonObject INS;

    public static void register(Plugin plugin) {
        assert INS == null : "DragonObject already registered";
        INS = new DragonObject(plugin);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "DragonObject is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }


    private final Map<UUID, SfDragon> dragons = new HashMap<>();

    public DragonObject(Plugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfDragon dragon : dragons.values()) {
                if(dragon.state == SfDragon.State.GET_ON_POSITION) continue;
                final var players = dragon.location.getNearbyEntitiesByType(Player.class, SfConfig.OBJECT_HOME_RADIUS);
                if(players.size() > 0) {
                    if(dragon.state == SfDragon.State.ATTACK_TARGET) continue;
                    dragon.target = players.iterator().next();
                    dragon.setState(SfDragon.State.GET_ON_POSITION);
                } else {
                    dragon.setState(SfDragon.State.WANDERING);
                }
            }
        }, 20, 20);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfDragon sfd : dragons.values()) {
                if(sfd.state != SfDragon.State.ATTACK_TARGET) continue;
                final var loc = sfd.entity.getLocation().clone();
                final var yaw = loc.getYaw();  // [-180:180]
                final var pitch = loc.getPitch();  // [-90:90]
                final var delta = sfd.target.getLocation().add(0, 3, 0).subtract(loc).toVector();
                loc.setDirection(
                        delta
                        .multiply(-1)  // invert dragon look
                );
                var dyaw = Location.normalizeYaw(loc.getYaw()) - yaw;
                if(dyaw > 180) dyaw = 360 - dyaw;
                if(dyaw < -180) dyaw = 360 + dyaw;
                var dpitch = Location.normalizePitch(loc.getPitch()) - pitch;

                final var rotSpeed = sfd.entity.getHeadRotationSpeed();
                if(dyaw > rotSpeed) dyaw = rotSpeed;
                else if(dyaw < -rotSpeed) dyaw = -rotSpeed;
                if(dpitch > rotSpeed) dpitch = rotSpeed;
                else if(dpitch < rotSpeed) dpitch = -rotSpeed;
                sfd.entity.setRotation(yaw + dyaw, pitch + dpitch);
                if(Math.abs(dyaw) < 45) {
                    sfd.tryAttack();
                }

                double flySpeed = 0.3f;
                double dy = delta.getY();
                if(dy > flySpeed) dy = flySpeed;
                if(dy < -flySpeed) dy = -flySpeed;
                sfd.entity.teleport(sfd.entity.getLocation().clone().subtract(0, dy, 0));
            }
        }, 3, 3);
    }

    @EventHandler
    public void on(GameStartEvent event) {
        final var world = Bukkit.getServer().getWorlds().get(0);
        for(SfConfig.ObjectLoc objectLoc : SfConfig.DRAGONS) {
            final var location = objectLoc.toLocation(world);
            world.spawnEntity(location, EntityType.ENDER_DRAGON, CreatureSpawnEvent.SpawnReason.DEFAULT, entity -> {
                dragons.put(entity.getUniqueId(), new SfDragon((EnderDragon) entity, location));
            });
        }
    }

    @EventHandler
    public void on(EnderDragonChangePhaseEvent event) {
        final var sfd = dragons.get(event.getEntity().getUniqueId());
        if (sfd == null) return;
        if(event.getNewPhase() == EnderDragon.Phase.LEAVE_PORTAL) return;
        if(event.getNewPhase() == EnderDragon.Phase.BREATH_ATTACK) return;
        event.setNewPhase(EnderDragon.Phase.HOVER);
    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    private void cleanup() {
        for (SfDragon dragon : dragons.values()) {
            dragon.entity.remove();
        }
        dragons.clear();
    }


}
