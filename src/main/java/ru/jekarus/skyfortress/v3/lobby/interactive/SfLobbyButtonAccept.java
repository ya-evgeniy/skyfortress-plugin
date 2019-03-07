package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;

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

        if (this.settings.captain != sfPlayer && plugin.getLobby().getSettings().useLobbyCaptainSystem) {
            player.sendMessage(Text.of("Ты не капитан :("));
            return true;
        }

        if (!plugin.getLobby().getSettings().canAccept) {
            player.sendMessage(Text.of("Принятие игроков выключено"));
            return true;
        }

        SfMessages messages = this.plugin.getMessages();
        if (settings.waitingPlayer != null)
        {
            this.lobbyTeam.addToTeam(settings.waitingPlayer);
            settings.waitingPlayer.getPlayer().ifPresent(waitingPlayer -> {
                waitingPlayer.sendMessage(messages.player_accept(sfPlayer, settings.waitingPlayer, settings.team));
                waitingPlayer.sendMessage(messages.player_joined(settings.waitingPlayer, settings.team));
                messages.send(
                        settings.team.getPlayers(),
                        messages.teammate_accept(sfPlayer, settings.waitingPlayer, settings.team)
                );
            });

            settings.waitingPlayer = null;
            this.lobbyTeam.setWaitingPlayer(
                    this.lobbyTeam.getJoinedPlayer()
            );
        }
        return true;
    }

}
