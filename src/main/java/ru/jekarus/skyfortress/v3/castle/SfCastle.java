package ru.jekarus.skyfortress.v3.castle;

import ru.jekarus.skyfortress.v3.SfSettings;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.Objects;

public class SfCastle {

    private static int ID = 0;
    private int id = ID++;

    private String uniqueId;
    private String teamId;
    private SfCastlePositions positions;

    private SfGameTeam team;

    private int health;
    private boolean alive = true;

    private int deathSeconds;
    private long deathTime = -1;

    public SfCastle()
    {
        SfSettings settings = SkyFortressPlugin.getInstance().getSettings();
        this.health = settings.castle_health;
        this.deathSeconds = settings.castle_death_seconds;
    }

    public SfCastle(String uniqueId, String teamId, SfCastlePositions positions)
    {
        this();
        this.uniqueId = uniqueId;
        this.teamId = teamId;
        this.positions = positions;
    }

    public void init(SkyFortressPlugin plugin)
    {
        this.team = (SfGameTeam) plugin.getTeamContainer().fromUniqueId(this.teamId).orElse(null);
    }

    public SfGameTeam getTeam()
    {
        return this.team;
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getTeamId()
    {
        return this.teamId;
    }

    public void setTeamId(String teamId)
    {
        this.teamId = teamId;
    }

    public SfCastlePositions getPositions()
    {
        return this.positions;
    }

    public void setPositions(SfCastlePositions positions)
    {
        this.positions = positions;
    }

    public int getHealth()
    {
        return this.health;
    }

    public boolean capture(SfScoreboards scoreboards)
    {
        return this.capture(scoreboards,-1);
    }

    public boolean capture(SfScoreboards scoreboards, int value)
    {
        if (this.isCaptured() || !this.isAlive())
        {
            return true;
        }
        this.health += value;
        scoreboards.updateHealth(this.team);
        return this.isCaptured();
    }

    public boolean isCaptured()
    {
        return this.health < 1;
    }

    public void setDeath(boolean needBroadcast)
    {
        this.alive = false;
        SkyFortressPlugin plugin = SkyFortressPlugin.getInstance();

        if (needBroadcast)
        {
            SfMessages messages = plugin.getMessages();
            messages.broadcast(
                    messages.lost(this.getTeam()), true
            );
        }

        this.health = 0;

        SfScoreboards scoreboards = plugin.getScoreboards();
        scoreboards.updateHealth(this.team);
        scoreboards.updateDeath(this.team);

        plugin.getGame().checkWin();
    }

    public boolean isAlive()
    {
        return this.alive;
    }

    public int getDeathSeconds()
    {
        return this.deathSeconds;
    }

    public void setDeathSeconds(SfScoreboards scoreboards, int deathSeconds)
    {
        this.deathSeconds = deathSeconds;
        scoreboards.updateDeathSeconds(this.team);
    }

    public void resetDeathSeconds(SfScoreboards scoreboards)
    {
        this.deathSeconds = 30;
        scoreboards.resetDeathSeconds(this.team);
    }

    public void setDeathTime(long deathTime)
    {
        this.deathTime = deathTime;
    }

    public long getDeathTime()
    {
        return this.deathTime;
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
        SfCastle castle = (SfCastle) o;
        return id == castle.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

}
