package ru.jekarus.skyfortress.v3.distribution.captain.config;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.List;

@ConfigPath("captain_system")
public class CaptainConfig {

    @ConfigPath("select_time")
    public int maxSelectTime;

    @ConfigPath("captain.location")
    public LocationAndRotation mainCaptainCell;

    @ConfigPath("captain.changed_blocks") @Generics(LocationAndRotation.class)
    public List<LocationAndRotation> mainCaptainCellChangedBlocks;

    @Generics(CaptainConfigCaptain.class)
    public List<CaptainConfigCaptain> captains;

    @Generics(CaptainConfigPlayer.class)
    public List<CaptainConfigPlayer> players;

}
