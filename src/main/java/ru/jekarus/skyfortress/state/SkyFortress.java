package ru.jekarus.skyfortress.state;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.module.SfSidebar;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyFortress {

    private boolean gameStarted = false;
    private final Map<SfTeam, Boolean> ready = new EnumMap<>(SfTeam.class);
    private final Map<SfTeam, SfTeamState> teamsState = new EnumMap<>(SfTeam.class);
    private final Map<UUID, SfPlayerState> playersState = new HashMap<>();

    private @NotNull PluginManager pluginMan;
    private @NotNull Scoreboard scoreboard;

    public SkyFortress() {
    }

    public void init() {
        pluginMan = Bukkit.getServer().getPluginManager();
        scoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
    }

    public SfTeamState getTeamState(SfTeam sft) {
        return this.teamsState.computeIfAbsent(sft, v -> new SfTeamState());
    }

    public SfPlayerState getPlayerState(OfflinePlayer player) {
        return this.playersState.computeIfAbsent(player.getUniqueId(), v -> new SfPlayerState());
    }

    public void gameStart() {
        gameStart(false);
    }
    public void gameStart(boolean noTp) {
        if(gameStarted) return;
        gameStarted = true;
        pluginMan.callEvent(new GameStartEvent());
        if(!noTp) {
            for (SfTeam sft : SfTeam.values()) {
                for (Player player : sft.players()) {
                    sft.spawn.teleport(player, sft.face);
                }
            }
        }
        SfSidebar.updateAll();
    }

    public void gameStop() {
        if(!gameStarted) return;
        gameStarted = false;
        pluginMan.callEvent(new GameStopEvent());
        SfSidebar.updateAll();
    }

    public boolean isReady(SfTeam sft) {
        return this.ready.getOrDefault(sft, false);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void toggleReady(SfTeam sft) {
        boolean ready = this.ready.getOrDefault(sft, false);
        ready = !ready;
        this.ready.put(sft, ready);
        if(this.isAllReady()) {
            gameStart();
        }
    }

    private boolean isAllReady() {
        for(SfTeam sft : SfTeam.values()) {
            if(!isReady(sft)) return false;
        }
        return true;
    }

    public void playerJoin(SfTeam sft, OfflinePlayer player) {
        sft.team().addPlayer(player);
        this.getPlayerState(player).team = sft;
    }

    public void playerLeave(Player player) {
        final var team = scoreboard.getPlayerTeam(player);
        if (team == null) return;
        team.removePlayer(player);
        this.getPlayerState(player).team = null;
    }
}
