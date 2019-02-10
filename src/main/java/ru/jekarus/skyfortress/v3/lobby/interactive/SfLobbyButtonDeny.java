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

public class SfLobbyButtonDeny extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonDeny(SkyFortressPlugin plugin, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (!SfUtils.compare(snapshot.getPosition(), this.settings.denyButton))
        {
            return false;
        }

        SfLobbySettings lobby = this.plugin.getLobby().getSettings();
        SfMessages messages = this.plugin.getMessages();
        if (this.settings.waitingPlayer != null)
        {
            Optional<Player> optionalWaiting = this.settings.waitingPlayer.getPlayer();

            if (optionalWaiting.isPresent())
            {
                Player waitingPlayer = optionalWaiting.get();

                waitingPlayer.sendMessage(messages.player_deny(sfPlayer, settings.waitingPlayer, settings.team));
                messages.send(
                        settings.team.getPlayers(),
                        messages.teammate_deny(sfPlayer, settings.waitingPlayer, settings.team)
                );

                waitingPlayer.setLocationAndRotation(
                        lobby.center.getLocation(),
                        lobby.center.getRotation()
                );

            }
            this.settings.waitingPlayer = null;

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
