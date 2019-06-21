package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SfScoreboards {

    private Map<Locale, SfScoreboard> scoreboardsByLocale = new HashMap<>();

    private final SkyFortressPlugin plugin;

    public SfScoreboards(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void init()
    {
        Map<Locale, SfLanguage> languages = this.plugin.getLanguages().getLanguageByLocale();
        for (Map.Entry<Locale, SfLanguage> entry : languages.entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage value = entry.getValue();

            SfScoreboard scoreboard = new SfScoreboard(this.plugin, value);
            this.scoreboardsByLocale.put(
                    locale,
                    scoreboard
            );
        }

        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.init();
        }
    }

    public void updateLeftSeconds(SfGameTeam team)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getInGameObjective().updateLeftSeconds(team);
        }
    }

    public void updateDeath(SfGameTeam team)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getInGameObjective().updateDeath(team);
        }
    }

    public void setTeam(SfTeam sfTeam, SfPlayer player)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getTeams().setTeam(sfTeam, player.getName());
        }
    }

    public void removeTeam(SfTeam sfTeam, SfPlayer player)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getTeams().setTeam(sfTeam, player.getName());
        }
    }

    public void setReady(SfGameTeam team)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getPreGameObjective().setReady(team);
        }
    }

    public void setUnready(SfGameTeam team)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getPreGameObjective().setUnready(team);
        }
    }

    public void clearSideBar()
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.clearSideBar();
        }
    }

    public void setSideBar(Types type)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.setSideBar(type);
        }
    }

    public void setFor(SfPlayer sfPlayer, Player player)
    {
        Locale locale = sfPlayer.getLocale();
        SfScoreboard scoreboard = this.scoreboardsByLocale.get(locale);
        if (scoreboard != null)
        {
            scoreboard.setFor(player);
        }
        else
        {
            StringBuilder builder = new StringBuilder("Available locales: ");
            for (Locale l : this.scoreboardsByLocale.keySet())
            {
                builder.append(l).append(" ");
            }
        }
    }

    public void resetTeam(SfGameTeam team)
    {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getInGameObjective().resetTeam(team);
        }
    }

    public void updatePlaces() {
        for (SfScoreboard scoreboard : this.scoreboardsByLocale.values())
        {
            scoreboard.getPostGameObjective().updatePlaces();
        }
    }

    public List<SfScoreboard> asList() {
        return new ArrayList<>(this.scoreboardsByLocale.values());
    }

    public enum Types
    {
        PRE_GAME,
        IN_GAME,
        POST_GAME
    }

}
