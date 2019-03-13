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

public class SfLobbyButtonDeny extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeam lobbyTeam;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonDeny(SkyFortressPlugin plugin, SfLobbyTeam lobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.lobbyTeam = lobbyTeam;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.denyButton))
        {
            return false;
        }

        SfMessages messages = this.plugin.getMessages();
        SfLobbyMessages lobby = messages.getLobby();

        if (this.settings.captain != sfPlayer && plugin.getLobby().getSettings().useLobbyCaptainSystem) {
            player.sendMessage(
                    lobby.teammateCaptainCantDeny(sfPlayer)
            );
            return true;
        }

        if (!plugin.getLobby().getSettings().canCancel) {
            player.sendMessage(
                    lobby.cantDeny(sfPlayer)
            );
            return true;
        }

        if (this.settings.waitingPlayer != null)
        {
            this.plugin.getLobby().moveToLobby(this.settings.waitingPlayer);
            this.settings.waitingPlayer.getPlayer().ifPresent(waitingPlayer -> {
                waitingPlayer.sendMessage(lobby.playerDeniedBy(settings.waitingPlayer, sfPlayer, settings.team));
                ArrayList<SfPlayer> teamPlayers = new ArrayList<>(settings.team.getPlayers());
                teamPlayers.remove(sfPlayer);
                messages.send(
                        teamPlayers,
                        lobby.teammateDeniedBy(sfPlayer, settings.waitingPlayer, settings.team)
                );
                player.sendMessage(
                        lobby.teammateYouDenied(sfPlayer, settings.waitingPlayer)
                );
            });

            this.settings.waitingPlayer = null;
            this.lobbyTeam.setWaitingPlayer(
                    this.lobbyTeam.getJoinedPlayer()
            );
        }
        return true;
    }

}
