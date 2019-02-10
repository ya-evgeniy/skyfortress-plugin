package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.List;
import java.util.Map;

public class CaptainsState {

    public int max_players = 24;
    public int select_time = 60;

    public SfLocation captain;
    public Map<SfGameTeam, CaptainState> captains;

    public List<PlayerState> players;

}
