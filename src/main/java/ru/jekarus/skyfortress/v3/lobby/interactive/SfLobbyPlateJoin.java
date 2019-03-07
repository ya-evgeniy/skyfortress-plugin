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

        if (!plugin.getLobby().getSettings().canJoin) {
            player.sendMessage(Text.of("Вход в команды выключен"));
            return true;
        }

        SfGameTeam gameTeam = this.settings.team;
        SfMessages messages = this.plugin.getMessages();
        if (gameTeam.getPlayers().size() < 1) {
            player.sendMessage(messages.player_joined(sfPlayer, settings.team));
            this.lobbyTeam.setCaptainPlayer(sfPlayer);
        }
        else if (settings.waitingPlayer == null) {
            player.sendMessage(messages.player_join(sfPlayer, gameTeam));
            messages.send(gameTeam.getPlayers(), messages.teammate_join(sfPlayer, gameTeam));

            this.lobbyTeam.setWaitingPlayer(sfPlayer);
        }

        return true;
    }

}
