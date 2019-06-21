package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;
import ru.jekarus.skyfortress.v3.serializer.view.LobbyTeamsView;

import java.nio.file.Paths;

public class LobbySettingsConfigLoader extends ConfigLoader {

    public LobbySettingsConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("lobby.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        this.plugin.getSettings().setLobby(
                serializer.deserialize(node, LobbySettings.class)
        );

        serializer.deserialize(node, LobbyTeamsView.class).setTo(plugin.getLobby());
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }

}
