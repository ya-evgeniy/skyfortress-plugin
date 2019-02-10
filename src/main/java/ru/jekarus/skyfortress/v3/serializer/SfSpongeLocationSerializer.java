package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class SfSpongeLocationSerializer implements TypeSerializer<Location> {

    @Override
    public Location deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        double x = node.getNode("x").getDouble();
        double y = node.getNode("y").getDouble();
        double z = node.getNode("z").getDouble();

        return new Location<>(SkyFortressPlugin.getInstance().getWorld(), x, y, z);
    }

    @Override
    public void serialize(TypeToken<?> type, Location obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
