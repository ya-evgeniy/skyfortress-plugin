package ru.jekarus.skyfortress.module.object;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SfObjectModule<T extends SfObjectModule.SfEntity<? extends LivingEntity>> implements Listener {


    public static abstract class SfEntity<E extends LivingEntity> {


        public final E entity;
        public final Location location;
        public final Area3i homeArea;
        public final ObjectHealth health;

        public SfEntity(E entity, Location location) {
            this.entity = entity;
            this.location = location;

            final var l = new Vec3i(location);
            final var size = new Vec3i(SfConfig.OBJECT_HOME_RADIUS);
            this.homeArea = new Area3i(l.add(size), l.sub(size));
            this.health = new ObjectHealth(entity);
        }

        public void tickState() {}

        public void tickMoving() {}

        public void hitBy(EntityDamageByEntityEvent event, Player player) {

        }

        public abstract void cleanup();

    }


    protected final Plugin plugin;
    protected final Map<UUID, T> entities = new HashMap<>();

    public SfObjectModule(Plugin plugin) {
        this.plugin = plugin;
    }


    protected void initState() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (T obj : entities.values()) {
                obj.tickState();
            }
        }, 20, 20);
    }

    protected void initMoving() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (T obj : entities.values()) {
                obj.tickMoving();
            }
        }, 3, 3);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        final var sfw = entities.get(event.getEntity().getUniqueId());
        if (sfw == null) return;
        plugin.getServer().getScheduler().runTask(plugin, sfw.health::update);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        final var sfw = entities.get(event.getEntity().getUniqueId());
        if (sfw == null) return;
        var damager = event.getDamager();
        if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Entity entityShooter) damager = entityShooter;
        }
        if (damager.getType() != EntityType.PLAYER) return;
        Player player = (Player) damager;
        sfw.hitBy(event, player);
    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    protected void cleanup() {
        for (T obj : entities.values()) {
            obj.cleanup();
        }
        entities.clear();
    }

}
