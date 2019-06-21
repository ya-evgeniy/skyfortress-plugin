package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CaptainsState {

    public boolean started = false;

    public int maxCaptains = 1;

    public Map<SfGameTeam, Captain> captainByTeam = new HashMap<>();

    public Iterator<Captain> captainsIterator = Collections.emptyIterator();

    public Map<UUID, CaptainTarget> targetByPlayerUniqueId = new HashMap<>();

    public Map<UUID, CaptainTarget> targetByEntityUniqueId = new HashMap<>();

    public List<CaptainTarget> unselectedTargets = new ArrayList<>();

}
