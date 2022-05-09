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
import org.bukkit.util.Vector;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.DragonNms;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.*;
import java.util.function.Consumer;

public class DragonObject implements Listener {

    private static DragonObject INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "DragonObject already registered";
        INS = new DragonObject(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "DragonObject is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    enum State {
        WANDERING,
        GET_ON_POSITION,
        LOOK_AT_TARGET;
    }

    public static class SfDragon {
        public final EnderDragon entity;
        private final Location location;
        public State state = State.WANDERING;
        private Player target;


        public SfDragon(EnderDragon entity, Location location) {
            this.entity = entity;
            this.location = location;
            entity.setSilent(true);
            entity.getPathfinder().stopPathfinding();
            Bukkit.getMobGoals().removeAllGoals(entity);
            entity.setPodium(location);
            actState();
        }

        public void setState(State state) {
            if(state == this.state) return;
            System.out.println("state " + this.state + " -> " + state);
            this.state = state;
            actState();
        }

        private void actState() {
            switch (state) {
                case WANDERING -> {
                    DragonNms.moveTo(entity,
                            entity.getLocation().clone().add(entity.getLocation().getDirection().multiply(7)),
                            4, () -> {
                        DragonNms.moveTo(entity, location, 0, null);
                    });
                }
                case GET_ON_POSITION -> {
                    DragonNms.moveTo(entity, location, 4, () -> {
                        setState(State.LOOK_AT_TARGET);
                    });
                }
                case LOOK_AT_TARGET -> {
                    entity.setPhase(EnderDragon.Phase.HOVER);
                }
            }
        }
    }

    private final SkyFortress sf;
    private final Map<UUID, SfDragon> dragons = new HashMap<>();

    public DragonObject(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfDragon dragon : dragons.values()) {
                if(dragon.state == State.GET_ON_POSITION) continue;
                final var players = dragon.location.getNearbyEntitiesByType(Player.class, 10);
                if(players.size() > 0) {
                    if(dragon.state == State.LOOK_AT_TARGET) continue;
                    dragon.target = players.iterator().next();
                    dragon.setState(State.GET_ON_POSITION);
                } else {
                    dragon.setState(State.WANDERING);
                }
            }
        }, 20, 20);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfDragon sfd : dragons.values()) {
                if(sfd.state != State.LOOK_AT_TARGET) continue;
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
                double flySpeed = 0.3f;
                double dy = delta.getY();
                if(dy > flySpeed) dy = flySpeed;
                if(dy < -flySpeed) dy = -flySpeed;
                sfd.entity.teleport(sfd.entity.getLocation().clone().subtract(0, dy, 0));
            }
        }, 3, 3);
    }

    public void rotate(Location loc, double angle) {
//        final var dir = loc.getDirection();
//        loc.setDirection(new Vector(
//                Math.cos(Math.toRadians(angle)) * dir.getX(),
//                dir.getY(),
//                Math.sin(Math.toRadians(angle)) * dir.getZ()
//        ));
    }

    @EventHandler
    public void on(GameStartEvent event) {
        final var world = Bukkit.getServer().getWorlds().get(0);
        for(SfConfig.DragonLoc dragonLoc : SfConfig.DRAGONS) {
            final var location = dragonLoc.toLocation(world);
            world.spawnEntity(location, EntityType.ENDER_DRAGON, CreatureSpawnEvent.SpawnReason.COMMAND, entity -> {
                dragons.put(entity.getUniqueId(), new SfDragon((EnderDragon) entity, location));
            });
        }
    }

    @EventHandler
    public void on(EnderDragonChangePhaseEvent event) {
        final var sfd = dragons.get(event.getEntity().getUniqueId());
        if (sfd == null) return;
        System.out.println(event.getCurrentPhase() + " -> " + event.getNewPhase());
        if(event.getNewPhase() == EnderDragon.Phase.HOVER) return;
        event.setNewPhase(EnderDragon.Phase.LEAVE_PORTAL);
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
