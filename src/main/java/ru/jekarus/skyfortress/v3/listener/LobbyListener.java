package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.Optional;

public class LobbyListener {

    private final SkyFortressPlugin plugin;
    private final SfPlayers players;

    public LobbyListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.players = SfPlayers.getInstance();
    }

    public void register()
    {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player)
    {
        Optional<SfPlayer> optionalSfPlayer = this.players.getPlayer(player);
        if (optionalSfPlayer.isPresent())
        {
            SfPlayer sfPlayer = optionalSfPlayer.get();
            if (sfPlayer.getTeam() == null)
            {
                sfPlayer.setTeam(this.plugin.getTeamContainer().getNoneTeam());
            }

            if (sfPlayer.getTeam().getType() == SfTeam.Type.NONE)
            {
                this.plugin.getLobby().playerDisconnect(sfPlayer, player);
            }
        }
    }

    @Listener
    public void onMove(MoveEntityEvent event, @Getter("getTargetEntity") Player player)
    {
        double playerY = event.getToTransform().getPosition().getY();
        double lobbyY = this.plugin.getLobby().getSettings().min_y;

        if (playerY < lobbyY)
        {
            Optional<SfPlayer> optionalSfPlayer = this.players.getPlayer(player);
            if (optionalSfPlayer.isPresent())
            {
                SfPlayer sfPlayer = optionalSfPlayer.get();
                if (sfPlayer.getTeam() == null)
                {
                    sfPlayer.setTeam(this.plugin.getTeamContainer().getNoneTeam());
                }
                if (sfPlayer.getTeam().getType() == SfTeam.Type.NONE)
                {
                    event.setToTransform(
                            new Transform<>(this.plugin.getLobby().getSettings().center.getLocation())
                    );
                }
            }
        }
    }

    @Listener
    public void onEntityDamage(DamageEntityEvent event, @Getter("getTargetEntity") Player player)
    {
        Optional<SfPlayer> optionalSfPlayer = this.players.getPlayer(player);
        if (optionalSfPlayer.isPresent())
        {
            SfPlayer sfPlayer = optionalSfPlayer.get();
            if (sfPlayer.getTeam() == null)
            {
                sfPlayer.setTeam(this.plugin.getTeamContainer().getNoneTeam());
            }
            if (sfPlayer.getTeam().getType() == SfTeam.Type.NONE)
            {
                event.setCancelled(true);
            }
        }
    }

}
