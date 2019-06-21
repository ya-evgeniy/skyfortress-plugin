package ru.jekarus.skyfortress.v3.distribution.captain.config;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;

public class CaptainConfigPlayer {

    @ConfigPath("location")
    public SfLocation cell;

    @ConfigPath("changed_blocks") @Generics(SfLocation.class)
    public List<SfLocation> changedBlocks = new ArrayList<>();

}
