package ru.jekarus.skyfortress.v3.scoreboard;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;

public class SfScoreboard {

    private final SkyFortressPlugin plugin;
    private final Scoreboard scoreboard = Scoreboard.builder().build();

    private final PreGameObjective preGameObjective;
    private final InGameObjective inGameObjective;
    private final SfScoreboardTeams teams;

    public SfScoreboard(SkyFortressPlugin plugin, SfLanguage language)
    {
        this.plugin = plugin;

        this.preGameObjective = new PreGameObjective(this.plugin, this.scoreboard, language);
        this.inGameObjective = new InGameObjective(this.plugin, this.scoreboard, language);
        this.teams = new SfScoreboardTeams(plugin, this.scoreboard, language);
    }

    public void init()
    {
        this.preGameObjective.init();
        this.inGameObjective.init();
        this.teams.init();
    }

    public PreGameObjective getPreGameObjective()
    {
        return this.preGameObjective;
    }

    public InGameObjective getInGameObjective()
    {
        return this.inGameObjective;
    }

    public SfScoreboardTeams getTeams()
    {
        return this.teams;
    }

    public void setFor(Player player)
    {
        player.setScoreboard(this.scoreboard);
    }

    public void setSideBar(SfScoreboards.Types type)
    {
        Objective objective = null;
        switch (type)
        {
            case PRE_GAME:
                objective = this.preGameObjective.getObjective();
                break;
            case IN_GAME:
                objective = this.inGameObjective.getObjective();
                break;
            case POST_GAME:
//                objective = this.post.getObjective();
                break;
        }
        if (objective != null)
        {
            this.scoreboard.updateDisplaySlot(objective, DisplaySlots.SIDEBAR);
        }
    }

    public void clearSideBar()
    {
        this.scoreboard.clearSlot(DisplaySlots.SIDEBAR);
    }

}
