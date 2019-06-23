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
    private Map<UUID, PlayerData> dataByUniqueId = new HashMap<>();

    public PlayerData getOrCreateData(Player player) {
        PlayerData playerData = this.dataByUniqueId.get(player.getUniqueId());
        if (playerData == null) {
            playerData = new PlayerData(player);
            this.dataByUniqueId.put(player.getUniqueId(), playerData);
            return playerData;
        }
        else {
            return playerData;
        }
    }


    public Optional<PlayerData> get(UUID uniqueId) {
        return Optional.ofNullable(this.dataByUniqueId.get(uniqueId));
    }

    public Optional<PlayerData> get(Player player) {
        return this.get(player.getUniqueId());
    }

    public static PlayersDataContainer getInstance() {
        return instance;
    }

    public void remove(UUID uniqueId) {
        this.dataByUniqueId.remove(uniqueId);
    }

    public void remove(Player player) {
        this.remove(player.getUniqueId());
    }

    public List<PlayerData> asList() {
        return new ArrayList<>(this.dataByUniqueId.values());
    }

}