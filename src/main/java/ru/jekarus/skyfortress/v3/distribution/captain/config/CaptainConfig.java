package ru.jekarus.skyfortress.v3.distribution.captain.config;

import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;

public class CaptainConfig {

    public int maxSelectTime = 30;

    public SfLocation mainCaptainCell;

    public List<SfLocation> mainCaptainCellChangedBlocks = new ArrayList<>();

    public List<CaptainConfigCaptain> captains;

    public List<CaptainConfigPlayer> players;

}
