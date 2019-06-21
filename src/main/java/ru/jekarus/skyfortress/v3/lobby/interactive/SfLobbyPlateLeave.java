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
import java.util.Collection;
import java.util.Random;

public class SfLobbyPlateLeave extends SfLobbyPlate {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeam lobbyTeam;
    private final SfLobbyTeamSettings settings;

    public SfLobbyPlateLeave(SkyFortressPlugin plugin, SfLobbyTeam lobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.lobbyTeam = lobbyTeam;
        this.settings = settings;
    }

    @Override
    public boolean activate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.leavePlate))
        {
            return false;
        }

        SfMessages messages = this.plugin.getMessages();
        SfLobbyMessages lobby = messages.getLobby();
        if (!plugin.getSettings().getGlobalLobby().isCanLeaveTeam()) {
            player.sendMessage(
                    lobby.cantLeave(sfPlayer)
            );
            return true;
        }

        this.plugin.getTeamContainer().getNoneTeam().addPlayer(this.plugin, sfPlayer);
        this.plugin.getLobby().moveToLobby(sfPlayer);

        player.sendMessage(lobby.playerLeaved(sfPlayer, settings.team));
        messages.send(
                settings.team.getPlayers(),
                lobby.teammateLeaved(sfPlayer)
        );

        this.lobbyTeam.checkOnlinePlayers();

        if (settings.captain == sfPlayer) {
            settings.captain = null;

            Collection<SfPlayer> players = this.settings.team.getPlayers();
            if (players.size() > 0) {
                ArrayList<SfPlayer> playersList = new ArrayList<>(players);
                int nextCaptainId = new Random().nextInt(players.size());
                settings.captain = playersList.get(nextCaptainId);
                return true;
            }
        }

        if (settings.waitingPlayer != null)
        {
            this.lobbyTeam.setCaptainPlayer(settings.waitingPlayer);
            settings.waitingPlayer.getPlayer().ifPresent(waitingPlayer -> {
                waitingPlayer.sendMessage(lobby.playerJoined(sfPlayer, settings.team));
            });

            this.lobbyTeam.setWaitingPlayer(
                    this.lobbyTeam.getJoinedPlayer()
            );
        }

        return true;
    }

}
