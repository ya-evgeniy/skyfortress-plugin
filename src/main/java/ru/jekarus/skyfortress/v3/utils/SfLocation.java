package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SfLocation {

    private final Location<World> position;
    private final Vector3d rotation;

    public SfLocation(Location<World> position, Vector3d rotation)
    {
        this.position = position;
        this.rotation = rotation;
    }

    public Location<World> getLocation()
    {
        return this.position;
    }

    public Vector3d getRotation()
    {
        return this.rotation;
    }

}
