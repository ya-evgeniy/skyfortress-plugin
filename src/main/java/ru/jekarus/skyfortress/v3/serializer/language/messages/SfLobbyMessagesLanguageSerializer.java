package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.lang.messages.SfLobbyMessagesLanguage;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SfLobbyMessagesLanguageSerializer implements TypeSerializer<SfLobbyMessagesLanguage> {

    @Override
    public SfLobbyMessagesLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfLobbyMessagesLanguage lang = new SfLobbyMessagesLanguage();

        ConfigurationNode cantNode = node.getNode("cant");
        lang.cantJoin = parseText(cantNode, "join");
        lang.cantLeave = parseText(cantNode, "leave");
        lang.cantReady = parseText(cantNode, "ready");
        lang.cantUnready = parseText(cantNode, "unready");
        lang.cantAccept = parseText(cantNode, "accept");
        lang.cantDeny = parseText(cantNode, "deny");

        ConfigurationNode playerNode = node.getNode("player");
        lang.playerWaitAccepted = parseText(playerNode, "wait_accepted");
        lang.playerWaitAcceptedByCaptain = parseText(playerNode, "wait_accepted_by_captain");
        lang.playerJoined = parseText(playerNode, "joined");
        lang.playerLeaved = parseText(playerNode, "leaved");
        lang.playerAcceptedBy = parseText(playerNode, "accepted_by");
        lang.playerDeniedBy = parseText(playerNode, "denied_by");

        ConfigurationNode teammateNode = node.getNode("teammate");
        lang.teammateCaptainYouAreNew = parseText(teammateNode, "wait_accepted");
        lang.teammateJoined = parseText(teammateNode, "joined");
        lang.teammateLeaved = parseText(teammateNode, "leaved");
        lang.teammateYouAccepted = parseText(teammateNode, "you_accepted");
        lang.teammateAcceptedBy = parseText(teammateNode, "accepted_by");
        lang.teammateYouDenied = parseText(teammateNode, "you_denied");
        lang.teammateDeniedBy = parseText(teammateNode, "denied_by");

        ConfigurationNode teammatesCaptainNode = teammateNode.getNode("captain");
        lang.teammateCaptainYouAreNew = parseText(teammatesCaptainNode, "wait_accepted");
//        lang.teammates_captain_accepted_by = parseText(teammatesCaptainNode, "accepted_by");
//        lang.teammates_captain_denied_by = parseText(teammatesCaptainNode, "denied_by");
        lang.teammateCaptainYouAreNew = parseText(teammatesCaptainNode, "you_are_new");
        lang.teammateCaptainNew = parseText(teammatesCaptainNode, "are_new");
        lang.teammateCaptainYouReplaced = parseText(teammatesCaptainNode, "you_replaced");
        lang.teammateCaptainReplaced = parseText(teammatesCaptainNode, "replaced");
        lang.teammateCaptainLeaved = parseText(teammatesCaptainNode, "leaved");
        lang.teammatesCaptainLeavedYouNew = parseText(teammatesCaptainNode, "leaved_you_new");

        ConfigurationNode teammatesCaptainCantNode = teammatesCaptainNode.getNode("cant");
        lang.teammateCaptainCantReady = parseText(teammatesCaptainCantNode, "ready");
        lang.teammateCaptainCantUnready = parseText(teammatesCaptainCantNode, "unready");
        lang.teammateCaptainCantAccept = parseText(teammatesCaptainCantNode, "accept");
        lang.teammateCaptainCantDeny = parseText(teammatesCaptainCantNode, "deny");

        ConfigurationNode commandNode = node.getNode("command");
        ConfigurationNode commandPlayerNode = commandNode.getNode("player");
        lang.commandPlayerChangeSelfTeam = parseText(commandPlayerNode, "change_self_team");
        lang.commandPlayerChangePlayerTeam = parseText(commandPlayerNode, "change_player_team");
        lang.commandPlayerYouAlreadyInTeam = parseText(commandPlayerNode, "you_already_in_team");
        lang.commandPlayerAlreadyInTeam = parseText(commandPlayerNode,"already_in_team");

        ConfigurationNode commandTargetNode = commandNode.getNode("target");
        lang.commandTargetSetTeam = parseText(commandTargetNode, "set_team");

        ConfigurationNode commandGlobalNode = commandNode.getNode("global");
        lang.commandGlobalSetSelfTeam = parseText(commandGlobalNode, "set_self_team");
        lang.commandGlobalSetPlayerTeam = parseText(commandGlobalNode, "set_player_team");

        return lang;
    }

    @Override
    public void serialize(TypeToken<?> type, SfLobbyMessagesLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

    private TextTemplate parseText(ConfigurationNode node, String subNodeName) {
        ConfigurationNode subNode = node.getNode(subNodeName);
        String string = subNode.getString();
        if (string == null) return TextTemplate.of(buildPath(subNode.getPath()));
        return SfTextParser.parse(string, TextColors.GRAY);
    }

    private String buildPath(Object[] path) {
        StringBuilder builder = new StringBuilder();
        List<Object> objects = Arrays.asList(path);
        Iterator<Object> iterator = objects.iterator();
        if (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof String) {
                builder.append(obj);
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                if (obj instanceof String) {
                    builder.append(".").append(obj);
                }
            }
        }
        return builder.toString();
    }

}
