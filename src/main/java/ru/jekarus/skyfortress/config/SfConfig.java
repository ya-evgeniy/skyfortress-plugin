package ru.jekarus.skyfortress.config;

import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.Vec3i;

public class SfConfig {

    public static Area3i LEAVE = Area3i.of(
            new Vec3i(498, 101, -2),
            new Vec3i(502, 103, 2)
    );
    public static Vec3i FORCE_START = new Vec3i(500, 101, 0);

}
