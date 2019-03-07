package ru.jekarus.skyfortress.v3.distribution.captain.config;

import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;

public class CaptainConfigCaptain {

    public String teamId;

    public SfGameTeam team;

    public SfLocation cell;

    public List<SfLocation> changedBlocks = new ArrayList<>();



}
