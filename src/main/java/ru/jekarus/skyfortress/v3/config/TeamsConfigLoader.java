package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.serializer.view.TeamsConfigView;

import java.nio.file.Paths;

public class TeamsConfigLoader extends ConfigLoader {

    public TeamsConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("teams.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        serializer.deserialize(node, TeamsConfigView.class).addTo(plugin.getTeamContainer());
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }

}
