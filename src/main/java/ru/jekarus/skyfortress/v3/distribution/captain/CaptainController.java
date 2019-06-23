package ru.jekarus.skyfortress.v3.distribution.captain;

import jekarus.hocon.config.serializer.ConfigSerializer;
import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.Distribution;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigCaptain;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CaptainController implements Distribution {

    private final SkyFortressPlugin plugin;
    private final DistributionController distributionController;
    private final BiConsumer<CaptainController, ResultMessage> consumer;

    @Getter private CaptainDistribution distribution;
    private Distribution.State currentState = State.STARTUP;

    private CaptainSettings settings;
    private boolean useExistingTeams = false;

    public CaptainController(SkyFortressPlugin plugin, DistributionController distributionController, BiConsumer<CaptainController, CaptainController.ResultMessage> consumer) {
        this.plugin = plugin;
        this.distributionController = distributionController;
        this.consumer = consumer;
    }

    public void onDisconnect(PlayerData playerData, Player player) {
        if (distribution != null) {
            distribution.onDisconnect(playerData, player);
        }
    }

    public void onConnect(PlayerData playerData, Player player) {
        if (distribution != null) {
            distribution.onConnect(playerData, player);
        }
    }

    public void start(CaptainSettings settings) {
        if (currentState != State.STARTUP) {
            consumer.accept(this, ResultMessage.ALREADY_STARTED);
            return;
        }
        this.settings = settings;
        changeState(State.LOAD_CONFIG);
    }

    private void changeState(Distribution.State state) {
        this.currentState = state;
        switch (state) {
            case LOAD_CONFIG:
                this.consumer.accept(this, ResultMessage.LOAD_CONFIG);
                loadConfig();
                break;
            case ERROR_CONFIG:
                this.consumer.accept(this, ResultMessage.ERROR_CONFIG);
                this.distributionController.disposeDistribution();
                break;
            case DISTRIBUTION:
                this.consumer.accept(this, ResultMessage.START_DISTRIBUTION);
                PlayersDataContainer playersData = plugin.getPlayersDataContainer();
                Collection<Player> onlinePlayers = Sponge.getServer().getOnlinePlayers();
                List<PlayerData> playerDatas = onlinePlayers.stream().map(playersData::getOrCreateData).collect(Collectors.toList());
//                for (int i = 0; i < 10; i++) {
//                    playerDatas.register(new PlayerData(
//                            UUID.randomUUID(),
//                            "ENTITY_" + i
//                    ));
//                }
                this.distribution.start(this.settings, playerDatas);
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
//                .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
                .build();

        final ConfigSerializer serializer = new ConfigSerializer(loader);

        try
        {
            CommentedConfigurationNode node = loader.load();
            CaptainConfig config = serializer.deserialize(node, CaptainConfig.class);

            if (config == null) {
                changeState(State.ERROR_CONFIG);
                return;
            }

            for (CaptainConfigCaptain captain : config.captains) {
                Optional<SfGameTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(captain.teamId);
                if (optionalTeam.isPresent()) {
                    captain.team = optionalTeam.get();
                }
                else {
                    changeState(State.ERROR_CONFIG);
                    return;
                }
            }

            distribution = new CaptainDistribution(this, this.plugin, config);
            changeState(State.DISTRIBUTION);
        }
        catch (IOException | UnsupportedOperationException e)
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

    public enum ResultMessage {

        ALREADY_STARTED,
        LOAD_CONFIG,
        ERROR_CONFIG,
        START_DISTRIBUTION

    }

}
