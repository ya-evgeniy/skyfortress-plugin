package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TypeTokens;
import ru.jekarus.skyfortress.v3.lang.messages.SfGameMessagesLanguage;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.ArrayList;
import java.util.Collection;

public class SfGameMessagesLanguageSerializer implements TypeSerializer<SfGameMessagesLanguage> {

    @Override
    public SfGameMessagesLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfGameMessagesLanguage language = new SfGameMessagesLanguage();

        ConfigurationNode castleNode = node.getNode("castle");
        language.castle_capture = SfTextParser.templates(castleNode.getNode("capture").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);
        language.castle_captured = SfTextParser.templates(castleNode.getNode("captured").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);

        Collection<String> strings = new ArrayList<>();

        ConfigurationNode teamNode = node.getNode("team");
        language.team_lost = SfTextParser.templates(teamNode.getNode("lost").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);
        language.team_win = SfTextParser.templates(teamNode.getNode("win").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfGameMessagesLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
