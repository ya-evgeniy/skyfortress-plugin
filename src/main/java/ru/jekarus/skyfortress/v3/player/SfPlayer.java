package ru.jekarus.skyfortress.v3.player;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SfPlayer {

    @NonNull @Getter private final UUID uniqueId;
    @NonNull @Getter private final String name;

    private WeakReference<Player> playerReference = new WeakReference<>(null);

    @Getter @Setter private SfTeam team = null;
    @NonNull @Getter @Setter private Locale locale = null;

    @NonNull @Getter @Setter private PlayerZone zone = PlayerZone.LOBBY;
    @Getter @Setter private long lastPlayed = -1;

    public long captureMessageTime = 0;

    public SfPlayer(@NonNull Player player) {
        this(player.getUniqueId(), player.getName());
        this.playerReference = new WeakReference<>(player);
    }

    public SfPlayer(@NonNull UUID uniqueId, @NonNull String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public Optional<Player> getPlayer() {
        final Player player = this.playerReference.get();
        if (player != null && !player.isRemoved()) {
            return Optional.of(player);
        }
        final Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(this.uniqueId);
        if (optionalPlayer.isPresent()) {
            this.playerReference = new WeakReference<>(optionalPlayer.get());
        }
        else {
            this.playerReference.clear();
        }
        return optionalPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SfPlayer sfPlayer = (SfPlayer) o;
        return Objects.equals(uniqueId, sfPlayer.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }

    @Override
    public String toString() {
        return String.format("SfPlayer{uniqueId=%s, name='%s'}", uniqueId, name);
    }
}