package ru.jekarus.skyfortress.v3.listener;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.jserializer.itemstack.ItemStackSerializer;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.random.RandomDistribution;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.serializer.SfSerializers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Locale;

public class ConnectionListener {

    private final SkyFortressPlugin plugin;
    private SfPlayers players;

    public ConnectionListener()
    {
        this.plugin = SkyFortressPlugin.getInstance();
        this.players = SfPlayers.getInstance();

        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    @Listener
    public void onConnect(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
        sfPlayer.setLastPlayed(-1);

        if (sfPlayer.getLocale() == null) {
            SfLanguages languages = this.plugin.getLanguages();
            Locale locale = player.getLocale();

            if (languages.has(locale)) {
                sfPlayer.setLocale(locale);
            }
            else {
                sfPlayer.setLocale(languages.getDef());
            }
        }

        this.plugin.getScoreboards().setFor(sfPlayer, player);

        SfTeam playerTeam = sfPlayer.getTeam();
        PlayerZone playerZone = sfPlayer.getZone();

        SfGameStageType gameStage = this.plugin.getGame().getStage();

        if (playerZone == PlayerZone.LOBBY || playerZone == PlayerZone.GAME || playerZone == PlayerZone.TEAM_ROOM) {
            if (playerTeam == null) {
                playerTeam = this.plugin.getTeamContainer().getNoneTeam();
                playerTeam.addPlayer(this.plugin, sfPlayer);
            }

            if (playerTeam.getType() == SfTeam.Type.NONE) {
                player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
                player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                    effects.addElement(
                            PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
                    );
                    player.offer(effects);
                });

                SfLocation center = this.plugin.getLobby().getSettings().center;
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
                this.plugin.getDistributionController().onConnect(sfPlayer, player);
            }
            if (!plugin.getDistributionController().isEnabled()) {
                if (playerTeam.getType() == SfTeam.Type.GAME) {
                    Collection<SfLobbyTeam> teams = plugin.getLobby().getTeams();
                    for (SfLobbyTeam team : teams) {
                        SfLobbyTeamSettings settings = team.getSettings();
                        if (playerTeam == settings.team) {
                            sfPlayer.setZone(PlayerZone.TEAM_ROOM);
                            player.setLocationAndRotation(
                                    settings.accepted.getLocation(),
                                    settings.accepted.getRotation()
                            );
                            break;
                        }
                    }
                }
                else {
                    SfLobby lobby = plugin.getLobby();
                    SfLocation center = lobby.getSettings().center;

                    plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, sfPlayer);
                    sfPlayer.setZone(PlayerZone.LOBBY);
                    player.setLocationAndRotation(
                            center.getLocation(),
                            center.getRotation()
                    );
                }
            }
        }

    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player)
    {
        SfPlayer sfPlayer = players.getOrCreatePlayer(player);
        sfPlayer.setLastPlayed(System.currentTimeMillis());

        SfGameStageType stage = this.plugin.getGame().getStage();
        SfTeam playerTeam = sfPlayer.getTeam();

        PlayerZone playerZone = sfPlayer.getZone();
        if (playerZone == PlayerZone.LOBBY) {
            if (playerTeam.getType() == SfTeam.Type.GAME)
            {
                if (stage == SfGameStageType.IN_GAME)
                {
                    SfGameTeam gameTeam = (SfGameTeam) playerTeam;
                    CastleDeathEngine.checkCapturedCastle(this.plugin, gameTeam.getCastle());
                }
            }
        }
        else if (playerZone == PlayerZone.TEAM_ROOM) {

        }
        else if (playerZone == PlayerZone.GAME) {

        }
        else if (playerZone == PlayerZone.OTHER) {

        }
        else if (playerZone == PlayerZone.CAPTAIN_SYSTEM) {
            if (stage == SfGameStageType.PRE_GAME) {
                this.plugin.getDistributionController().onDisconnect(sfPlayer, player);
            }
        }
    }

}
