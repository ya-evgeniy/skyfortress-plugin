package ru.jekarus.skyfortress.v3.serializer.language.scoreboard;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfInGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfInGameScoreboardLanguageSerializer implements TypeSerializer<SfInGameScoreboardLanguage> {

    @Override
    public SfInGameScoreboardLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfInGameScoreboardLanguage language = new SfInGameScoreboardLanguage();

        language.alive = SfTextParser.parse(node.getNode("alive").getString(), TextColors.GRAY);
        language.death = SfTextParser.parse(node.getNode("death").getString(), TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH);

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfInGameScoreboardLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
