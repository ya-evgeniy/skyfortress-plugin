package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import ru.jekarus.skyfortress.v3.SfSettings;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.resource.SfResource;
import ru.jekarus.skyfortress.v3.resource.SfResourceContainer;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SfSerializer {

    private final SkyFortressPlugin plugin;
    private final Path directory;
    private final Path global;

    public SfSerializer(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.directory = Sponge.getConfigManager().getPluginConfig(this.plugin).getDirectory();
        this.global = Paths.get(this.directory.toString(), "/global.conf");
    }

    public void load()
    {
        if (!Files.exists(this.global))
        {
            this.createDefault();
        }
        if (!Files.exists(this.global))
        {
            return;
        }
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(this.global)
                .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
                .build();
        try
        {
            CommentedConfigurationNode node = loader.load();
            this.construct(node);
        }
        catch (IOException | ObjectMappingException e)
        {
            e.printStackTrace();
        }
    }

    private void construct(CommentedConfigurationNode node) throws ObjectMappingException
    {
        this.constructSettings(node);
        this.constructTeams(node);
        this.constructCastles(node);
        this.constructResources(node);
        this.constructLobby(node);
    }

    private void constructSettings(CommentedConfigurationNode node) throws ObjectMappingException
    {
        SfSettings settings = node.getNode("settings").getValue(TypeToken.of(SfSettings.class));
        this.plugin.setSettings(settings);
    }

    private void constructTeams(CommentedConfigurationNode node) throws ObjectMappingException
    {
        CommentedConfigurationNode teams = node.getNode("teams");
        SfTeamContainer teamContainer = this.plugin.getTeamContainer();
        for (CommentedConfigurationNode teamNode : teams.getChildrenList())
        {
            SfTeam team = teamNode.getValue(TypeToken.of(SfTeam.class));
            teamContainer.add(team);
        }
    }

    private void constructCastles(CommentedConfigurationNode node) throws ObjectMappingException
    {
        CommentedConfigurationNode castles = node.getNode("castles");
        SfCastleContainer castleContainer = this.plugin.getCastleContainer();
        for (CommentedConfigurationNode castleNode : castles.getChildrenList())
        {
            SfCastle castle = castleNode.getValue(TypeToken.of(SfCastle.class));
            castleContainer.add(castle);
        }
    }

    private void constructResources(CommentedConfigurationNode node) throws ObjectMappingException
    {
        CommentedConfigurationNode resources = node.getNode("resources");
        SfResourceContainer resourceContainer = this.plugin.getResourceContainer();
        for (CommentedConfigurationNode resourceNode : resources.getChildrenList())
        {
            SfResource resource = resourceNode.getValue(TypeToken.of(SfResource.class));
            resourceContainer.add(resource);
        }
    }

    private void constructLobby(CommentedConfigurationNode node) throws ObjectMappingException
    {
        CommentedConfigurationNode lobbyNode = node.getNode("lobby");
        SfLobby lobby = this.plugin.getLobby();

        CommentedConfigurationNode settings = lobbyNode.getNode("settings");
        lobby.getSettings().canJoin = settings.getNode("can_join").getBoolean(false);
        lobby.getSettings().canLeave = settings.getNode("can_leave").getBoolean(false);
        lobby.getSettings().canReady = settings.getNode("can_ready").getBoolean(false);
        lobby.getSettings().center = settings.getNode("center").getValue(TypeToken.of(SfLocation.class));
        lobby.getSettings().min_y = settings.getNode("min_y").getDouble();

        CommentedConfigurationNode teams = lobbyNode.getNode("teams");
        for (CommentedConfigurationNode team : teams.getChildrenList())
        {
            SfLobbyTeam lobbyTeam = team.getValue(TypeToken.of(SfLobbyTeam.class));
            lobby.add(lobbyTeam);
        }
    }

    private void createDefault()
    {

        if (!Files.exists(this.directory))
        {
            try
            {
                Files.createDirectories(this.directory);
            }
            catch (IOException e)
            {
                return;
            }
        }

        InputStream source = SkyFortressPlugin.class.getResourceAsStream("/assets/global.conf");
        try {
            Files.copy(source, this.global, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
