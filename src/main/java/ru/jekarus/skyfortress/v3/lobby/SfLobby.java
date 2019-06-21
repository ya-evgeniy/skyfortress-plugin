package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;

public class SfLobby {

    private final SkyFortressPlugin plugin;

    private List<SfLobbyTeam> teams = new ArrayList<>();

    public SfLobby(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        for (SfLobbyTeam team : this.teams) {
            team.init(this.plugin);
        }
    }

    public void add(SfLobbyTeam team) {
        this.teams.add(team);
    }

    public void standOnPlate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot) {
        for (SfLobbyTeam team : this.teams) {
            if (team.standOnPlate(player, sfPlayer, snapshot)) {
                return;
            }
        }
    }

    public void pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot) {
        for (SfLobbyTeam team : this.teams) {
            if (team.pressButton(player, sfPlayer, snapshot)) {
                return;
            }
        }
    }

    public void playerDisconnect(SfPlayer sfPlayer, Player player) {
        for (SfLobbyTeam team : this.teams) {
            team.playerDisconnect(sfPlayer, player);
        }
    }

    public void checkStart() {
        if (this.checkAllReady()) {
            this.plugin.getGame().start();
        }
    }

    public boolean checkAllReady() {
        boolean allReady = true;
        for (SfLobbyTeam team : this.teams) {
            allReady = allReady && team.getSettings().ready;
        }
        return allReady;
    }

    public List<SfLobbyTeam> getTeams() {
        return this.teams;
    }

    public void clearWaitingPlayers() {
        for (SfLobbyTeam team : this.teams) {
            team.getSettings().waitingPlayer = null;
        }
    }

    public void moveToLobby(SfPlayer sfPlayer) {
        sfPlayer.setZone(PlayerZone.LOBBY);
        sfPlayer.getPlayer().ifPresent(player -> {
            SfLocation center = this.plugin.getSettings().getLobby().getCenter();
            player.setLocationAndRotation(
                    center.getLocation(),
                    center.getRotation()
            );
        });
    }
}