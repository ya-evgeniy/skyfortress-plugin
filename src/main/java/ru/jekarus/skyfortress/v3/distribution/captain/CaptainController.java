package ru.jekarus.skyfortress.v3.distribution.captain;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.distribution.captain.CaptainDistributionCommand;
import ru.jekarus.skyfortress.v3.distribution.Distribution;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigCaptain;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.serializer.SfSerializers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CaptainController implements Distribution {

    private final SkyFortressPlugin plugin;
    private final DistributionController distributionController;

    private CaptainDistribution distribution;
    private Distribution.State currentState = State.STARTUP;

    private CaptainSettings settings;
    private boolean useExistingTeams = false;

    public CaptainController(SkyFortressPlugin plugin, DistributionController distributionController) {
        this.plugin = plugin;
        this.distributionController = distributionController;
    }

    public void onDisconnect(SfPlayer sfPlayer, Player player) {
        if (distribution != null) {
            distribution.onDisconnect(sfPlayer, player);
        }
    }

    public void onConnect(SfPlayer sfPlayer, Player player) {
        if (distribution != null) {
            distribution.onConnect(sfPlayer, player);
        }
    }

    public void start(CaptainSettings settings) {
        if (currentState != State.STARTUP) {
            return;
        }
        this.settings = settings;
        changeState(State.LOAD_CONFIG);
    }

    private void changeState(Distribution.State state) {
        this.currentState = state;
        switch (state) {
            case LOAD_CONFIG:
                loadConfig();
                break;
            case ERROR_CONFIG:
                break;
            case DISTRIBUTION:
                SfPlayers players = SfPlayers.getInstance();
                Collection<Player> onlinePlayers = Sponge.getServer().getOnlinePlayers();
                List<SfPlayer> sfPlayers = onlinePlayers.stream().map(players::getOrCreatePlayer).collect(Collectors.toList());
                for (int i = 0; i < 10; i++) {
                    sfPlayers.add(new SfPlayer(
                            UUID.randomUUID(),
                            "ENTITY_" + i
                    ));
                }
                this.distribution.start(this.settings, sfPlayers);
                break;
        }
    }

    private void loadConfig() {
        Path directory = Sponge.getConfigManager().getPluginConfig(this.plugin).getDirectory();
        Path captainsFilePath = Paths.get(directory.toString(), "/captain_system.conf");

        if (Files.notExists(captainsFilePath)) { // fixme copy default
            changeState(State.ERROR_CONFIG);
            return;
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(captainsFilePath)
                .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
                .build();

        try
        {
            CommentedConfigurationNode node = loader.load();
            CaptainConfig config = node.getNode("captain_system").getValue(TypeToken.of(CaptainConfig.class));

            if (config == null) {
                changeState(State.ERROR_CONFIG);
                return;
            }

            for (CaptainConfigCaptain captain : config.captains) {
                Optional<SfTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(captain.teamId);
                if (optionalTeam.isPresent()) {
                    captain.team = (SfGameTeam) optionalTeam.get();
                }
                else {
                    changeState(State.ERROR_CONFIG);
                    return;
                }
            }

            distribution = new CaptainDistribution(this, this.plugin, config);
            changeState(State.DISTRIBUTION);
        }
        catch (IOException | ObjectMappingException e)
        {
            changeState(State.ERROR_CONFIG);
            e.printStackTrace();
        }
    }

    public void onEnd() {
        changeState(State.ENDED);
        this.distribution = null;
        this.distributionController.disposeDistribution();
    }

    public void cancel(boolean saveTeams) {
        this.distribution.cancel(saveTeams);
    }

    @Override
    public Type getType() {
        return Type.CAPTAIN;
    }

    @Override
    public State getState() {
        return this.currentState;
    }

    @Override
    public void serverStopping() {
        for (CaptainTarget target : this.distribution.getState().targetByPlayerUniqueId.values()) {
            target.getEntity().ifPresent(entity -> {
                if (entity.getType().equals(EntityTypes.PLAYER)) {
                    entity.offer(Keys.GLOWING, false);
                }
                else {
                    entity.remove();
                }
            });
        }
    }

}
