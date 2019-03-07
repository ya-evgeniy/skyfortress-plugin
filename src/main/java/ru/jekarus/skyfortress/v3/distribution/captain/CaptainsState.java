package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.*;

public class CaptainsState {

    public boolean started = false;

    public int maxCaptains = 1;

    public Map<SfGameTeam, Captain> captainByTeam = new HashMap<>();

    public Iterator<Captain> captainsIterator = Collections.emptyIterator();

    public Map<UUID, CaptainTarget> targetByPlayerUniqueId = new HashMap<>();

    public Map<UUID, CaptainTarget> targetByEntityUniqueId = new HashMap<>();

    public List<CaptainTarget> unselectedTargets = new ArrayList<>();

}
