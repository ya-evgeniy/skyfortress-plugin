package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.scoreboard.CollisionRules;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.HashMap;
import java.util.Map;

public class SfScoreboardTeams {

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard;

    private Map<SfTeam, Team> teamBySfTeam = new HashMap<>();

    public SfScoreboardTeams(SkyFortressPlugin plugin, Scoreboard scoreboard, SfLanguage language)
    {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
    }

    public void init()
    {
        for (SfTeam sfTeam : this.plugin.getTeamContainer().getCollection())
        {
            Team team = Team.builder()
                    .name(sfTeam.getUniqueId())
                    .color(sfTeam.getColor())
                    .allowFriendlyFire(false)
                    .collisionRule(CollisionRules.ALWAYS)
                    .prefix(Text.builder().append(Text.of()).color(sfTeam.getColor()).build())
                    .build();
            this.scoreboard.registerTeam(team);
            this.teamBySfTeam.put(sfTeam, team);
        }
    }

    public void setTeam(SfTeam sfTeam, String name)
    {
        Team team = this.teamBySfTeam.get(sfTeam);
        if (team != null)
        {
            team.addMember(Text.of(name));
        }
    }

    public void removeTeam(SfTeam sfTeam, String name)
    {
        Team team = this.teamBySfTeam.get(sfTeam);
        if (team != null)
        {
            team.removeMember(Text.of(name));
        }
    }

}
