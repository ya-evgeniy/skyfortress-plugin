package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfLobbyPlateJoin extends SfLobbyPlate {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyPlateJoin(SkyFortressPlugin plugin, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean activate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.joinPlate))
        {
            return false;
        }

        SfGameTeam gameTeam = this.settings.team;
        SfMessages messages = this.plugin.getMessages();
        if (gameTeam.getPlayers().size() < 1)
        {

            player.sendMessage(messages.player_joined(sfPlayer, settings.team));

            gameTeam.addPlayer(this.plugin, sfPlayer);
            player.setLocationAndRotation(
                    this.settings.accepted.getLocation(),
                    this.settings.accepted.getRotation()
            );
        }
        else if (settings.waitingPlayer == null)
        {
            settings.waitingPlayer = sfPlayer;
            player.setLocationAndRotation(
                    this.settings.waitingLocation.getLocation(),
                    this.settings.waitingLocation.getRotation()
            );

            player.sendMessage(messages.player_join(sfPlayer, gameTeam));
            messages.send(gameTeam.getPlayers(), messages.teammate_join(sfPlayer, gameTeam));

        }

        return true;
    }

}
