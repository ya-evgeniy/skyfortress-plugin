package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfLobbyPlateJoin extends SfLobbyPlate {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeam lobbyTeam;
    private final SfLobbyTeamSettings settings;

    public SfLobbyPlateJoin(SkyFortressPlugin plugin, SfLobbyTeam lobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.lobbyTeam = lobbyTeam;
        this.settings = settings;
    }

    @Override
    public boolean activate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot) {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.joinPlate)) {
            return false;
        }

        if (settings.team == sfPlayer.getTeam()) {
            player.setLocationAndRotation(
                    settings.accepted.getLocation(),
                    settings.accepted.getRotation()
            );
            sfPlayer.setZone(PlayerZone.TEAM_ROOM);
            return true;
        }

        SfMessages messages = this.plugin.getMessages();
        if (!plugin.getLobby().getSettings().canJoin) {
            player.sendMessage(
                    messages.getLobby().cantJoin(sfPlayer)
            );
            return true;
        }

        SfGameTeam gameTeam = this.settings.team;
        if (gameTeam.getPlayers().size() < 1) {
            player.sendMessage(messages.getLobby().playerJoined(sfPlayer, settings.team));
            this.lobbyTeam.setCaptainPlayer(sfPlayer);
        }
        else if (settings.waitingPlayer == null) {
            player.sendMessage(messages.getLobby().playerWaitAccepted(sfPlayer));
            messages.send(gameTeam.getPlayers(), messages.getLobby().teammateWaitAccepted(sfPlayer));

            this.lobbyTeam.setWaitingPlayer(sfPlayer);
        }

        return true;
    }

}
