package ru.jekarus.skyfortress.v3.lobby;

import lombok.Getter;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

import java.util.ArrayList;
import java.util.List;

public class LobbyRoomsContainer {

    private final SkyFortressPlugin plugin;
    @Getter private List<LobbyRoom> rooms = new ArrayList<>();

    public LobbyRoomsContainer(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        for (LobbyRoom room : this.rooms) {
            room.init();
        }
    }

    public void clearWaitingPlayers() {
        for (LobbyRoom room : this.rooms) {
            final SfPlayer waitingPlayer = room.getState().getWaitingPlayer();
            if (waitingPlayer != null) {
                room.getMovement().moveToLobby(null, waitingPlayer);
                room.getState().setWaitingPlayer(null);
            }
        }
    }

    public void playerDisconnect(Player player, SfPlayer playerData) {

    }
}
