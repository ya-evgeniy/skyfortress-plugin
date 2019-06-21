package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.serializer.view.CastlesConfigView;

import java.nio.file.Paths;

public class CastlesConfigLoader extends ConfigLoader {

    public CastlesConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("castles.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        serializer.deserialize(node, CastlesConfigView.class).addTo(plugin.getCastleContainer());
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }
}
