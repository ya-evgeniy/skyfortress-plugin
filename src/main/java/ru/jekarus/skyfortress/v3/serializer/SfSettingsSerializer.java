package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.SfSettings;

public class SfSettingsSerializer implements TypeSerializer<SfSettings> {

    @Override
    public SfSettings deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfSettings settings = new SfSettings();

        settings.player_offline_death = node.getNode("player_offline_death").getInt(settings.player_offline_death);
        settings.castle_health = node.getNode("castle_health").getInt(settings.castle_health);
        settings.castle_death_seconds = node.getNode("castle_death_seconds").getInt(settings.castle_death_seconds);

        return settings;
    }

    @Override
    public void serialize(TypeToken<?> type, SfSettings obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
