package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;

public class LobbyButtonsListener {

    private final SkyFortressPlugin plugin;
    private final SfPlayers players;

    public LobbyButtonsListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.players = SfPlayers.getInstance();
    }

    public void register()
    {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onInteract(ChangeBlockEvent.Modify event, @First Player player)
    {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
        for (Transaction<BlockSnapshot> transaction : event.getTransactions())
        {
            BlockSnapshot original = transaction.getOriginal();
            BlockType type = original.getState().getType();
            if (type.equals(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE))
            {
                if (plugin.getGame().getStage() == SfGameStageType.PRE_GAME) {
                    this.plugin.getLobby().standOnPlate(player, sfPlayer, original);
                }
            }
            else if (type.equals(BlockTypes.WOODEN_BUTTON))
            {
                if (plugin.getGame().getStage() == SfGameStageType.PRE_GAME) {
                    this.plugin.getLobby().pressButton(player, sfPlayer, original);
                }
            }
        }
    }

}
