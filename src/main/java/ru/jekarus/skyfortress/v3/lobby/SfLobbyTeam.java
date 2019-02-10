package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.interactive.*;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;

public class SfLobbyTeam {

    private SkyFortressPlugin plugin;
    private SfLobbyTeamSettings settings;

    private SfLobbyPlateJoin joinPlate;
    private SfLobbyPlateLeave leavePlate;

    private SfLobbyButtonLeave leaveButton;

    private SfLobbyButtonAccept acceptButton;
    private SfLobbyButtonDeny denyButton;

    private SfLobbyButtonReady buttonReady;

    public SfLobbyTeam(SfLobbyTeamSettings settings)
    {
        this.settings = settings;
    }

    public void init(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.settings.init(plugin);

        this.joinPlate = new SfLobbyPlateJoin(plugin, this.settings);
        this.leavePlate = new SfLobbyPlateLeave(plugin, this.settings);

        this.leaveButton = new SfLobbyButtonLeave(plugin, this.settings);

        this.acceptButton = new SfLobbyButtonAccept(plugin, this.settings);
        this.denyButton = new SfLobbyButtonDeny(plugin, this.settings);

        this.buttonReady = new SfLobbyButtonReady(plugin, this.settings);
    }

    public SfLobbyTeamSettings getSettings()
    {
        return this.settings;
    }

    public void setSettings(SfLobbyTeamSettings settings)
    {
        this.settings = settings;
    }

    public boolean standOnPlate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        return
                this.joinPlate.activate(player, sfPlayer, snapshot)
                || this.leavePlate.activate(player, sfPlayer, snapshot);
    }

    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        return
                this.leaveButton.pressButton(player, sfPlayer, snapshot)
                || this.denyButton.pressButton(player, sfPlayer, snapshot)
                || this.acceptButton.pressButton(player, sfPlayer, snapshot)
                || this.buttonReady.pressButton(player, sfPlayer, snapshot);
    }

    public void playerDisconnect(SfPlayer sfPlayer, Player player)
    {
        SfTeam gameTeam = sfPlayer.getTeam();
        if (gameTeam != this.settings.team)
        {
            return;
        }

        this.plugin.getTeamContainer().getNoneTeam().addPlayer(this.plugin, sfPlayer);
        if (this.settings.waitingPlayer != null && this.settings.waitingPlayer.getUniqueId().equals(player.getUniqueId()))
        {
            this.settings.waitingPlayer = null;
            for (Player anotherPlayer : Sponge.getServer().getOnlinePlayers())
            {
                if (SfUtils.compare(anotherPlayer.getLocation(), settings.joinPlate))
                {
                    settings.waitingPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);
                    player.setLocationAndRotation(
                            settings.waitingLocation.getLocation(),
                            settings.waitingLocation.getRotation()
                    );
                    break;
                }
            }
        }
        else
        {

            if (this.settings.team.getPlayers().size() > 0)
            {
                return;
            }

            if (settings.waitingPlayer != null)
            {
                Optional<Player> optionalWaiting = settings.waitingPlayer.getPlayer();
                if (optionalWaiting.isPresent())
                {
                    Player waitingPlayer = optionalWaiting.get();
                    settings.team.addPlayer(this.plugin, settings.waitingPlayer);
                    waitingPlayer.setLocationAndRotation(
                            settings.accepted.getLocation(),
                            settings.accepted.getRotation()
                    );
                }
                settings.waitingPlayer = null;

                for (Player anotherPlayer : Sponge.getServer().getOnlinePlayers())
                {
                    if (SfUtils.compare(anotherPlayer.getLocation(), settings.joinPlate))
                    {
                        settings.waitingPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);
                        player.setLocationAndRotation(
                                settings.waitingLocation.getLocation(),
                                settings.waitingLocation.getRotation()
                        );
                        break;
                    }
                }
            }

        }
    }

}
