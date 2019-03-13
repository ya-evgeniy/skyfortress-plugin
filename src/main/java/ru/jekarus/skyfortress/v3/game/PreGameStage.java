package ru.jekarus.skyfortress.v3.game;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.listener.LobbyButtonsListener;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;

public class PreGameStage extends SfGameStage {

    private final SkyFortressPlugin plugin;

    private final LobbyButtonsListener lobbyButtonsListener;

    public PreGameStage(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

        this.lobbyButtonsListener = new LobbyButtonsListener(this.plugin);
    }

    @Override
    public void enable()
    {
        this.plugin.getScoreboards().setSideBar(SfScoreboards.Types.PRE_GAME);

        this.lobbyButtonsListener.register();
    }

    @Override
    public void disable()
    {
        this.lobbyButtonsListener.unregister();
    }

    @Override
    public SfGameStageType getType()
    {
        return SfGameStageType.PRE_GAME;
    }

}
