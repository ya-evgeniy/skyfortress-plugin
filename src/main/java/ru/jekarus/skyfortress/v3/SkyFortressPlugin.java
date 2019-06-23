package ru.jekarus.skyfortress.v3;

import com.google.inject.Inject;
import lombok.Getter;
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
import ru.jekarus.skyfortress.v3.config.ConfigLoaders;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.engine.SfEngineManager;
import ru.jekarus.skyfortress.v3.game.SfGame;
import ru.jekarus.skyfortress.v3.gui.Shops;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.listener.ConnectionListener;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomsContainer;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.resource.ResourceContainer;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.serializer.ShopSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.SfLanguagesSerializer;
import ru.jekarus.skyfortress.v3.settings.SettingsContainer;
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

    @Getter private static SkyFortressPlugin instance;
    @Getter private World world;

    @Getter private PlayersDataContainer playersDataContainer = new PlayersDataContainer();
    @Getter private LobbyRoomsContainer lobbyRoomsContainer = new LobbyRoomsContainer(this);

    @Getter private SfGame game = new SfGame(this);
    @Getter private SettingsContainer settings = new SettingsContainer();

    @Getter private final SfTeamContainer teamContainer = new SfTeamContainer();
    @Getter private final SfCastleContainer castleContainer = new SfCastleContainer();
    @Getter private final ResourceContainer resourceContainer = new ResourceContainer();

    @Getter private SfEngineManager engineManager;
    @Getter private SfCommandManager commandManager = new SfCommandManager(this);

    @Getter private final SfScoreboards scoreboards = new SfScoreboards(this);

    @Getter private DistributionController distributionController;

    @Getter private final SfLanguages languages = new SfLanguages();
    @Getter private final SfMessages messages = new SfMessages(this);

    @Getter private final Shops shops = new Shops();

    @Inject
    private Logger logger;

    @Listener
    public void onConstruction(GameConstructionEvent event) {
        instance = this;
    }

    @Listener
    public void onPostInit(GameStartedServerEvent event) {
        Server server = Sponge.getServer();
        this.world = server.getWorld(server.getDefaultWorldName()).orElse(null);

        final ConfigLoaders configLoaders = new ConfigLoaders(this);
        configLoaders.load();

        this.castleContainer.init(this);
        this.teamContainer.init(this);
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

        this.getLobbyRoomsContainer().init();
        this.game.init();
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        this.distributionController.serverStopping();
    }

}
