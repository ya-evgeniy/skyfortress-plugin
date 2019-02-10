package ru.jekarus.skyfortress.v3.lang;

import ru.jekarus.skyfortress.v3.lang.messages.SfMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfScoreboardLanguage;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SfLanguage {

    public Locale locale;

    public SfMapLanguage map = new SfMapLanguage();
    public Map<SfTeam, SfTeamLanguage> teams = new HashMap<>();
    public SfScoreboardLanguage scoreboard = new SfScoreboardLanguage();
    public SfMessagesLanguage messages = new SfMessagesLanguage();

}
