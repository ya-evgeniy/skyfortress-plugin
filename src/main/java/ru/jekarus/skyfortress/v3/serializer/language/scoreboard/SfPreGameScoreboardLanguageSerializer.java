package ru.jekarus.skyfortress.v3.serializer.language.scoreboard;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPreGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfPreGameScoreboardLanguageSerializer implements TypeSerializer<SfPreGameScoreboardLanguage> {

    @Override
    public SfPreGameScoreboardLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfPreGameScoreboardLanguage language = new SfPreGameScoreboardLanguage();

        language.creators = SfTextParser.parse(node.getNode("creators").getString(), TextColors.GOLD);
        language.creator = SfTextParser.parse(node.getNode("creator").getString(), TextColors.GRAY);
        language.teams = SfTextParser.parse(node.getNode("teams").getString(), TextColors.GOLD);
        language.team = SfTextParser.parse(node.getNode("team").getString(), TextColors.GRAY);

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfPreGameScoreboardLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
