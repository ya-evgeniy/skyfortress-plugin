package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfLobbyButtonLeave extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeam lobbyTeam;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonLeave(SkyFortressPlugin plugin, SfLobbyTeam lobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.lobbyTeam = lobbyTeam;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.waitingLeaveButton))
        {
            return false;
        }

        if (this.settings.waitingPlayer == sfPlayer)
        {
            this.plugin.getLobby().moveToLobby(sfPlayer);
            this.settings.waitingPlayer = null;

            this.lobbyTeam.setWaitingPlayer(
                    this.lobbyTeam.getJoinedPlayer()
            );
        }
        return true;
    }

}
