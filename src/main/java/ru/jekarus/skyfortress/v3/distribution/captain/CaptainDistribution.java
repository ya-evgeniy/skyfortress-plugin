package ru.jekarus.skyfortress.v3.distribution.captain;

import ru.jekarus.skyfortress.v3.distribution.Distribution;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.List;
import java.util.Map;

public class CaptainDistribution implements Distribution {

    private CaptainsState state;

    public void initCaptains(Map<SfGameTeam, SfPlayer> captains)
    {
        this.clearStateCaptains();
        for (Map.Entry<SfGameTeam, SfPlayer> entry : captains.entrySet())
        {
            SfGameTeam team = entry.getKey();
            SfPlayer player = entry.getValue();

            CaptainState captainState = this.state.captains.get(team);
            captainState.player = player;
        }
    }

    private void clearStateCaptains()
    {
        for (Map.Entry<SfGameTeam, CaptainState> entry : this.state.captains.entrySet())
        {
            entry.getValue().player = null;
        }
    }

    public void initPlayers(List<SfPlayer> players)
    {
        this.clearStatePlayers();
        List<PlayerState> statePlayers = this.state.players;
        for (int index = 0; index < players.size() && index < statePlayers.size(); index++)
        {
            SfPlayer player = players.get(index);
            PlayerState playerState = statePlayers.get(index);

            playerState.player = player;
        }
    }

    private void clearStatePlayers()
    {
        for (PlayerState player : this.state.players)
        {
            player.player = null;
        }
    }

    public void moveToZone()
    {
        for (Map.Entry<SfGameTeam, CaptainState> entry : this.state.captains.entrySet())
        {
            CaptainState captainState = entry.getValue();
            if (captainState.player == null)
            {
                continue;
            }
            captainState.player.getPlayer().ifPresent(player -> {
                player.setLocationAndRotation(
                        captainState.location.getLocation(),
                        captainState.location.getRotation()
                );
            });
        }

        for (PlayerState playerState : this.state.players)
        {
            if (playerState.player != null)
            {
                playerState.player.getPlayer().ifPresent(player -> {
                    player.setLocationAndRotation(
                            playerState.location.getLocation(),
                            playerState.location.getRotation()
                    );
                });
            }
        }
    }

    public void start()
    {

    }

}
