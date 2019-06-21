package ru.jekarus.skyfortress.v3.scoreboard;

import lombok.Getter;
import lombok.val;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.LanguageVariables;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class InGameObjective extends SfObjective {

    private static final int ALIVE_VALUE = 1;
    private static final int DEATH_VALUE = 0;

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard;

    @Getter private final Objective objective;
    private final SfLanguage language;

    private Map<SfGameTeam, SfScore> teamScores = new HashMap<>();
    private DecimalFormat healthFormat = new DecimalFormat("0.00");

    public InGameObjective(SkyFortressPlugin plugin, Scoreboard scoreboard, SfLanguage language) {
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
    public void init() {
        this.scoreboard.addObjective(this.objective);

        int index = 0;
        for (SfGameTeam gameTeam : this.plugin.getTeamContainer().getGameCollection()) {
            SfScore score = new SfScore(this.objective.getName());
            score.createScore(this.objective, index++, ALIVE_VALUE);
            score.setSuffix(
                    new LanguageVariables(language).teamKey().name(gameTeam).apply(language.scoreboard.inGame.alive) // FIXME: 11.03.2019
            );
            this.teamScores.put(gameTeam, score);
            this.updateLeftSeconds(gameTeam);
        }
    }

    public void updateLeftSeconds(SfGameTeam gameTeam) { // 8+5+4=12+5=17
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null) {
            final val castle = gameTeam.getCastle();
            final val builder = Text.builder();

            final val formattedHealthSeconds = this.healthFormat.format((double) castle.getHealth() / 20);
            final val healthSeconds = Text.of(TextColors.GOLD, formattedHealthSeconds);

            if (castle.isShowDeathSeconds()) {
                final val deathSeconds = Text.of(TextColors.RED, String.valueOf(castle.getDeathSeconds()));
                builder.append(deathSeconds);
                if (castle.getHealth() > 0) {
                    builder.append(Text.of("  ")).append(healthSeconds);
                }
            }
            else {
                builder.append(healthSeconds);
            }
            score.setPrefix(builder.build());
        }
    }

    public void updateDeath(SfGameTeam gameTeam) {
        SfScore score = this.teamScores.get(gameTeam);
        if (score != null) {
            score.setValue(DEATH_VALUE);
            score.setPrefix(Text.of(gameTeam.getCastle().getTeam().getColor(), gameTeam.getCastle().getPlace()));
            score.setSuffix(
                    new LanguageVariables(language).teamKey().name(gameTeam, false).apply(language.scoreboard.inGame.death) // FIXME: 11.03.2019
            );
        }
    }

    public void resetTeam(SfGameTeam team) {
        SfScore score = this.teamScores.get(team);
        if (score != null) {
            score.remove(this.getObjective());
        }
    }

}
