package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.*;

public class CaptureEngine {

    private final SkyFortressPlugin plugin;
    private final SfScoreboards scoreboards;

    private Map<SfCastle, Set<SfPlayer>> castleCaptures = new HashMap<>();

    private Task task;
    private boolean enabled = false;

    public CaptureEngine(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.scoreboards = this.plugin.getScoreboards();
    }

    public void start()
    {
        if (this.enabled)
        {
            return;
        }
        this.enabled = true;
        this.task = Task.builder()
                .name(this.getClass().getName())
                .execute(this::run)
                .intervalTicks(1)
                .submit(this.plugin);
    }

    public void stop()
    {
        if (!this.enabled)
        {
            return;
        }
        this.enabled = false;
        this.task.cancel();
    }

    public void addCapture(SfCastle castle, SfPlayer sfPlayer)
    {
        Collection<SfPlayer> players = this.castleCaptures.computeIfAbsent(castle, k -> new HashSet<>());
        if (players.add(sfPlayer))
        {
            SfMessages messages = this.plugin.getMessages();
            messages.broadcast(
                    messages.capture(castle.getTeam(), sfPlayer), true
            );
        }
        this.start();
    }

    private void run()
    {
        boolean needStop = true;
        for (Map.Entry<SfCastle, Set<SfPlayer>> entry : this.castleCaptures.entrySet())
        {
            SfCastle castle = entry.getKey();
            if (castle.isCaptured() || !castle.isAlive())
            {
                continue;
            }
            Iterator<SfPlayer> iterator = entry.getValue().iterator();
            Set<SfPlayer> capturePlayers = new HashSet<>();
            while (iterator.hasNext())
            {
                SfPlayer sfPlayer = iterator.next();
                Optional<Player> optionalPlayer = sfPlayer.getPlayer();
                if (optionalPlayer.isPresent())
                {
                    Player player = optionalPlayer.get();
                    if (!player.isOnline())
                    {
                        iterator.remove();
                        continue;
                    }
                    if (!checkGoldBlock(player))
                    {
                        iterator.remove();
                        continue;
                    }
                    SfCastlePositions positions = castle.getPositions();
                    if (!SfUtils.checkLocation(player, positions.getCapture().getLocation()))
                    {
                        iterator.remove();
                        continue;
                    }
                    capturePlayers.add(sfPlayer);
                }
                else
                {
                    iterator.remove();
                }
            }
            if (!capturePlayers.isEmpty())
            {
                if (castle.capture(this.scoreboards, -capturePlayers.size()))
                {
                    CastleDeathEngine.checkCapturedCastle(this.plugin, castle);

                    SfMessages messages = this.plugin.getMessages();
                    messages.broadcast(
                            messages.captured(castle.getTeam()), true
                    );
                    for (SfPlayer sfPlayer : castle.getTeam().getPlayers())
                    {
                        sfPlayer.getPlayer().ifPresent(player -> {
                            if (player.isOnline())
                            {
                                player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                                    effects.addElement(
                                            PotionEffect.builder().potionType(PotionEffectTypes.STRENGTH).duration(1_000_000).amplifier(0).particles(false).build()
                                    );
                                    player.offer(effects);
                                });
                            }
                        });
                    }
                }
                else
                {
                    needStop = false;
                }
            }
        }
        if (needStop)
        {
            this.stop();
        }
    }

    public static boolean checkGoldBlock(Player player)
    {
        Location<World> location = player.getLocation();
        Location<World> goldBlockLocation = location.getRelative(Direction.DOWN);
        return goldBlockLocation.getBlockType().equals(BlockTypes.GOLD_BLOCK);
    }

}
