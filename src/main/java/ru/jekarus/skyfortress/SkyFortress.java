package ru.jekarus.skyfortress;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.module.SfSidebar;
import ru.jekarus.skyfortress.state.SfPlayerState;
import ru.jekarus.skyfortress.state.SfTeamState;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyFortress {

    private boolean gameStarted = false;
    private final Map<SfTeam, Boolean> ready = new EnumMap<>(SfTeam.class);
    private final Map<SfTeam, SfTeamState> teamsState = new EnumMap<>(SfTeam.class);
    private final Map<UUID, SfPlayerState> playersState = new HashMap<>();

    public SfTeamState getTeamState(SfTeam sft) {
        return this.teamsState.computeIfAbsent(sft, v -> new SfTeamState());
    }

    public SfPlayerState getPlayerState(OfflinePlayer player) {
        return this.playersState.computeIfAbsent(player.getUniqueId(), v -> new SfPlayerState());
    }

    public void gameStart() {
        if(gameStarted) return;
        gameStarted = true;
        for (SfTeam sft : SfTeam.values()) {
            for (Player player : sft.players()) {
                sft.spawn.teleport(player, sft.face);
            }
        }
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
}
