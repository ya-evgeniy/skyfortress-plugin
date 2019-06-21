package ru.jekarus.skyfortress.v3.config;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.config.settings.GlobalLobbySettingsConfigLoader;
import ru.jekarus.skyfortress.v3.config.settings.GlobalSettingsConfigLoader;
import ru.jekarus.skyfortress.v3.config.settings.WorldMapConfigLoader;

public class ConfigLoaders {

    private final SkyFortressPlugin plugin;

    public ConfigLoaders(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {

        new GlobalSettingsConfigLoader(this.plugin).load();
        new GlobalLobbySettingsConfigLoader(this.plugin).load();

        new WorldMapConfigLoader(this.plugin).load();
        new LobbySettingsConfigLoader(this.plugin).load();

        new CastlesConfigLoader(this.plugin).load();
        new TeamsConfigLoader(this.plugin).load();

        new ResourcesConfigLoader(this.plugin).load();

    }

}
