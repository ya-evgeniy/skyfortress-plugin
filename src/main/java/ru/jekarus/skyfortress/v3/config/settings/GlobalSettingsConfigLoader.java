package ru.jekarus.skyfortress.v3.config.settings;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.config.ConfigLoader;
import ru.jekarus.skyfortress.v3.settings.GlobalSettings;
import ru.jekarus.skyfortress.v3.settings.WorldMapSettings;

import java.nio.file.Paths;

public class GlobalSettingsConfigLoader extends ConfigLoader {

    public GlobalSettingsConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("global.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        this.plugin.getSettings().setGlobal(
                serializer.deserialize(node, GlobalSettings.class)
        );
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }

}
