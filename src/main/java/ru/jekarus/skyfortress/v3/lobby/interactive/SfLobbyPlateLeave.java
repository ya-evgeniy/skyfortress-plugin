package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;

public class SfLobbyPlateLeave extends SfLobbyPlate {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyPlateLeave(SkyFortressPlugin plugin, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean activate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.leavePlate))
        {
            return false;
        }

        this.plugin.getTeamContainer().getNoneTeam().addPlayer(this.plugin, sfPlayer);
        player.setLocationAndRotation(
                this.plugin.getLobby().getSettings().center.getLocation(),
                this.plugin.getLobby().getSettings().center.getRotation()
        );

        SfMessages messages = this.plugin.getMessages();
        player.sendMessage(messages.player_leave(sfPlayer, settings.team));
        messages.send(
                settings.team.getPlayers(),
                messages.teammate_leave(sfPlayer, settings.team)
        );

        if (this.settings.team.getPlayers().size() > 0)
        {
            return true;
        }

        if (settings.waitingPlayer != null)
        {
            Optional<Player> optionalWaiting = settings.waitingPlayer.getPlayer();
            if (optionalWaiting.isPresent())
            {
                Player waitingPlayer = optionalWaiting.get();

                player.sendMessage(messages.player_joined(sfPlayer, settings.team));

                settings.team.addPlayer(this.plugin, settings.waitingPlayer);
                waitingPlayer.setLocationAndRotation(
                        settings.accepted.getLocation(),
                        settings.accepted.getRotation()
                );
            }
            settings.waitingPlayer = null;

//            for (Player anotherPlayer : Sponge.getServer().getOnlinePlayers())
//            {
//                if (SfUtils.compare(anotherPlayer.getLocation(), settings.joinPlate))
//                {
//                    settings.waitingPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);
//                    player.setLocationAndRotation(
//                            settings.waitingLocation.getLocation(),
//                            settings.waitingLocation.getRotation()
//                    );
//                    break;
//                }
//            }
        }

        return true;
    }

}
