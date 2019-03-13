package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TypeTokens;
import ru.jekarus.skyfortress.v3.lang.messages.SfGameMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfTitleMessagesLanguage;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.templates;

public class SfGameMessagesLanguageSerializer implements TypeSerializer<SfGameMessagesLanguage> {

    @Override
    public SfGameMessagesLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfGameMessagesLanguage lang = new SfGameMessagesLanguage();

        ConfigurationNode castleNode = node.getNode("castle");
        lang.castleCapture = templates(castleNode.getNode("capture").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);
        lang.castleCaptured = templates(castleNode.getNode("captured").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);

        ConfigurationNode castleForTeamNode = castleNode.getNode("for_team");
        lang.castleForTeamCapturing = parse(castleForTeamNode.getNode("you_capturing").getString());
        lang.castleForTeamCaptured = castleForTeamNode.getNode("you_captured").getList(TypeToken.of(SfTitleMessagesLanguage.class));

        ConfigurationNode teamNode = node.getNode("team");
        lang.teamLost = templates(teamNode.getNode("lost").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);
        lang.teamWin = templates(teamNode.getNode("win").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);

        return lang;
    }

    @Override
    public void serialize(TypeToken<?> type, SfGameMessagesLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
