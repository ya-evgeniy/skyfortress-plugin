package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

public class SfLocationSerializer implements TypeSerializer<SfLocation> {

    @Override
    public SfLocation deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        Vector3d position = node.getNode("position").getValue(TypeToken.of(Vector3d.class));
        Vector3d rotation = node.getNode("rotation").getValue(TypeToken.of(Vector3d.class));

        Location<World> location = new Location<World>(SkyFortressPlugin.getInstance().getWorld(), position);
        rotation = rotation != null ? rotation : new Vector3d(0.0, 0.0, 0.0);

        return new SfLocation(location, rotation);
    }

    @Override
    public void serialize(TypeToken<?> type, SfLocation obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
