package ru.jekarus.skyfortress.v3.distribution.captain.config;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.ArrayList;
import java.util.List;

public class CaptainConfigPlayer {

    @ConfigPath("location")
    public LocationAndRotation cell;

    @ConfigPath("changed_blocks") @Generics(LocationAndRotation.class)
    public List<LocationAndRotation> changedBlocks = new ArrayList<>();

}
