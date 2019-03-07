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

        if (this.settings.captain != sfPlayer && plugin.getLobby().getSettings().useLobbyCaptainSystem) {
            player.sendMessage(Text.of("Ты не капитан :("));
            return true;
        }

        if (!plugin.getLobby().getSettings().canCancel) {
            player.sendMessage(Text.of("Отклонение игроков выключено"));
            return true;
        }

        SfMessages messages = this.plugin.getMessages();
        if (this.settings.waitingPlayer != null)
        {
            this.plugin.getLobby().moveToLobby(this.settings.waitingPlayer);
            this.settings.waitingPlayer.getPlayer().ifPresent(waitingPlayer -> {
                waitingPlayer.sendMessage(messages.player_deny(sfPlayer, settings.waitingPlayer, settings.team));
                messages.send(
                        settings.team.getPlayers(),
                        messages.teammate_deny(sfPlayer, settings.waitingPlayer, settings.team)
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
