package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.List;

public class Captain extends CaptainTarget {

    public SfGameTeam team;

    public Captain(SfPlayer player, SfGameTeam team, SfLocation cell, List<SfLocation> changedBlocks) {
        super(player, cell, changedBlocks);
        this.team = team;
    }

}
