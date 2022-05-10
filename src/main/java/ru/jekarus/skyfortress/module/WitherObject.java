package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.object.SfDragon;
import ru.jekarus.skyfortress.module.object.WitherNms;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WitherObject implements Listener {

    private static WitherObject INS;

    public static void register(Plugin plugin) {
        assert INS == null : "WitherObject already registered";
        INS = new WitherObject(plugin);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "WitherObject is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public static class SfWither {

        public final Wither entity;
        public final Location location;

        public SfWither(Wither entity, Location location) {
            this.entity = entity;
            this.location = location;
            entity.setSilent(true);
            entity.getPathfinder().stopPathfinding();
            Bukkit.getMobGoals().removeAllGoals(entity);
            WitherNms.setGravity(entity, false);
        }

    }

    private final Map<UUID, SfWither> withers = new HashMap<>();

    public WitherObject(Plugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfWither wither : withers.values()) {
                final var players = wither.location.getNearbyEntitiesByType(Player.class, SfConfig.OBJECT_HOME_RADIUS);

            }
        }, 20, 20);
    }

    @EventHandler
    public void on(GameStartEvent event) {
        final var world = Bukkit.getServer().getWorlds().get(0);
        for(SfConfig.ObjectLoc objectLoc : SfConfig.WITHERS) {
            final var location = objectLoc.toLocation(world);
            world.spawnEntity(location, EntityType.WITHER, CreatureSpawnEvent.SpawnReason.DEFAULT, entity -> {
                withers.put(entity.getUniqueId(), new SfWither((Wither) entity, location));
            });
        }
    }

    @EventHandler
    public void on(ExplosionPrimeEvent event) {
        final var sfw = withers.get(event.getEntity().getUniqueId());
        if (sfw == null) return;

    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    private void cleanup() {
        for (SfWither wither : withers.values()) {
            wither.entity.remove();
        }
        withers.clear();
    }

}
