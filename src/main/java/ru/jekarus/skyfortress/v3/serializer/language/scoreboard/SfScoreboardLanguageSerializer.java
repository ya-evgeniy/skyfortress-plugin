package ru.jekarus.skyfortress.v3.serializer.language.scoreboard;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfInGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPostGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPreGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfScoreboardLanguage;

public class SfScoreboardLanguageSerializer implements TypeSerializer<SfScoreboardLanguage> {

    @Override
    public SfScoreboardLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfScoreboardLanguage language = new SfScoreboardLanguage();

        language.preGame = node.getNode("pre_game").getValue(TypeToken.of(SfPreGameScoreboardLanguage.class));
        language.inGame = node.getNode("in_game").getValue(TypeToken.of(SfInGameScoreboardLanguage.class));
        language.postGame = node.getNode("post_game").getValue(TypeToken.of(SfPostGameScoreboardLanguage.class));

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfScoreboardLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
