package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;

public class SfLobbyButtonAccept extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonAccept(SkyFortressPlugin plugin, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.acceptButton))
        {
            return false;
        }

        SfLobbySettings lobby = this.plugin.getLobby().getSettings();
        SfMessages messages = this.plugin.getMessages();
        if (settings.waitingPlayer != null)
        {
            Optional<Player> optionalWaiting = settings.waitingPlayer.getPlayer();
            if (optionalWaiting.isPresent())
            {
                Player waitingPlayer = optionalWaiting.get();

                waitingPlayer.sendMessage(messages.player_accept(sfPlayer, settings.waitingPlayer, settings.team));
                waitingPlayer.sendMessage(messages.player_joined(settings.waitingPlayer, settings.team));
                messages.send(
                        settings.team.getPlayers(),
                        messages.teammate_accept(sfPlayer, settings.waitingPlayer, settings.team)
                );

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
