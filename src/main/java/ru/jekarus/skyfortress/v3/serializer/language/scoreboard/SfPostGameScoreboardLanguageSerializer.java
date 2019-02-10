package ru.jekarus.skyfortress.v3.serializer.language.scoreboard;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPostGameScoreboardLanguage;

public class SfPostGameScoreboardLanguageSerializer implements TypeSerializer<SfPostGameScoreboardLanguage> {

    @Override
    public SfPostGameScoreboardLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfPostGameScoreboardLanguage language = new SfPostGameScoreboardLanguage();



        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfPostGameScoreboardLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
