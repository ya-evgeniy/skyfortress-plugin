package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import lombok.Getter;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class LocationAndRotation {

    @MethodConverter(inClass = LocationAndRotation.class, method = "createLocation") @ConfigPath("position")
    @Getter private Location<World> location;

    @OptionalValue @MethodConverter(inClass = LocationAndRotation.class, method = "vector3d")
    @Getter private Vector3d rotation = Vector3d.ZERO;

    public LocationAndRotation() {
    }

    public LocationAndRotation(Location<World> location, Vector3d rotation) {
        this.location = location;
        this.rotation = rotation;
    }

    public static Location<World> createLocation(@ConfigPath("x") double x, @ConfigPath("y") double y, @ConfigPath("z") double z) {
        return new Location<>(SkyFortressPlugin.getInstance().getWorld(), new Vector3d(x, y, z));
    }

    private static Vector3d vector3d(@OptionalValue @ConfigPath("x") double x, @OptionalValue @ConfigPath("y") double y, @OptionalValue @ConfigPath("z") double z) {
        return new Vector3d(x, y, z);
    }

}
