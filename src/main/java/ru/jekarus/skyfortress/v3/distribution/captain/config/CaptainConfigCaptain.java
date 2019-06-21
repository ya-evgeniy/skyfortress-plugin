package ru.jekarus.skyfortress.v3.distribution.captain.config;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.Ignore;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;

public class CaptainConfigCaptain {

    @ConfigPath("team_id")
    public String teamId;

    @Ignore
    public SfGameTeam team;

    @ConfigPath("location")
    public SfLocation cell;

    @ConfigPath("changed_blocks") @Generics(SfLocation.class)
    public List<SfLocation> changedBlocks = new ArrayList<>();

}
