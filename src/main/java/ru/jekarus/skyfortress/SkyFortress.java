package ru.jekarus.skyfortress;

import org.bukkit.entity.Player;
import ru.jekarus.skyfortress.config.SfTeam;

import java.util.EnumMap;
import java.util.Map;

public class SkyFortress {

    private boolean gameStarted = false;
    private final Map<SfTeam, Boolean> ready = new EnumMap<>(SfTeam.class);
    private final Map<SfTeam, SfTeamState> state = new EnumMap<>(SfTeam.class);

    public SfTeamState getState(SfTeam sft) {
        var state = this.state.get(sft);
        if(state == null) {
            state = new SfTeamState();
            this.state.put(sft, state);
        }
        return state;
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
