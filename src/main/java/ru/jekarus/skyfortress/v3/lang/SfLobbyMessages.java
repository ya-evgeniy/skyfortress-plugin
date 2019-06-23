package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.Locale;
import java.util.Map;

public class SfLobbyMessages {

    private final SkyFortressPlugin plugin;
    private final SfMessages messages;
    private final SfPlayers players;

    public SfLobbyMessages(SkyFortressPlugin plugin, SfMessages messages) {
        this.plugin = plugin;
        this.messages = messages;

        this.players = SfPlayers.getInstance();
    }

    private SfLanguage getLang(Player player) {
        return getLang(players.getOrCreatePlayer(player));
    }

    private SfLanguage getLang(PlayerData player) {
        return plugin.getLanguages().get(player.getLocale());
    }

    public Text cantJoin(PlayerData player) {
        return getLang(player).messages.lobby.cantJoin.toText();
    }

    public Text cantLeave(PlayerData player) {
        return getLang(player).messages.lobby.cantLeave.toText();
    }

    public Text cantReady(PlayerData player) {
        return getLang(player).messages.lobby.cantReady.toText();
    }

    public Text cantUnready(PlayerData player) {
        return getLang(player).messages.lobby.cantUnready.toText();
    }

    public Text cantAccept(PlayerData player) {
        return getLang(player).messages.lobby.cantAccept.toText();
    }

    public Text cantDeny(PlayerData player) {
        return getLang(player).messages.lobby.cantDeny.toText();
    }

    public Text cantJoinWhenDistribution(PlayerData player) {
        return getLang(player).messages.lobby.cantJoinWhenDistribution.toText();
    }

    public Text playerWaitAccepted(PlayerData player) {
        return getLang(player).messages.lobby.playerWaitAccepted.toText();
    }

    public Text playerWaitAcceptedByCaptain(PlayerData player, PlayerData captain, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.playerWaitAcceptedByCaptain,
                vars -> vars.playerKey().color(team).name(captain)
        );
    }

    public Text playerJoined(PlayerData player, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.playerJoined,
                vars -> vars.teamKey().name(team)
        );
    }

    public Text playerLeaved(PlayerData player, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.playerLeaved,
                vars -> vars.teamKey().name(team)
        );
    }

    public Text playerAcceptedBy(PlayerData player, PlayerData acceptor, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.playerAcceptedBy,
                vars -> vars.playerKey().color(team).name(acceptor)
        );
    }

    public Text playerDeniedBy(PlayerData player, PlayerData denier, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.playerDeniedBy,
                vars -> vars.playerKey().color(team).name(denier)
        );
    }

    public Map<Locale, Text> teammateWaitAccepted(PlayerData player) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateWaitAccepted,
                vars -> vars.playerKey().name(player)
        );
    }

    public Map<Locale, Text> teammateJoined(PlayerData player) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateJoined,
                vars -> vars.playerKey().name(player)
        );
    }

    public Map<Locale, Text> teammateLeaved(PlayerData player) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateLeaved,
                vars -> vars.playerKey().name(player)
        );
    }

    public Text teammateYouAccepted(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.teammateYouAccepted,
                vars -> vars.playerKey("target").color(team).name(target)
        );
    }

    public Map<Locale, Text> teammateAcceptedBy(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateAcceptedBy,
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("target").color(team).name(target)
        );
    }

    public Text teammateYouDenied(PlayerData player, PlayerData target) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.teammateYouDenied,
                vars -> vars.playerKey("target").name(target)
        );
    }

    public Map<Locale, Text> teammateDeniedBy(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateDeniedBy,
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("target").name(target)
        );
    }

    public Text teammateCaptainYouAreNew(PlayerData player, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.teammateCaptainYouAreNew,
                vars -> vars.teamKey().name(team)
        );
    }

    public Map<Locale, Text> teammateNewCaptain(PlayerData player, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateCaptainNew,
                vars -> vars.playerKey().color(team).name(player)
        );
    }

    public Text teammateCaptainYouReplaced(PlayerData player, PlayerData captain, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.teammateCaptainYouAreNew,
                vars -> vars.playerKey().color(team).name(captain)
        );
    }

    public Map<Locale, Text> teammateCaptainReplaced(PlayerData player, PlayerData captain, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateCaptainReplaced,
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("captain").color(team).name(captain)
        );
    }

    public Text teammateCaptainLeavedYouNew(PlayerData player, PlayerData captain, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.teammatesCaptainLeavedYouNew,
                vars -> vars.playerKey().color(team).name(captain)
        );
    }

    public Map<Locale, Text> teammateCaptainLeaved(PlayerData player, PlayerData captain, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.teammateCaptainLeaved,
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("captain").color(team).name(captain)
        );
    }

    public Text teammateCaptainCantReady(PlayerData player) {
        return getLang(player).messages.lobby.teammateCaptainCantReady.toText();
    }

    public Text teammateCaptainCantUnready(PlayerData player) {
        return getLang(player).messages.lobby.teammateCaptainCantUnready.toText();
    }

    public Text teammateCaptainCantAccept(PlayerData player) {
        return getLang(player).messages.lobby.teammateCaptainCantAccept.toText();
    }

    public Text teammateCaptainCantDeny(PlayerData player) {
        return getLang(player).messages.lobby.teammateCaptainCantDeny.toText();
    }

    public Text commandPlayerChangeSelfTeam(PlayerData player, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.commandPlayerChangeSelfTeam,
                vars -> vars.teamKey().name(team)
        );
    }

    public Text commandPlayerChangePlayerTeam(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.commandPlayerChangePlayerTeam,
                vars -> vars
                        .playerKey().name(target)
                        .teamKey().name(team)
        );
    }

    public Text commandPlayerYouAlreadyInTeam(PlayerData player) {
        return getLang(player).messages.lobby.commandPlayerYouAlreadyInTeam.toText();
    }

    public Text commandPlayerAlreadyInTeam(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.commandPlayerAlreadyInTeam,
                vars -> vars.playerKey().color(team).name(target)
        );
    }

    public Text commandTargetSetTeam(PlayerData player, PlayerData who, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.messages.lobby.commandTargetSetTeam,
                vars -> vars
                        .playerKey().name(who)
                        .teamKey().name(team)
        );
    }

    public Map<Locale, Text> commandGlobalSetSelfTeam(PlayerData player, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.commandGlobalSetSelfTeam,
                vars -> vars
                        .playerKey().color(team).name(player)
                        .teamKey().name(team)
        );
    }

    public Map<Locale, Text> commandGlobalSetPlayerTeam(PlayerData player, PlayerData target, SfTeam team) {
        return messages.construct(
                lang -> lang.messages.lobby.commandGlobalSetPlayerTeam,
                vars -> vars
                        .playerKey().name(player)
                        .playerKey("target").color(team).name(target)
                        .teamKey().name(team)
        );
    }

}
