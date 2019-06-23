package ru.jekarus.skyfortress.v3.player;

import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayersDataContainer {

    private static final PlayersDataContainer instance = new PlayersDataContainer();

    private Map<UUID, PlayerData> players = new HashMap<>();

    public PlayerData getOrCreatePlayer(Player player) {
        PlayerData playerData = this.players.get(player.getUniqueId());
        if (playerData == null) {
            playerData = new PlayerData(player);
            this.players.put(player.getUniqueId(), playerData);
            return playerData;
        }
        else {
            return playerData;
        }
    }

    public Optional<PlayerData> getPlayer(UUID uniqueId) {
        return Optional.ofNullable(this.players.get(uniqueId));
    }

    public Optional<PlayerData> getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

    public static PlayersDataContainer getInstance() {
        return instance;
    }

    public void remove(UUID uniqueId) {
        this.players.remove(uniqueId);
    }

    public void remove(Player player) {
        this.remove(player.getUniqueId());
    }

    public List<PlayerData> asList() {
        return new ArrayList<>(this.players.values());
    }
}