package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.lang.messages.SfLobbyMessagesLanguage;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfLobbyMessagesLanguageSerializer implements TypeSerializer<SfLobbyMessagesLanguage> {

    @Override
    public SfLobbyMessagesLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfLobbyMessagesLanguage language = new SfLobbyMessagesLanguage();

        ConfigurationNode playerNode = node.getNode("player");
        language.player_join = SfTextParser.parse(playerNode.getNode("join").getString(), TextColors.GRAY);
        language.player_joined = SfTextParser.parse(playerNode.getNode("joined").getString(), TextColors.GRAY);
        language.player_leave = SfTextParser.parse(playerNode.getNode("leave").getString(), TextColors.GRAY);
        language.player_accept = SfTextParser.parse(playerNode.getNode("accept").getString(), TextColors.GRAY);
        language.player_deny = SfTextParser.parse(playerNode.getNode("deny").getString(), TextColors.GRAY);
        language.player_cant_join = SfTextParser.parse(playerNode.getNode("cant", "join").getString(), TextColors.GRAY);
        language.player_cant_leave = SfTextParser.parse(playerNode.getNode("cant", "leave").getString(), TextColors.GRAY);

        ConfigurationNode teammateNode = node.getNode("teammate");
        language.teammate_join = SfTextParser.parse(teammateNode.getNode("join").getString(), TextColors.GRAY);
        language.teammate_joined = SfTextParser.parse(teammateNode.getNode("joined").getString(), TextColors.GRAY);
        language.teammate_leave = SfTextParser.parse(teammateNode.getNode("leave").getString(), TextColors.GRAY);
        language.teammate_accept = SfTextParser.parse(teammateNode.getNode("accept").getString(), TextColors.GRAY);
        language.teammate_deny = SfTextParser.parse(teammateNode.getNode("deny").getString(), TextColors.GRAY);

        ConfigurationNode commandNode = node.getNode("command");
        ConfigurationNode commandPlayerNode = commandNode.getNode("player");
        language.command_player_set_self_team = SfTextParser.parse(commandPlayerNode.getNode("set_self_team").getString(), TextColors.GRAY);
        language.command_player_set_player_team = SfTextParser.parse(commandPlayerNode.getNode("set_player_team").getString(), TextColors.GRAY);
        language.command_player_you_already_in_team = SfTextParser.parse(commandPlayerNode.getNode("you_already_in_team").getString(), TextColors.GRAY);
        language.command_player_target_already_in_team = SfTextParser.parse(commandPlayerNode.getNode("target_already_in_team").getString(), TextColors.GRAY);

        ConfigurationNode commandTargetNode = commandNode.getNode("player");
        language.command_target_set_team = SfTextParser.parse(commandTargetNode.getNode("set_team").getString(), TextColors.GRAY);

        ConfigurationNode commandGlobalNode = commandNode.getNode("player");
        language.command_global_set_self_team = SfTextParser.parse(commandGlobalNode.getNode("set_self_team").getString(), TextColors.GRAY);
        language.command_global_set_player_team = SfTextParser.parse(commandGlobalNode.getNode("set_player_team").getString(), TextColors.GRAY);

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfLobbyMessagesLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
