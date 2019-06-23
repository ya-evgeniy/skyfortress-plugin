package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLobbyMessages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.Optional;

public class LobbyRoomMessages {

    private final LobbyRoom room;

    public LobbyRoomMessages(LobbyRoom room) {
        this.room = room;
    }

    public void sendCantJoin(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantJoin(playerData)
        );
    }

    public void sendCantLeave(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantLeave(playerData)
        );
    }

    public void sendCantJoinWhenDistribution(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantJoinWhenDistribution(playerData)
        );
    }

    public void sendJoined(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.playerJoined(playerData, room.getState().getTeam())
        );
    }

    public void sendPlayerLeaved(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.playerLeaved(playerData, room.getState().getTeam())
        );
    }

    public void sendTeammateLeave(Player player, PlayerData playerData, boolean isCaptain) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfMessages messages = plugin.getMessages();
        final SfLobbyMessages lobbyMessages = messages.getLobby();

        final LobbyRoomState state = room.getState();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        final SfGameTeam team = state.getTeam();
        player.sendMessage(
                lobbyMessages.playerLeaved(playerData, team)
        );

        if (plugin.getSettings().getGlobalLobby().isUseLobbyCaptainSystem() && isCaptain) {
            final ArrayList<PlayerData> players = new ArrayList<>(team.getPlayers());
            final PlayerData captainData = state.getCaptain();

            if (captainData == null) {
                return;
            }

            players.remove(captainData);

            messages.send(
                    players,
                    lobbyMessages.teammateCaptainLeaved(playerData, captainData, team)
            );
            final Optional<Player> optionalCaptain = captainData.getPlayer();
            optionalCaptain.ifPresent(captain -> captain.sendMessage(
                    lobbyMessages.teammateCaptainLeavedYouNew(captainData, playerData, team)
            ));
            return;
        }

        messages.send(
                team.getPlayers(),
                lobbyMessages.teammateLeaved(playerData)
        );
    }

    public void sendWaitAccepted(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfMessages messages = plugin.getMessages();
        final SfLobbyMessages lobbyMessages = messages.getLobby();

        final LobbyRoomState state = room.getState();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        messages.send(
                state.getTeam().getPlayers(),
                lobbyMessages.teammateWaitAccepted(playerData)
        );
        if (plugin.getSettings().getGlobalLobby().isUseLobbyCaptainSystem()) {
            player.sendMessage(
                    lobbyMessages.playerWaitAcceptedByCaptain(playerData, state.getCaptain(), state.getTeam())
            );
        }
        else {
            player.sendMessage(
                    lobbyMessages.playerWaitAccepted(playerData)
            );
        }
    }

    public void sendCantAccept(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantAccept(playerData)
        );
    }

    public void sendCantDeny(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantDeny(playerData)
        );
    }

    public void sendCantAcceptCaptain(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.teammateCaptainCantAccept(playerData)
        );
    }

    public void sendCantDenyCaptain(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.teammateCaptainCantDeny(playerData)
        );
    }

    public void sendAccepted(Player player, PlayerData playerData, Player target, PlayerData targetData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfMessages messages = plugin.getMessages();
        final SfLobbyMessages lobbyMessages = messages.getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        if (target == null) {
            target = targetData.getPlayer().orElse(null);
            if (target == null) return;
        }

        final LobbyRoomState state = room.getState();
        final SfGameTeam team = state.getTeam();

        final ArrayList<PlayerData> players = new ArrayList<>(team.getPlayers());
        players.remove(playerData);

        messages.send(
                players,
                lobbyMessages.teammateAcceptedBy(playerData, targetData, team)
        );
        player.sendMessage(
                lobbyMessages.teammateYouAccepted(playerData, targetData, team)
        );
        target.sendMessage(
                lobbyMessages.playerAcceptedBy(targetData, playerData, team)
        );
        target.sendMessage(
                lobbyMessages.playerJoined(targetData, team)
        );
    }

    public void sendDenied(Player player, PlayerData playerData, Player target, PlayerData targetData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfMessages messages = plugin.getMessages();
        final SfLobbyMessages lobbyMessages = messages.getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        if (target == null) {
            target = targetData.getPlayer().orElse(null);
            if (target == null) return;
        }

        final LobbyRoomState state = room.getState();
        final SfGameTeam team = state.getTeam();

        final ArrayList<PlayerData> players = new ArrayList<>(team.getPlayers());
        players.remove(playerData);

        messages.send(
                players,
                lobbyMessages.teammateDeniedBy(playerData, targetData, team)
        );
        player.sendMessage(
                lobbyMessages.teammateYouDenied(playerData, targetData)
        );
        target.sendMessage(
                lobbyMessages.playerDeniedBy(targetData, playerData, team)
        );
    }

    public void sendCantReady(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantReady(playerData)
        );
    }

    public void sendCantUnready(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.cantUnready(playerData)
        );
    }

    public void sendCantReadyCaptain(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.teammateCaptainCantReady(playerData)
        );
    }

    public void sendCantUnreadyCaptain(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final SfLobbyMessages messages = plugin.getMessages().getLobby();

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.sendMessage(
                messages.teammateCaptainCantUnready(playerData)
        );
    }


}
