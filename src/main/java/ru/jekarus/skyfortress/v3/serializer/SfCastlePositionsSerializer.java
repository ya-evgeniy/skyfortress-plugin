package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.Collection;

public class SfCastlePositionsSerializer implements TypeSerializer<SfCastlePositions> {

    @Override
    @SuppressWarnings("unchecked")
    public SfCastlePositions deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfLocation respawn = node.getNode("respawn").getValue(TypeToken.of(SfLocation.class));
        SfLocation capture = node.getNode("capture").getValue(TypeToken.of(SfLocation.class));

        Collection<SfLocation> shops = new ArrayList<>();
        for (ConfigurationNode locationNode : node.getNode("shops").getChildrenList())
        {
            SfLocation shop = locationNode.getValue(TypeToken.of(SfLocation.class));
            shops.add(shop);
        }

        return new SfCastlePositions(
                respawn, capture, shops
        );
    }

    @Override
    public void serialize(TypeToken<?> type, SfCastlePositions obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
