package ru.jekarus.skyfortress.config;

import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.Vec3i;

public class SfConfig {

    public static Area3i LEAVE = new Area3i(
            new Vec3i(199, 100, -1),
            new Vec3i(201, 101, 1)
    );
    public static Vec3i FORCE_START = new Vec3i(200, 100, 0);

}
