package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;
import java.util.function.Predicate;

public class CheckCaptureEngine {

    private final SkyFortressPlugin plugin;

    private final CaptureEngine captureEngine;
    private final SfTeamContainer teamContainer;
    private final SfCastleContainer castleContainer;

    private final PlayersDataContainer playersData;

    private Task task;
    private boolean enabled = false;

    public CheckCaptureEngine(SkyFortressPlugin plugin, CaptureEngine captureEngine)
    {
        this.plugin = plugin;

        this.captureEngine = captureEngine;
        this.teamContainer = this.plugin.getTeamContainer();
        this.castleContainer = this.plugin.getCastleContainer();

        this.playersData = plugin.getPlayersDataContainer();
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
                .intervalTicks(10)
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

    private void run()
    {
        for (SfGameTeam gameTeam : this.teamContainer.getGameCollection())
        {
            this.tryTeamCapture(gameTeam);
        }
    }

    private void tryTeamCapture(SfGameTeam gameTeam)
    {
        for (PlayerData playerData : gameTeam.getPlayers())
        {
            this.tryPlayerCapture(playerData);
        }
    }

    private void tryPlayerCapture(PlayerData playerData)
    {
        if (playerData.getLastPlayed() != -1)
        {
            return;
        }
        Optional<Player> optionalPlayer = playerData.getPlayer();
        if (optionalPlayer.isPresent())
        {
            Player player = optionalPlayer.get();
            if (player.isOnline())
            {
                if (!CaptureEngine.checkGoldBlock(player))
                {
                    return;
                }
                SfTeam team = playerData.getTeam();
                for (SfCastle castle : this.castleContainer.getCollection())
                {
                    if (!castle.isAlive()) continue;
                    if (tryCastleCapture(castle, playerData, player, team))
                    {
                        return;
                    }
                }
            }
        }
    }

    private boolean tryCastleCapture(SfCastle castle, PlayerData playerData, Player player, SfTeam team) {
        SfCastlePositions positions = castle.getPositions();
        if (SfUtils.checkLocation(player, positions.getCapture().getLocation())) {
            if (castle.getTeam() == team) {
                if (playerData.getCapturePoints() > 0) {
                    final boolean captured = castle.isCaptured();
                    castle.setAdditionalHealth(castle.getAdditionalHealth() + (int) (playerData.getCapturePoints() * 0.20));
                    playerData.setCapturePoints(0);
                    plugin.getScoreboards().updateLeftSeconds(castle.getTeam());

                    if (!captured) return true;

                    for (PlayerData teammatePlayerData : castle.getTeam().getPlayers()) {
                        teammatePlayerData.getPlayer().ifPresent(onlineTeammate -> {
                            onlineTeammate.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                                onlineTeammate.offer(
                                        effects.removeAll(potionEffect -> potionEffect.getType().equals(PotionEffectTypes.STRENGTH))
                                );
                            });
                        });
                    }
                }
            }
            else {
                if (castle.isCaptured()) return true;
                this.captureEngine.addCapture(castle, playerData);
            }
            return true;
        }
        return false;
    }

}
