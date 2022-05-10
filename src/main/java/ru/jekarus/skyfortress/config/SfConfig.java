package ru.jekarus.skyfortress.config;

import org.bukkit.Location;
import org.bukkit.World;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.Vec3i;

public class SfConfig {

    public static Area3i LEAVE = Area3i.of(
            new Vec3i(498, 101, -2),
            new Vec3i(502, 103, 2)
    );
    public static Vec3i FORCE_START = new Vec3i(500, 101, 0);

    public record ObjectLoc(Vec3i loc, Vec3i dir) {

        public Location toLocation(World world) {
            return loc.toLocation(world).setDirection(dir.toVector().normalize());
        }
    }

    public static ObjectLoc[] DRAGONS = new ObjectLoc[] {
            new ObjectLoc(new Vec3i(2570, 96, -70), new Vec3i(-1, 0, 1)),
            new ObjectLoc(new Vec3i(2430, 96, 70), new Vec3i(1, 0, -1))
    };

    public static ObjectLoc[] WITHERS = new ObjectLoc[] {
            new ObjectLoc(new Vec3i(2430, 96, -70), new Vec3i(1, 0, 1)),
            new ObjectLoc(new Vec3i(2570, 96, 70), new Vec3i(-1, 0, -1))
    };

    public static final double OBJECT_HOME_RADIUS = 13;

}
