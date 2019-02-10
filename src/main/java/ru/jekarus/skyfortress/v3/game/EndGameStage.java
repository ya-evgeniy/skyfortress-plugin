package ru.jekarus.skyfortress.v3.game;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;

public class EndGameStage extends SfGameStage {

    private final SkyFortressPlugin plugin;

    public EndGameStage(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void enable()
    {
        this.plugin.getScoreboards().clearSideBar();
        SfLobby lobby = this.plugin.getLobby();
        SfPlayers players = SfPlayers.getInstance();
        for (Player player : Sponge.getServer().getOnlinePlayers())
        {
            player.setLocationAndRotation(
                    lobby.getSettings().center.getLocation(),
                    lobby.getSettings().center.getRotation()
            );
            player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
            SfPlayer sfPlayer = players.getOrCreatePlayer(player);
            this.plugin.getTeamContainer().getNoneTeam().addPlayer(this.plugin, sfPlayer);
            player.getInventory().clear();
        }
    }

    @Override
    public void disable()
    {

    }

    @Override
    public SfGameStageType getType()
    {
        return SfGameStageType.END_GAME;
    }

}
