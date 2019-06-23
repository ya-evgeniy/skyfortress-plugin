package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

public class LobbyRoomMovement {

    private final LobbyRoom room;

    public LobbyRoomMovement(LobbyRoom room) {
        this.room = room;
    }

    public void moveToAccepted(Player player, PlayerData playerData) {
        final LobbyRoomSettings settings = room.getSettings();
        final LocationAndRotation accepted = settings.getAccepted();

        playerData.setZone(PlayerZone.TEAM_ROOM);
        room.getState().getTeam().addPlayer(room.getPlugin(), playerData);

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.setLocationAndRotation(
                accepted.getLocation(),
                accepted.getRotation()
        );
    }

    public void moveToWaiting(Player player, PlayerData playerData) {
        final LobbyRoomSettings settings = room.getSettings();
        final LocationAndRotation waiting = settings.getWaiting();

        playerData.setZone(PlayerZone.TEAM_ROOM);

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.setLocationAndRotation(
                waiting.getLocation(),
                waiting.getRotation()
        );
    }

    public void moveToLobby(Player player, PlayerData playerData) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final LobbySettings lobbySettings = plugin.getSettings().getLobby();
        final LocationAndRotation center = lobbySettings.getCenter();

        playerData.setZone(PlayerZone.LOBBY);
        plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, playerData);

        if (player == null) {
            player = playerData.getPlayer().orElse(null);
            if (player == null) return;
        }

        player.setLocationAndRotation(
                center.getLocation(),
                center.getRotation()
        );
    }

}
