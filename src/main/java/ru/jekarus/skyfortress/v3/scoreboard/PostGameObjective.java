package ru.jekarus.skyfortress.v3.scoreboard;

import lombok.Getter;
import lombok.val;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.lang.LanguageVariables;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.HashMap;
import java.util.Map;

public class PostGameObjective extends SfObjective {

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard;
    private final SfLanguage language;

    @Getter private final Objective objective;
    private Map<SfGameTeam, SfScore> teamScores = new HashMap<>();

    public PostGameObjective(SkyFortressPlugin plugin, Scoreboard scoreboard, SfLanguage language) {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
        this.language = language;

        this.objective = Objective.builder()
                .name("pgo")
                .displayName(language.map.name)
                .criterion(Criteria.DUMMY)
                .build();

        this.init();
    }

    @Override
    public void init() {

        this.scoreboard.addObjective(this.objective);

        int index = 0;
        for (SfGameTeam gameTeam : this.plugin.getTeamContainer().getGameCollection()) {
            SfScore score = new SfScore(this.objective.getName());
            score.createScore(this.objective, index++);
            score.setSuffix(
                    new LanguageVariables(language).teamKey().name(gameTeam).apply(language.scoreboard.inGame.alive) // FIXME: 11.03.2019
            );
            this.teamScores.put(gameTeam, score);
        }

    }

    public void updatePlaces() {
        for (SfCastle castle : this.plugin.getCastleContainer().getCollection()) {
            final val team = castle.getTeam();
            final val score = this.teamScores.get(team);
            if (!castle.isPlayed()) {
                score.remove(objective);
                continue;
            }
            if (score != null) {
                score.setValue(plugin.getCastleContainer().getCollection().size() - castle.getPlace());
                score.setPrefix(Text.of(team.getColor(), castle.getPlace()));
                score.setSuffix(
                        new LanguageVariables(language).teamKey().name(team, false).apply(language.scoreboard.inGame.alive) // FIXME: 11.03.2019
                );
            }
        }
    }

}