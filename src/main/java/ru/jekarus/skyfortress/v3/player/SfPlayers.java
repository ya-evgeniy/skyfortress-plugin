package ru.jekarus.skyfortress.v3.player;

import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SfPlayers {

    private static final SfPlayers instance = new SfPlayers();

    private Map<UUID, SfPlayer> players = new HashMap<>();

    public SfPlayer getOrCreatePlayer(Player player)
    {
        SfPlayer sfPlayer = this.players.get(player.getUniqueId());
        if (sfPlayer == null)
        {
            sfPlayer = new SfPlayer(player);
            this.players.put(player.getUniqueId(), sfPlayer);
            return sfPlayer;
        }
        else
        {
            return sfPlayer;
        }
    }

    public Optional<SfPlayer> getPlayer(UUID uniqueId)
    {
        return Optional.ofNullable(this.players.get(uniqueId));
    }

    public Optional<SfPlayer> getPlayer(Player player)
    {
        return this.getPlayer(player.getUniqueId());
    }

    public static SfPlayers getInstance()
    {
        return instance;
    }

    public void remove(UUID uniqueId)
    {
        this.players.remove(uniqueId);
    }

    public void remove(Player player)
    {
        this.remove(player.getUniqueId());
    }
}
