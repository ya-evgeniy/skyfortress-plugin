package ru.jekarus.skyfortress.v3.player;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.translation.locale.Locales;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SfPlayer {

    private final UUID uniqueId;
    private final String name;

    private SfTeam team = null;
    private Locale locale = Locales.RU_RU;

    private long lastPlayed = -1;

    public SfPlayer(Player player)
    {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
    }

    public UUID getUniqueId()
    {
        return this.uniqueId;
    }

    public String getName()
    {
        return this.name;
    }

    public SfTeam getTeam()
    {
        return this.team;
    }

    public void setTeam(SfTeam team)
    {
        this.team = team;
    }

    public long getLastPlayed()
    {
        return this.lastPlayed;
    }

    public void setLastPlayed(long lastPlayed)
    {
        this.lastPlayed = lastPlayed;
    }

    public Optional<Player> getPlayer()
    {
        return Sponge.getServer().getPlayer(this.uniqueId);
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        SfPlayer sfPlayer = (SfPlayer) o;
        return Objects.equals(uniqueId, sfPlayer.uniqueId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uniqueId);
    }

}
