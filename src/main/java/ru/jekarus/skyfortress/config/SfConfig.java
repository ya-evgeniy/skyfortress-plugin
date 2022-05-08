package ru.jekarus.skyfortress.config;

import ru.jekarus.skyfortress.Area2i;
import ru.jekarus.skyfortress.Vec3i;

public class SfConfig {

    public static Area2i LEAVE = new Area2i(
            new Vec3i(199, 100, -1),
            new Vec3i(201, 101, 1)
    );
    public static Vec3i FORCE_START = new Vec3i(200, 100, 0);

}
