package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfSpectatorTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

public class SfTeamSerializer implements TypeSerializer<SfTeam> {

    @Override
    public SfTeam deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        String type = node.getNode("type").getString("none");
        String uniqueId = node.getNode("unique_id").getString();
        String castleId = node.getNode("castle_id").getString();
        String colorStr = node.getNode("color").getString();

        if (uniqueId == null || colorStr == null)
        {
            throw new NullPointerException(String.format("uniqueId[%s] == null || colorStr[%s] == null", uniqueId, colorStr));
        }

        TextColor color = Sponge.getRegistry().getType(TextColor.class, colorStr).orElse(TextColors.WHITE);

        switch (type)
        {
            case "game":
                return new SfGameTeam(
                        uniqueId,
                        castleId,
                        color
                );
            case "spectator":
                return new SfSpectatorTeam(
                        uniqueId,
                        color
                );
            default:
                return new SfTeam(
                        uniqueId,
                        color
                );
        }

    }

    @Override
    public void serialize(TypeToken<?> typeToken, SfTeam obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
