package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.LanguageVariables;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lang.SfTeamLanguage;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.HashMap;
import java.util.Map;

public class PreGameObjective extends SfObjective {

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard;

    private final Objective objective;

    private Map<SfGameTeam, SfScore> teamScores = new HashMap<>();
    private final SfLanguage language;

    public PreGameObjective(SkyFortressPlugin plugin, Scoreboard scoreboard, SfLanguage language)
    {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
        this.language = language;

        this.objective = Objective.builder()
                .name("pgo")
                .displayName(language.map.name)
                .criterion(Criteria.DUMMY)
                .build();
    }

    @Override
    public void init()
    {
        int index = 0;
        this.scoreboard.addObjective(this.objective);

        for (SfGameTeam gameTeam : this.plugin.getTeamContainer().getGameCollection())
        {
            SfScore score = new SfScore(this.objective.getName());
            score.createScore(this.objective, index);
            score.setPrefix(
                    Text.builder().append(Text.of("-")).color(TextColors.RED).build()
            );

            SfTeamLanguage teamLanguage = this.language.teams.get(gameTeam);
            score.setSuffix(
                    new LanguageVariables(this.language).teamKey().name(gameTeam, false).apply(language.scoreboard.preGame.team) // fixme
            );
            this.teamScores.put(
                    gameTeam, score
            );
            index ++;
        }

        SfScore score = new SfScore(this.objective.getName());
        score.createScore(this.objective, ++index);
        score.setPrefix(this.language.scoreboard.preGame.teams.toText());

        score = new SfScore(this.objective.getName());
        score.createScore(this.objective, ++index);

        for (Text creator : language.map.creators)
        {
            score = new SfScore(this.objective.getName());
            score.createScore(this.objective, ++index);
            score.setPrefix(Text.builder("-").color(TextColors.GRAY).build());
            score.setSuffix(creator);
        }

        score = new SfScore(this.objective.getName());
        score.createScore(this.objective, ++index);
        score.setPrefix(this.language.scoreboard.preGame.creators.toText());

        score = new SfScore(this.objective.getName());
        score.createScore(this.objective, ++index);

    }

    public void setReady(SfGameTeam team)
    {
        SfScore score = this.teamScores.get(team);
        if (score != null)
        {
            score.setPrefix(
                    Text.builder().append(Text.of("+")).color(TextColors.GREEN).build()
            );
        }
    }

    public void setUnready(SfGameTeam team)
    {
        SfScore score = this.teamScores.get(team);
        if (score != null)
        {
            score.setPrefix(
                    Text.builder().append(Text.of("-")).color(TextColors.RED).build()
            );
        }
    }

    @Override
    public Objective getObjective()
    {
        return this.objective;
    }

}
