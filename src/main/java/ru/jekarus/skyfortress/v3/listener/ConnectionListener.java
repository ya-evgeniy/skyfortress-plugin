package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.PlayerChangeClientSettingsEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomSettings;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomState;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.List;
import java.util.Locale;

public class ConnectionListener {

    private final SkyFortressPlugin plugin;
    private PlayersDataContainer players;

    public ConnectionListener() {
        this.plugin = SkyFortressPlugin.getInstance();
        this.players = PlayersDataContainer.getInstance();

        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    @Listener
    public void onChangeClientSettings(PlayerChangeClientSettingsEvent event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = players.getOrCreateData(player);
        SfLanguages languages = plugin.getLanguages();

        Locale locale = event.getLocale();
        if (languages.has(locale)) {
            playerData.setLocale(locale);
        }
        plugin.getScoreboards().setFor(playerData, player);
    }

    @Listener
    public void onConnect(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = this.players.getOrCreateData(player);
        playerData.setLastPlayed(-1);

        if (playerData.getLocale() == null) {
            SfLanguages languages = this.plugin.getLanguages();
            playerData.setLocale(languages.getDef());
        }

        this.plugin.getScoreboards().setFor(playerData, player);

        SfTeam playerTeam = playerData.getTeam();
        PlayerZone playerZone = playerData.getZone();

        SfGameStageType gameStage = this.plugin.getGame().getStage();

        if (playerZone == PlayerZone.LOBBY || playerZone == PlayerZone.GAME || playerZone == PlayerZone.TEAM_ROOM) {
            if (playerTeam == null) {
                playerTeam = this.plugin.getTeamContainer().getNoneTeam();
                playerTeam.addPlayer(this.plugin, playerData);
            }

            if (playerTeam.getType() == SfTeam.Type.NONE || gameStage == SfGameStageType.END_GAME) {
                player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
                player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                    effects.addElement(
                            PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
                    );
                    player.offer(effects);
                });

                LocationAndRotation center = this.plugin.getSettings().getLobby().getCenter();

                System.out.println(center.getLocation().getPosition());

                player.setLocationAndRotation(
                        center.getLocation(),
                        center.getRotation()
                );
                return;
            }
        }

        if (playerZone == PlayerZone.LOBBY) {

        }
        else if (playerZone == PlayerZone.TEAM_ROOM) {

        }
        else if (playerZone == PlayerZone.GAME) {
            if (playerTeam.getType() == SfTeam.Type.GAME) {
                SfGameTeam gameTeam = (SfGameTeam) playerTeam;
                if (!gameTeam.getCastle().isAlive()) {
                    //fixme drop all items in inventory
                    SfUtils.setPlayerSpectator(player);
                }
                else if (gameTeam.getCastle().isCaptured()) {
                    player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                        effects.addElement(
                                PotionEffect.builder().potionType(PotionEffectTypes.STRENGTH).duration(1_000_000).amplifier(0).particles(false).build()
                        );
                        player.offer(effects);
                    });
                }
            }
        }
        else if (playerZone == PlayerZone.OTHER) {
        }
        else if (playerZone == PlayerZone.CAPTAIN_SYSTEM) {
            if (gameStage == SfGameStageType.PRE_GAME) {
                this.plugin.getDistributionController().onConnect(playerData, player);
            }
            if (!plugin.getDistributionController().isEnabled()) {
                if (playerTeam.getType() == SfTeam.Type.GAME) {
                    List<LobbyRoom> rooms = plugin.getLobbyRoomsContainer().getRooms();
                    for (LobbyRoom room : rooms) {
                        final LobbyRoomSettings settings = room.getSettings();
                        final LobbyRoomState state = room.getState();
                        if (playerTeam == state.getTeam()) {
                            playerData.setZone(PlayerZone.TEAM_ROOM);
                            player.setLocationAndRotation(
                                    settings.getAccepted().getLocation(),
                                    settings.getAccepted().getRotation()
                            );
                            break;
                        }
                    }
                }
                else {
                    final LocationAndRotation center = plugin.getSettings().getLobby().getCenter();

                    plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, playerData);
                    playerData.setZone(PlayerZone.LOBBY);
                    player.setLocationAndRotation(
                            center.getLocation(),
                            center.getRotation()
                    );
                }
            }
        }

    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        PlayerData playerData = players.getOrCreateData(player);
        playerData.setLastPlayed(System.currentTimeMillis());

        SfGameStageType stage = this.plugin.getGame().getStage();
        SfTeam playerTeam = playerData.getTeam();

        PlayerZone playerZone = playerData.getZone();
        if (playerZone == PlayerZone.LOBBY) {

        }
        else if (playerZone == PlayerZone.TEAM_ROOM) {

        }
        else if (playerZone == PlayerZone.GAME) {
            if (playerTeam.getType() == SfTeam.Type.GAME) {
                if (stage == SfGameStageType.IN_GAME) {
                    SfGameTeam gameTeam = (SfGameTeam) playerTeam;
                    CastleDeathEngine.checkCapturedCastle(this.plugin, gameTeam.getCastle());
                }
            }
        }
        else if (playerZone == PlayerZone.OTHER) {

        }
        else if (playerZone == PlayerZone.CAPTAIN_SYSTEM) {
            if (stage == SfGameStageType.PRE_GAME) {
                this.plugin.getDistributionController().onDisconnect(playerData, player);
            }
        }
    }

}
