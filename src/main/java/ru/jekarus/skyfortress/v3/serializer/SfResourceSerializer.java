package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.resource.SfResource;

import java.util.ArrayList;
import java.util.Collection;

public class SfResourceSerializer implements TypeSerializer<SfResource> {

    @Override
    @SuppressWarnings("unchecked")
    public SfResource deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        String uniqueId = node.getNode("unique_id").getString();
        ItemStack item = node.getNode("item").getValue(TypeToken.of(ItemStack.class));
        int ticks = node.getNode("ticks").getInt();

        Collection<Location<World>> positions = new ArrayList<>();
        for (ConfigurationNode locationNode : node.getNode("positions").getChildrenList())
        {
            Vector3d shop = locationNode.getValue(TypeToken.of(Vector3d.class));
            positions.add(new Location<>(SkyFortressPlugin.getInstance().getWorld(), shop));
        }

        if (uniqueId == null || ticks < 1 || item == null || positions.isEmpty())
        {
            throw new UnsupportedOperationException("uniqueId == null || ticks < 1 || item == null || positions.isEmpty()");
        }

        return new SfResource(
                uniqueId, item, ticks, positions
        );
    }

    @Override
    public void serialize(TypeToken<?> type, SfResource obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
