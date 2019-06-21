package ru.jekarus.skyfortress.v3.config.settings;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.config.ConfigLoader;
import ru.jekarus.skyfortress.v3.settings.WorldMapSettings;

import java.nio.file.Paths;

public class WorldMapConfigLoader extends ConfigLoader {

    public WorldMapConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("world_map_settings.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        this.plugin.getSettings().setWorldMap(
                serializer.deserialize(node, WorldMapSettings.class)
        );
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }

}
