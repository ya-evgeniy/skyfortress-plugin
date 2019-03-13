package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.LanguageVariables;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.HashMap;
import java.util.Map;

public class InGameObjective extends SfObjective {

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard;

    private final Objective objective;
    private final SfLanguage language;

    private Map<SfGameTeam, SfScore> teamScores = new HashMap<>();

    public InGameObjective(SkyFortressPlugin plugin, Scoreboard scoreboard, SfLanguage language)
    {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
        this.language = language;

        this.objective = Objective.builder()
                .name("igo")
                .displayName(language.map.name)
                .criterion(Criteria.DUMMY)
                .build();
    }

    @Override
    public void init()
    {
        this.scoreboard.addObjective(this.objective);

        for (SfGameTeam gameTeam : this.plugin.getTeamContainer().getGameCollection())
        {
            SfScore score = new SfScore(this.objective.getName());
            score.createScore(this.objective, gameTeam.getOrigin(), gameTeam.getCastle().getHealth());
            score.setPrefix(Text.builder().append(Text.of()).color(TextColors.DARK_RED).build());
            score.setSuffix(
                    new LanguageVariables(language).teamKey().color(gameTeam).name(gameTeam).apply(language.scoreboard.inGame.alive) // FIXME: 11.03.2019
            );
            this.teamScores.put(
                    gameTeam, score
            );
        }
    }

    public void updateHealth(SfGameTeam gameTeam)
    {
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null)
        {
            score.setValue(gameTeam.getCastle().getHealth());
        }
    }

    public void updateDeathSeconds(SfGameTeam gameTeam)
    {
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null)
        {
            score.setPrefix(
                    Text.builder().append(Text.of(gameTeam.getCastle().getDeathSeconds())).color(TextColors.DARK_RED).build()
            );
        }
    }

    public void resetDeathSeconds(SfGameTeam gameTeam)
    {
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null)
        {
            score.setPrefix(Text.builder().append(Text.of()).color(TextColors.DARK_RED).build());
        }
    }

    public void updateDeath(SfGameTeam gameTeam)
    {
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null)
        {
            score.setPrefix(Text.builder().append(Text.of()).color(TextColors.DARK_RED).build());
            score.setSuffix(
                    new LanguageVariables(language).teamKey().name(gameTeam).apply(language.scoreboard.inGame.death) // FIXME: 11.03.2019
            );
        }
    }

    public void resetTeam(SfGameTeam team)
    {
        SfScore score = this.teamScores.get(team);
        if (score != null)
        {
            score.remove(this.getObjective());
        }
    }

    @Override
    public Objective getObjective()
    {
        return this.objective;
    }
}
