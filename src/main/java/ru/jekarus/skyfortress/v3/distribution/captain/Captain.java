package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.List;

public class Captain extends CaptainTarget {

    public SfGameTeam team;

    public Captain(SfPlayer player, SfGameTeam team, LocationAndRotation cell, List<LocationAndRotation> changedBlocks) {
        super(player, cell, changedBlocks);
        this.team = team;
    }

}
