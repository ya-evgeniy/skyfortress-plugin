package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLobbyMessages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.ArrayList;

public class SfLobbyButtonAccept extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeam lobbyTeam;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonAccept(SkyFortressPlugin plugin, SfLobbyTeam lobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.lobbyTeam = lobbyTeam;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.acceptButton))
        {
            return false;
        }

        SfMessages messages = this.plugin.getMessages();
        SfLobbyMessages lobby = messages.getLobby();

        if (this.settings.captain != sfPlayer && plugin.getLobby().getSettings().useLobbyCaptainSystem) {
            player.sendMessage(
                    lobby.teammateCaptainCantAccept(sfPlayer)
            );
            return true;
        }

        if (!plugin.getLobby().getSettings().canAccept) {
            player.sendMessage(
                    lobby.cantAccept(sfPlayer)
            );
            return true;
        }

        if (settings.waitingPlayer != null)
        {
            settings.waitingPlayer.getPlayer().ifPresent(waitingPlayer -> {
                waitingPlayer.sendMessage(lobby.playerAcceptedBy(settings.waitingPlayer, sfPlayer, settings.team));
                waitingPlayer.sendMessage(lobby.playerJoined(settings.waitingPlayer, settings.team));
                ArrayList<SfPlayer> teamPlayers = new ArrayList<>(settings.team.getPlayers());
                teamPlayers.remove(sfPlayer);
                messages.send(
                        teamPlayers,
                        lobby.teammateAcceptedBy(sfPlayer, settings.waitingPlayer, settings.team)
                );
                player.sendMessage(
                        lobby.teammateYouAccepted(sfPlayer, settings.waitingPlayer, settings.team)
                );
            });
            this.lobbyTeam.addToTeam(settings.waitingPlayer);

            settings.waitingPlayer = null;
            this.lobbyTeam.setWaitingPlayer(
                    this.lobbyTeam.getJoinedPlayer()
            );
        }
        return true;
    }

}
