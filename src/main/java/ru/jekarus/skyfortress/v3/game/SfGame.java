package ru.jekarus.skyfortress.v3.game;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.listener.LobbyListener;

import java.util.EnumMap;
import java.util.Map;

public class SfGame {

    private final SkyFortressPlugin plugin;
    private final LobbyListener lobbyListener;

    private Map<SfGameStageType, SfGameStage> stageByType = new EnumMap<>(SfGameStageType.class);
    private SfGameStage currentStage;

    public SfGame(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

        this.stageByType.put(SfGameStageType.PRE_GAME, new PreGameStage(plugin));
        this.stageByType.put(SfGameStageType.IN_GAME, new InGameStage(plugin));
        this.stageByType.put(SfGameStageType.END_GAME, new EndGameStage(plugin));

        this.currentStage = this.stageByType.get(SfGameStageType.PRE_GAME);
        this.lobbyListener = new LobbyListener(this.plugin);
    }

    public void init()
    {
        this.currentStage.enable();
        this.lobbyListener.register();
    }

    public SfGameStageType getStage()
    {
        return this.currentStage.getType();
    }

    public void setStage(SfGameStageType type)
    {
        this.currentStage.disable();
        this.currentStage = this.stageByType.get(type);
        this.currentStage.enable();
    }

    public void start()
    {
        this.setStage(SfGameStageType.IN_GAME);
    }

    public void stop()
    {
        this.setStage(SfGameStageType.END_GAME);
    }

    public void checkWin()
    {
        SfCastle winCastle = null;
        for (SfCastle castle : this.plugin.getCastleContainer().getCollection())
        {
            if (castle.isAlive())
            {
                if (winCastle == null)
                {
                    winCastle = castle;
                }
                else
                {
                    return;
                }
            }
        }

        if (winCastle == null)
        {
            return;
        }

        SfMessages messages = this.plugin.getMessages();
        messages.broadcast(
                messages.win(winCastle.getTeam()), true
        );
        this.stop();
    }

}
