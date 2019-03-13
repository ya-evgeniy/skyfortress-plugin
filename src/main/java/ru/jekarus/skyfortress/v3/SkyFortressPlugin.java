package ru.jekarus.skyfortress.v3;

import com.google.inject.Inject;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.command.SfCommandManager;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.engine.SfEngineManager;
import ru.jekarus.skyfortress.v3.game.SfGame;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.listener.ConnectionListener;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.resource.SfResourceContainer;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.serializer.SfSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.SfLanguagesSerializer;
import ru.jekarus.skyfortress.v3.serializer.shop.ShopSerializer;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.util.logging.Logger;

@Plugin(
        id = SkyFortressPlugin.ID,
        name = SkyFortressPlugin.NAME,
        version = SkyFortressPlugin.MAP_VERSION + "_" + SkyFortressPlugin.VERSION,
        authors = {"jekarus"}
)
public class SkyFortressPlugin {

    public static final String ID = "sky_fortress";
    public static final String NAME = "Sky Fortress";
    public static final String VERSION = "1.0";
    public static final String MAP_VERSION = "3.0";

    public static final Text SF_NAME = Text.builder().append(Text.of(NAME)).color(TextColors.GOLD).build();
    public static final Text SF_VERSION = Text.builder().append(Text.of(MAP_VERSION)).color(TextColors.GRAY).build();
    public static final Text SF_NAME_VERSION = Text.builder().append(SF_NAME).append(Text.of(" ")).append(SF_VERSION).build();

    private static SkyFortressPlugin instance;
    private World world;

    private SfLobby lobby = new SfLobby(this);

    private SfGame game = new SfGame(this);
    private SfSettings settings = new SfSettings();

    private final SfTeamContainer teamContainer = new SfTeamContainer();
    private final SfCastleContainer castleContainer = new SfCastleContainer();
    private final SfResourceContainer resourceContainer = new SfResourceContainer();

    private SfEngineManager engineManager;
    private SfCommandManager commandManager = new SfCommandManager(this);

    private final SfScoreboards scoreboards = new SfScoreboards(this);

    private DistributionController distributionController;

    private final SfLanguages languages = new SfLanguages();
    private final SfMessages messages = new SfMessages(this);

    @Inject
    private Logger logger;

    @Listener
    public void onConstruction(GameConstructionEvent event)
    {
        instance = this;
    }

    @Listener
    public void onPostInit(GameStartedServerEvent event)
    {
        Server server = Sponge.getServer();
        this.world = server.getWorld(server.getDefaultWorldName()).orElse(null);

        SfSerializer serializer = new SfSerializer(this);
        serializer.load();

        this.teamContainer.init(this);
        this.castleContainer.init(this);
        this.resourceContainer.init(this);

        this.distributionController = new DistributionController(this);

        SfLanguagesSerializer languagesSerializer = new SfLanguagesSerializer(this);
        languagesSerializer.load();

        ShopSerializer shopSerializer = new ShopSerializer(this);
        shopSerializer.load();

        this.engineManager = new SfEngineManager(this);
        this.commandManager.init();
        this.scoreboards.init();

        new ConnectionListener();

        this.lobby.init();
        this.game.init();
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        this.distributionController.serverStopping();
    }

    public World getWorld()
    {
        return this.world;
    }

    public SfLobby getLobby()
    {
        return this.lobby;
    }

    public SfGame getGame()
    {
        return this.game;
    }

    public SfSettings getSettings()
    {
        return this.settings;
    }

    public void setSettings(SfSettings settings)
    {
        this.settings = settings;
    }

    public SfTeamContainer getTeamContainer()
    {
        return this.teamContainer;
    }

    public SfCastleContainer getCastleContainer()
    {
        return this.castleContainer;
    }

    public SfResourceContainer getResourceContainer()
    {
        return this.resourceContainer;
    }

    public SfEngineManager getEngineManager()
    {
        return this.engineManager;
    }

    public SfScoreboards getScoreboards()
    {
        return this.scoreboards;
    }

    public DistributionController getDistributionController() {
        return this.distributionController;
    }

    public SfLanguages getLanguages()
    {
        return this.languages;
    }

    public SfMessages getMessages()
    {
        return this.messages;
    }

    public static SkyFortressPlugin getInstance()
    {
        return instance;
    }
}
