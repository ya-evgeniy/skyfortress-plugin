package ru.jekarus.skyfortress.v3.lang.scoreboard;

import jekarus.hocon.config.serializer.annotation.ConfigPath;

public class SfScoreboardLanguage {

    @ConfigPath("pre_game")
    public SfPreGameScoreboardLanguage preGame = new SfPreGameScoreboardLanguage();

    @ConfigPath("in_game")
    public SfInGameScoreboardLanguage inGame = new SfInGameScoreboardLanguage();

    @ConfigPath("post_game")
    public SfPostGameScoreboardLanguage postGame = new SfPostGameScoreboardLanguage();

}
