package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

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
    public void onConnect(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
//        Optional<SfPlayer> optionalSfPlayer = this.players.getPlayer(player);
//        if (optionalSfPlayer.isPresent())
//        {
//            SfPlayer sfPlayer = optionalSfPlayer.get();
//            if (sfPlayer.getTeam() == null)
//            {
//                sfPlayer.setTeam(this.plugin.getTeamContainer().getNoneTeam());
//            }
//
//            if (sfPlayer.getTeam().getType() == SfTeam.Type.NONE)
//            {
//                if (plugin.getGame().getStage() == SfGameStageType.PRE_GAME) {
//                    this.plugin.getLobby().playerDisconnect(sfPlayer, player);
//                }
//            }
//        }
    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);

        SfGameStageType stage = this.plugin.getGame().getStage();

        SfTeam playerTeam = sfPlayer.getTeam();
        PlayerZone playerZone = sfPlayer.getZone();

        if (playerZone == PlayerZone.TEAM_ROOM && stage == SfGameStageType.PRE_GAME) {
            this.plugin.getLobby().playerDisconnect(sfPlayer, player);
        }
    }

    @Listener
    public void onMove(MoveEntityEvent event, @Getter("getTargetEntity") Player player) {
        double playerY = event.getToTransform().getPosition().getY();
        double lobbyY = this.plugin.getLobby().getSettings().min_y;

        if (playerY < lobbyY) {
            SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
            PlayerZone playerZone = sfPlayer.getZone();
            if (playerZone == PlayerZone.LOBBY) {
                event.setToTransform(
                        new Transform<>(this.plugin.getLobby().getSettings().center.getLocation())
                );
            }
        }
    }

    @Listener
    public void onEntityDamage(DamageEntityEvent event, @Getter("getTargetEntity") Player player) {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
        PlayerZone playerZone = sfPlayer.getZone();

        if (playerZone == PlayerZone.LOBBY || playerZone == PlayerZone.TEAM_ROOM) {
            event.setCancelled(true);
        }
    }

    @Listener
    public void onRespawn(RespawnPlayerEvent event, @Getter("getTargetEntity") Player player) {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);

        SfTeam playerTeam = sfPlayer.getTeam();
        PlayerZone playerZone = sfPlayer.getZone();

        if (playerTeam.getType() == SfTeam.Type.NONE) {
            SfLobbySettings settings = this.plugin.getLobby().getSettings();
            event.setToTransform(new Transform<>(
                    settings.center.getLocation().getExtent(),
                    settings.center.getLocation().getPosition(),
                    settings.center.getRotation()
            ));
            sfPlayer.setZone(PlayerZone.LOBBY);
        }

        if (playerTeam.getType() == SfTeam.Type.SPECTATOR) {

        }

    }

}
