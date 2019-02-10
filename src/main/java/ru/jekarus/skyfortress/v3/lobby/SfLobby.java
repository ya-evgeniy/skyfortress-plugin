package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

import java.util.ArrayList;
import java.util.Collection;

public class SfLobby {

    private final SkyFortressPlugin plugin;
    private final SfLobbySettings settings = new SfLobbySettings();

    private Collection<SfLobbyTeam> teams = new ArrayList<>();

    public SfLobby(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void init()
    {
        for (SfLobbyTeam team : this.teams)
        {
            team.init(this.plugin);
        }
    }

    public SfLobbySettings getSettings()
    {
        return this.settings;
    }

    public void add(SfLobbyTeam team)
    {
        this.teams.add(team);
    }

    public void standOnPlate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        for (SfLobbyTeam team : this.teams)
        {
            if (team.standOnPlate(player, sfPlayer, snapshot))
            {
                return;
            }
        }
    }

    public void pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        for (SfLobbyTeam team : this.teams)
        {
            if (team.pressButton(player, sfPlayer, snapshot))
            {
                return;
            }
        }
    }

    public void playerDisconnect(SfPlayer sfPlayer, Player player)
    {
        for (SfLobbyTeam team : this.teams)
        {
            team.playerDisconnect(sfPlayer, player);
        }
    }

    public void checkStart()
    {
        if (this.checkAllReady())
        {
            this.plugin.getGame().start();
        }
    }

    public boolean checkAllReady()
    {
        boolean allReady = true;
        for (SfLobbyTeam team : this.teams)
        {
            allReady = allReady && team.getSettings().ready;
        }
        return allReady;
    }

}
