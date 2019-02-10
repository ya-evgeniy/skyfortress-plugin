package ru.jekarus.skyfortress.v3.distribution.random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.Distribution;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.util.*;

public class RandomDistribution implements Distribution
{

    private RandomDistributionState state = new RandomDistributionState();

    public void initPlayers(List<SfPlayer> players)
    {
        this.state.players = players;
    }

    public void start()
    {

        SkyFortressPlugin plugin = SkyFortressPlugin.getInstance();

        SfTeamContainer teamContainer = plugin.getTeamContainer();

        ArrayList<SfGameTeam> gameTeams = new ArrayList<>(teamContainer.getGameCollection());
        gameTeams.sort((o1, o2) -> o2.getPlayers().size() - o1.getPlayers().size());

        List<SfPlayer> players = new ArrayList<>(this.state.players);
        Iterator<SfPlayer> iterator = players.iterator();
        while (iterator.hasNext())
        {
            SfPlayer player = iterator.next();
            SfTeam team = player.getTeam();
            if (team != null && team.getType() != SfTeam.Type.NONE)
            {
                iterator.remove();
            }
        }

//        int currentCount =

    }

}
