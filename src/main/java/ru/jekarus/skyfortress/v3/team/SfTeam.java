package ru.jekarus.skyfortress.v3.team;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;

import java.util.HashSet;
import java.util.Set;

public class SfTeam {

    @Getter @Setter @NonNull private Type type;
    @Getter @NonNull private String uniqueId;
    @Getter @NonNull private TextColor color;

    @Getter private Set<PlayerData> players = new HashSet<>();

    public SfTeam(@NonNull String uniqueId, @NonNull TextColor color) {
        this(Type.NONE, uniqueId, color);
    }

    public SfTeam(@NonNull Type type, @NonNull String uniqueId, @NonNull TextColor color) {
        this.type = type;
        this.uniqueId = uniqueId;
        this.color = color;
    }

    public void addPlayer(SkyFortressPlugin plugin, PlayerData player) {
        SfTeam team = player.getTeam();
        if (this == team) {
            return;
        }
        this.players.add(player);
        if (team != null) {
            team.removePlayer(plugin, player);
        }
        player.setTeam(this);
        plugin.getScoreboards().setTeam(this, player);
    }

    public void removePlayer(SkyFortressPlugin plugin, PlayerData player) {
        this.players.remove(player);
        plugin.getScoreboards().removeTeam(this, player);
    }

    public enum Type {
        NONE,
        GAME,
        SPECTATOR
    }

}
