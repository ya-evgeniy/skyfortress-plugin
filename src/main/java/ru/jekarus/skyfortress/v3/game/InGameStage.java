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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.engine.SfEngineManager;
import ru.jekarus.skyfortress.v3.listener.*;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class InGameStage extends SfGameStage {

    private final SkyFortressPlugin plugin;

    private final ChangeBlockListener changeBlockListener;
    private final DropItemListener dropItemListener;
    private final PlayerInteractListener playerInteractListener;
    private final PlayerDeathListener playerDeathListener;
    private final PlayerRespawnListener playerRespawnListener;

    public InGameStage(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

        this.changeBlockListener = new ChangeBlockListener(this.plugin);
        this.dropItemListener = new DropItemListener(this.plugin);
        this.playerInteractListener = new PlayerInteractListener(this.plugin);
        this.playerDeathListener = new PlayerDeathListener(this.plugin);
        this.playerRespawnListener = new PlayerRespawnListener(this.plugin);
    }

    @Override
    public void enable()
    {
        this.movePlayersToGame();

        this.plugin.getScoreboards().setSideBar(SfScoreboards.Types.IN_GAME);

        SfEngineManager engineManager = this.plugin.getEngineManager();
        engineManager.getCheckCaptureEngine().start();
        engineManager.getResourcesEngine().start();

        this.spawnEntityShops();

        this.changeBlockListener.register();
        this.dropItemListener.register();
        this.playerInteractListener.register();
        this.playerDeathListener.register();
        this.playerRespawnListener.register();

        castle_for: for (SfCastle castle : this.plugin.getCastleContainer().getCollection())
        {
            for (SfPlayer player : castle.getTeam().getPlayers())
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
        engineManager.getResourcesEngine().stop();

        this.changeBlockListener.unregister();
        this.dropItemListener.unregister();
        this.playerInteractListener.unregister();
        this.playerDeathListener.unregister();
        this.playerRespawnListener.unregister();
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
            Collection<SfPlayer> offlinePlayers = new ArrayList<>();
            for (SfPlayer sfPlayer : gameTeam.getPlayers())
            {
                if (sfPlayer.getLastPlayed() != -1)
                {
                    offlinePlayers.add(sfPlayer);
                }
                else
                {
                    Optional<Player> optionalPlayer = sfPlayer.getPlayer();
                    if (optionalPlayer.isPresent())
                    {
                        Player player = optionalPlayer.get();
                        SfCastlePositions positions = gameTeam.getCastle().getPositions();
                        player.setLocationAndRotation(
                                positions.getRespawn().getLocation(),
                                positions.getRespawn().getRotation()
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
                    else
                    {
                        offlinePlayers.add(sfPlayer);
                    }
                }
            }
            for (SfPlayer sfPlayer : offlinePlayers)
            {
                teamContainer.getNoneTeam().addPlayer(this.plugin, sfPlayer);
            }
        }
    }

    private void spawnEntityShops()
    {
        SfCastleContainer castleContainer = this.plugin.getCastleContainer();
        for (SfCastle castle : castleContainer.getCollection())
        {
            for (SfLocation location : castle.getPositions().getShops())
            {
                this.spawnEntityShop(location);
            }
        }
    }

    private void spawnEntityShop(SfLocation location)
    {
        Location<World> position = location.getLocation();
        Living entity = (Living) position.createEntity(EntityTypes.ILLUSION_ILLAGER);

        entity.setHeadRotation(location.getRotation());

        entity.offer(Keys.AI_ENABLED, false);
        entity.offer(Keys.INVULNERABLE, true);
        entity.offer(Keys.HAS_GRAVITY, false);
        entity.offer(Keys.IS_SILENT, false);

        position.getExtent().loadChunk(position.getChunkPosition(), false);
        position.spawnEntity(entity);

    }

}
