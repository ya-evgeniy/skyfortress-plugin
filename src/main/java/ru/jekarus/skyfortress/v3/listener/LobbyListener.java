package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
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
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;
import ru.jekarus.skyfortress.v3.team.SfTeam;

public class LobbyListener {

    private final SkyFortressPlugin plugin;
    private final PlayersDataContainer playersData;

    public LobbyListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.playersData = plugin.getPlayersDataContainer();
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = this.playersData.getOrCreateData(player);

        SfGameStageType stage = this.plugin.getGame().getStage();

        SfTeam playerTeam = playerData.getTeam();
        PlayerZone playerZone = playerData.getZone();

        if (playerZone == PlayerZone.TEAM_ROOM && stage == SfGameStageType.PRE_GAME) {
            this.plugin.getLobbyRoomsContainer().playerDisconnect(player, playerData);
        }
    }

    @Listener
    public void onMove(MoveEntityEvent event, @Getter("getTargetEntity") Player player) {
        double playerY = event.getToTransform().getPosition().getY();
        final LobbySettings lobbySettings = this.plugin.getSettings().getLobby();
        double lobbyY = lobbySettings.getMinY();

        if (playerY < lobbyY) {
            PlayerData playerData = this.playersData.getOrCreateData(player);
            PlayerZone playerZone = playerData.getZone();
            if (playerZone == PlayerZone.LOBBY) {
                event.setToTransform(
                        new Transform<>(lobbySettings.getCenter().getLocation())
                );
            }
        }
    }

    @Listener
    public void onEntityDamage(DamageEntityEvent event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = this.playersData.getOrCreateData(player);
        PlayerZone playerZone = playerData.getZone();

        if (playerZone == PlayerZone.LOBBY || playerZone == PlayerZone.TEAM_ROOM) {
            event.setCancelled(true);
        }
    }

    @Listener
    public void onRespawn(RespawnPlayerEvent event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = this.playersData.getOrCreateData(player);

        SfTeam playerTeam = playerData.getTeam();
        PlayerZone playerZone = playerData.getZone();

        if (playerTeam.getType() == SfTeam.Type.NONE) {
            LobbySettings settings = this.plugin.getSettings().getLobby();
            event.setToTransform(new Transform<>(
                    settings.getCenter().getLocation().getExtent(),
                    settings.getCenter().getLocation().getPosition(),
                    settings.getCenter().getRotation()
            ));
            playerData.setZone(PlayerZone.LOBBY);
        }

        if (playerTeam.getType() == SfTeam.Type.SPECTATOR) {

        }

    }

}
