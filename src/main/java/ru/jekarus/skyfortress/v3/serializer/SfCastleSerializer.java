package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;

public class SfCastleSerializer implements TypeSerializer<SfCastle> {

    @Override
    public SfCastle deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        String uniqueId = node.getNode("unique_id").getString();
        String teamId = node.getNode("team_id").getString();
        SfCastlePositions positions = node.getNode("positions").getValue(TypeToken.of(SfCastlePositions.class));

        if (uniqueId == null || teamId == null || positions == null)
        {
            throw new NullPointerException("uniqueId == null || teamId == null || positions == null");
        }

        return new SfCastle(
                uniqueId, teamId, positions
        );
    }

    @Override
    public void serialize(TypeToken<?> typeToken, SfCastle obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
