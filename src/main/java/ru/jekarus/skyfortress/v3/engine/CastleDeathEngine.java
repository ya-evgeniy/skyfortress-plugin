package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.settings.SettingsContainer;
import ru.jekarus.skyfortress.v3.settings.WorldMapSettings;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CastleDeathEngine {

    private final SkyFortressPlugin plugin;
    private final SfScoreboards scoreboards;

    private Set<SfCastle> castles = new HashSet<>();

    private Task task;
    private boolean enabled = false;

    public CastleDeathEngine(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        scoreboards = plugin.getScoreboards();
    }

    private void start()
    {
        if (this.enabled)
        {
            return;
        }
        this.enabled = true;
        this.task = Task.builder()
                .name(this.getClass().getName())
                .execute(this::run)
                .interval(1, TimeUnit.SECONDS)
                .submit(this.plugin);
    }

    private void stop()
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
        Iterator<SfCastle> iterator = this.castles.iterator();
        while (iterator.hasNext())
        {
            SfCastle castle = iterator.next();
            if (!castle.isAlive())
            {
                iterator.remove();
                continue;
            }
            if (checkOnlinePlayers(this.plugin, castle))
            {
                castle.setShowDeathSeconds(false);
                castle.resetDeathSeconds(this.scoreboards);
                iterator.remove();
            }
            else
            {
                castle.setShowDeathSeconds(true);
                castle.setDeathSeconds(this.scoreboards,castle.getDeathSeconds() - 1);
                if (castle.getDeathSeconds() < 1)
                {
                    castle.setDeath(true);
                    iterator.remove();
                }
            }
        }
        if (this.castles.isEmpty())
        {
            this.stop();
        }
    }

    public void addCastle(SfCastle castle)
    {
        this.castles.add(castle);
        this.start();
    }

    public static boolean checkOnlinePlayers(SkyFortressPlugin plugin, SfCastle castle)
    {
        Collection<PlayerData> offlineDeathPlayers = new ArrayList<>();

        SfGameTeam gameTeam = castle.getTeam();
        final SettingsContainer settings = SkyFortressPlugin.getInstance().getSettings();
        final WorldMapSettings worldMapSettings = settings.getWorldMap();
        for (PlayerData playerData : gameTeam.getPlayers())
        {
            if (playerData.getLastPlayed() == -1)
            {
                return true;
            }
            else
            {
                long playerOfflineDeath = TimeUnit.SECONDS.toMillis(worldMapSettings.getPlayerOfflineDeath());
                if (playerData.getLastPlayed() + playerOfflineDeath < System.currentTimeMillis())
                {
                    offlineDeathPlayers.add(playerData);
                }
            }
        }

        for (PlayerData playerData : offlineDeathPlayers)
        {
            gameTeam.removePlayer(plugin, playerData);
        }

        return false;
    }

    public static void checkCapturedCastle(SkyFortressPlugin plugin, SfCastle castle)
    {
        SfGameTeam gameTeam = castle.getTeam();
        if (CastleDeathEngine.checkOnlinePlayers(plugin, castle))
        {
            return;
        }

        if (!gameTeam.getPlayers().isEmpty())
        {
            SkyFortressPlugin.getInstance().getEngineManager().getCastleDeathEngine().addCastle(castle);
            return;
        }

        castle.setDeath(true);
    }

    public static int getCastlePlace(SkyFortressPlugin plugin) {
        int aliveCastles = 1;
        for (SfCastle castle : plugin.getCastleContainer().getCollection()) {
            if (castle.isAlive()) {
                aliveCastles++;
            }
        }
        return aliveCastles;
    }

}
