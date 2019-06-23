package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Optional;

public class CheckCaptureEngine {

    private final SkyFortressPlugin plugin;

    private final CaptureEngine captureEngine;
    private final SfTeamContainer teamContainer;
    private final SfCastleContainer castleContainer;

    private final SfPlayers players;

    private Task task;
    private boolean enabled = false;

    public CheckCaptureEngine(SkyFortressPlugin plugin, CaptureEngine captureEngine)
    {
        this.plugin = plugin;

        this.captureEngine = captureEngine;
        this.teamContainer = this.plugin.getTeamContainer();
        this.castleContainer = this.plugin.getCastleContainer();

        this.players = SfPlayers.getInstance();
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
                    if (castle.isCaptured())
                    {
                        continue;
                    }
                    if (tryCastleCapture(castle, playerData, player, team))
                    {
                        return;
                    }
                }
            }
        }
    }

    private boolean tryCastleCapture(SfCastle castle, PlayerData playerData, Player player, SfTeam team)
    {
        if (castle.getTeam() == team)
        {
            return false;
        }
        SfCastlePositions positions = castle.getPositions();
        if (SfUtils.checkLocation(player, positions.getCapture().getLocation()))
        {
            this.captureEngine.addCapture(castle, playerData);
            return true;
        }
        return false;
    }

}
