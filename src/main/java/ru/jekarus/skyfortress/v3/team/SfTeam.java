package ru.jekarus.skyfortress.v3.team;

import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

import java.util.*;

public class SfTeam {

    private static int ID = 0;
    private int id = ID++;

    private String uniqueId;
    private TextColor color;

    private Set<SfPlayer> players = new HashSet<>();

    public SfTeam()
    {

    }

    public SfTeam(String uniqueId, TextColor color)
    {
        this.uniqueId = uniqueId;
        this.color = color;
    }

    public void init(SkyFortressPlugin plugin)
    {

    }

    public Collection<SfPlayer> getPlayers()
    {
        return this.players;
    }

    public void addPlayer(SkyFortressPlugin plugin, SfPlayer player)
    {
        this.players.add(player);
        SfTeam team = player.getTeam();
        if (team != null && team != this)
        {
            team.removePlayer(plugin, player);
        }
        player.setTeam(this);
        plugin.getScoreboards().setTeam(this, player);
    }

    public void removePlayer(SkyFortressPlugin plugin, SfPlayer player)
    {
        this.players.remove(player);
        plugin.getScoreboards().removeTeam(this, player);
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public TextColor getColor()
    {
        return this.color;
    }

    public void setColor(TextColor color)
    {
        this.color = color;
    }

    public Type getType()
    {
        return Type.NONE;
    }

    public int getOrigin()
    {
        return this.id;
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
        SfTeam team = (SfTeam) o;
        return id == team.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    public enum Type {

        NONE,
        GAME,
        SPECTATOR

    }

}
