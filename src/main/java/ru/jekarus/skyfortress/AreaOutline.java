package ru.jekarus.skyfortress;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

public class AreaOutline {

    private static Plugin plugin;

    public static void register(Plugin plugin) {
        AreaOutline.plugin = plugin;
    }
    public static void unregister() {
        AreaOutline.plugin = null;
    }
    record Vec2f(float x, float y) {}

    private static final Vec2f[] SIDES = new Vec2f[] {
            new Vec2f(0, 0),
            new Vec2f(1, 0),
            new Vec2f(0, 1),
            new Vec2f(1, 1),
    };

    private static void show(
            Player player, Area2i a,
            ParticleEffect effect,
            Vector off, float speed, int amount,
            ParticleData data
    ) {
        final var world = player.getWorld();
        final var size = a.max().sub(a.min()).add(1, 1, 1);
        for (int x = 1; x < size.x() * 10; x++) {
            for(Vec2f side : SIDES) {
                effect.display(new Location(
                        world,
                        a.min().x() + 0.1 * x,
                        a.min().y() + side.x * size.y(),
                        a.min().z() + side.y * size.z()
                ), off, speed, amount, data, player);
            }
        }
        for (int y = 1; y < size.y() * 10; y++) {
            for(Vec2f side : SIDES) {
                effect.display(new Location(
                        world,
                        a.min().x() + side.x * size.x(),
                        a.min().y() + 0.1 * y,
                        a.min().z() + side.y * size.z()
                ), off, speed, amount, data, player);
            }
        }
        for (int z = 1; z < size.z() * 10; z++) {
            for(Vec2f side : SIDES) {
                effect.display(new Location(
                        world,
                        a.min().x() + side.x * size.x(),
                        a.min().y() + side.y * size.y(),
                        a.min().z() + 0.1 * z
                ), off, speed, amount, data, player);
            }
        }
    }

    public static void show(Player player, Vec3i v) {
        show(player, new Area2i(v));
    }
    public static void show(Player player, Area2i a) {
        if(plugin == null) return;
        Runnable spawn = () -> show(
                player, a,
                ParticleEffect.REVERSE_PORTAL,
//                ParticleEffect.WAX_OFF,
//                ParticleEffect.REDSTONE,
//                ParticleEffect.ASH,
//                ParticleEffect.DOLPHIN,
                new Vector(
                        0f, 0f, 0f
                ),
                0f,
                0,
                null
//                new DustData(java.awt.Color.GRAY, 0.7f)
        );
        spawn.run();
        plugin.getServer().getScheduler().runTaskLater(plugin, spawn, 10);
        plugin.getServer().getScheduler().runTaskLater(plugin, spawn, 20);
        plugin.getServer().getScheduler().runTaskLater(plugin, spawn, 30);
    }

}
