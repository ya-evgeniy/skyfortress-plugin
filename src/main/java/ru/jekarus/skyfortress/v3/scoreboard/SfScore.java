package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SfScore {

    public static final String MINECRAFT_COLOR_CODE = "§";
    public static final char[] CHARS = {'a','b','c','d','e','f','g','h','i','j','o','p','q','r','s','t','u','v','w','x','y','z'};

    private final String originalObjectivePrefix;
    private final String objectivePrefix;
    private Score score;
    private Team team;

    public SfScore(String objectivePrefix)
    {
        this.originalObjectivePrefix = objectivePrefix;
        StringBuilder builder = new StringBuilder();
        for (char c : objectivePrefix.toCharArray())
        {
            builder.append(MINECRAFT_COLOR_CODE).append(c);
        }
        this.objectivePrefix = builder.toString();
    }

    public void createScore(Objective objective, int index)
    {
        this.createScore(objective, index, index);
    }

    public void createScore(Objective objective, int index, int value)
    {
        if (this.score != null)
        {
            return;
        }
        int correctIndex = index % CHARS.length;
        String name = this.objectivePrefix + MINECRAFT_COLOR_CODE + CHARS[correctIndex] + " ";
        this.score = objective.getOrCreateScore(Text.of(name));
        this.score.setScore(value);

        this.team = Team.builder().name(this.originalObjectivePrefix + "_" + CHARS[correctIndex]).build();
        this.team.addMember(this.score.getName());

        for (Scoreboard scoreboard : objective.getScoreboards())
        {
            Optional<Team> optionalTeam = scoreboard.getTeam(this.team.getName());
            optionalTeam.ifPresent(Team::unregister);
            scoreboard.registerTeam(this.team);
        }
    }

    public void setPrefix(Text text)
    {
        this.team.setPrefix(text);
    }

    public void setSuffix(Text text)
    {
        try
        {
            this.team.setSuffix(text);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(text);
            System.out.println(text.toPlain());
        }
    }

    public void setValue(int value)
    {
        this.score.setScore(value);
    }

    public Score getSpongeScore()
    {
        return this.score;
    }

    public Team getSpongeTeam()
    {
        return this.team;
    }

    public void remove(Objective objective)
    {
        objective.removeScore(this.score);
    }
}
