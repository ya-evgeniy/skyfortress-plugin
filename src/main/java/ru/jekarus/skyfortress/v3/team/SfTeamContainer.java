package ru.jekarus.skyfortress.v3.team;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.*;

public class SfTeamContainer {

    private Map<String, SfTeam> teamById = new HashMap<>();

    private SfTeam noneTeam;
    private SfSpectatorTeam spectatorTeam;

    public SfTeamContainer()
    {

    }

    public void init(SkyFortressPlugin plugin)
    {
        for (SfTeam team : this.teamById.values())
        {
            team.init(plugin);
        }
    }

    public void add(SfTeam team)
    {
        this.teamById.put(team.getUniqueId(), team);
        if (team.getType() == SfTeam.Type.NONE)
        {
            this.noneTeam = team;
        }
        else if (team.getType() == SfTeam.Type.SPECTATOR)
        {
            this.spectatorTeam = (SfSpectatorTeam) team;
        }
    }

    public void removeCastle(SfTeam team)
    {
        this.teamById.remove(team.getUniqueId());
    }

    public Optional<SfTeam> fromUniqueId(String uniqueId)
    {
        return Optional.ofNullable(this.teamById.get(uniqueId));
    }

    public Collection<SfTeam> getCollection()
    {
        return this.teamById.values();
    }

    public Collection<SfGameTeam> getGameCollection()
    {
        Collection<SfGameTeam> result = new ArrayList<>();
        for (SfTeam team : this.teamById.values())
        {
            if (team.getType() == SfTeam.Type.GAME)
            {
                result.add((SfGameTeam) team);
            }
        }
        return result;
    }

    public SfTeam getNoneTeam()
    {
        return this.noneTeam;
    }

    public SfSpectatorTeam getSpectatorTeam()
    {
        return this.spectatorTeam;
    }

}
