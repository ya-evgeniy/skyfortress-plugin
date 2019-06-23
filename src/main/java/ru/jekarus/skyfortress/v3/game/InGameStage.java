package ru.jekarus.skyfortress.v3.game;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.engine.SfEngineManager;
import ru.jekarus.skyfortress.v3.listener.ArrowMechanicsListener;
import ru.jekarus.skyfortress.v3.listener.ChangeBlockListener;
import ru.jekarus.skyfortress.v3.listener.DropItemListener;
import ru.jekarus.skyfortress.v3.listener.FriendFireListener;
import ru.jekarus.skyfortress.v3.listener.PlayerDeathListener;
import ru.jekarus.skyfortress.v3.listener.PlayerInteractListener;
import ru.jekarus.skyfortress.v3.listener.PlayerRespawnListener;
import ru.jekarus.skyfortress.v3.listener.ResourceListener;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboard;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InGameStage extends SfGameStage {

    private final SkyFortressPlugin plugin;

    private final ChangeBlockListener changeBlockListener;
    private final DropItemListener dropItemListener;
    private final PlayerInteractListener playerInteractListener;
    private final PlayerDeathListener playerDeathListener;
    private final PlayerRespawnListener playerRespawnListener;
    private final ResourceListener resourceListener;
    private final ArrowMechanicsListener arrowMechanicsListener;
    private final FriendFireListener friendFireListener;

    public InGameStage(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

        this.changeBlockListener = new ChangeBlockListener(this.plugin);
        this.dropItemListener = new DropItemListener(this.plugin);
        this.playerInteractListener = new PlayerInteractListener(this.plugin);
        this.playerDeathListener = new PlayerDeathListener(this.plugin);
        this.playerRespawnListener = new PlayerRespawnListener(this.plugin);
        this.resourceListener = new ResourceListener(this.plugin);
        this.arrowMechanicsListener = new ArrowMechanicsListener(this.plugin);
        this.friendFireListener = new FriendFireListener(this.plugin);
    }

    @Override
    public void enable()
    {
        this.movePlayersToGame();

        this.plugin.getScoreboards().setSideBar(SfScoreboards.Types.IN_GAME);

        SfEngineManager engineManager = this.plugin.getEngineManager();
        engineManager.getCheckCaptureEngine().start();
//        engineManager.getResourcesEngine().start();
        engineManager.getResourcesEngine().start();

        this.spawnEntityShops();

        this.changeBlockListener.register();
        this.dropItemListener.register();
        this.playerInteractListener.register();
        this.playerDeathListener.register();
        this.playerRespawnListener.register();
        this.resourceListener.register();
        this.arrowMechanicsListener.register();
        this.friendFireListener.register();

        castle_for: for (SfCastle castle : this.plugin.getCastleContainer().getCollection())
        {
            for (PlayerData player : castle.getTeam().getPlayers())
            {
                if (player.getLastPlayed() == -1)
                {
                    continue castle_for;
                }
            }
            castle.setDeath(false);
            this.plugin.getScoreboards().resetTeam(castle.getTeam());
        }
    }

    @Override
    public void disable()
    {
        SfEngineManager engineManager = this.plugin.getEngineManager();
        engineManager.getCheckCaptureEngine().stop();
//        engineManager.getResourcesEngine().stop();
        engineManager.getResourcesEngine().stop();

        this.changeBlockListener.unregister();
        this.dropItemListener.unregister();
        this.playerInteractListener.unregister();
        this.playerDeathListener.unregister();
        this.playerRespawnListener.unregister();
        this.resourceListener.unregister();
        this.arrowMechanicsListener.unregister();
        this.friendFireListener.unregister();
    }

    @Override
    public SfGameStageType getType()
    {
        return SfGameStageType.IN_GAME;
    }

    private void movePlayersToGame()
    {
        SfTeamContainer teamContainer = this.plugin.getTeamContainer();
        for (SfGameTeam gameTeam : teamContainer.getGameCollection())
        {
            List<SfScoreboard> scoreboards = this.plugin.getScoreboards().asList();
            List<Team> scoreboardTeams = new ArrayList<>();

            for (SfScoreboard scoreboard : scoreboards) {
                scoreboard.getTeams().getTeam(gameTeam).ifPresent(scoreboardTeams::add);
            }

            for (PlayerData playerData : gameTeam.getPlayers())
            {
                scoreboardTeams.forEach(team -> team.addMember(Text.of(playerData.getName())));
                Optional<Player> optionalPlayer = playerData.getPlayer();
                if (optionalPlayer.isPresent())
                {
                    Player player = optionalPlayer.get();
                    setupPlayer(playerData, player);
                }
            }
        }
    }

    public void setupPlayer(PlayerData playerData, Player player) {
        if (playerData.getTeam().getType() != SfTeam.Type.GAME) {
            return;
        }
        SfGameTeam team = (SfGameTeam) playerData.getTeam();
        playerData.setZone(PlayerZone.GAME);

        SfCastlePositions positions = team.getCastle().getPositions();
        player.setLocationAndRotation(
                positions.getRespawn().get(0).getLocation(), // fixme get(0)
                positions.getRespawn().get(0).getRotation() // fixme get(0)
        );

        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
        player.offer(Keys.FOOD_LEVEL, 20);
        player.offer(Keys.SATURATION, 20.0);
        player.offer(Keys.HEALTH, 20.0);
        player.offer(Keys.POTION_EFFECTS, new ArrayList<>());
        player.getInventory().clear();
        Inventory hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
        hotbar.offer(ItemStack.of(ItemTypes.IRON_INGOT, 1));
    }

    private void spawnEntityShops()
    {
        SfCastleContainer castleContainer = this.plugin.getCastleContainer();
        for (SfCastle castle : castleContainer.getCollection())
        {
            for (LocationAndRotation location : castle.getPositions().getShops())
            {
                this.spawnEntityShop(location);
            }
        }
    }

    private void spawnEntityShop(LocationAndRotation location)
    {
        Location<World> position = location.getLocation();
        Living entity = (Living) position.createEntity(EntityTypes.ILLUSION_ILLAGER);

        entity.setHeadRotation(location.getRotation());

        entity.offer(Keys.AI_ENABLED, false);
        entity.offer(Keys.INVULNERABLE, true);
        entity.offer(Keys.HAS_GRAVITY, false);
        entity.offer(Keys.IS_SILENT, true);

        position.getExtent().loadChunk(position.getChunkPosition(), false);
        position.spawnEntity(entity);

    }

}
