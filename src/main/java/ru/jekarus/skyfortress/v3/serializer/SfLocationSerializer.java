package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

public class SfLocationSerializer implements TypeSerializer<LocationAndRotation> {

    @Override
    public LocationAndRotation deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        Vector3d position = node.getNode("location").getValue(TypeToken.of(Vector3d.class));
        Vector3d rotation = node.getNode("rotation").getValue(TypeToken.of(Vector3d.class), Vector3d.ZERO);

        Location<World> location = new Location<>(SkyFortressPlugin.getInstance().getWorld(), position);

        return new LocationAndRotation(location, rotation);
    }

    @Override
    public void serialize(TypeToken<?> type, LocationAndRotation obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
