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
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

public class LobbyButtonsListener {

    private final SkyFortressPlugin plugin;
    private final PlayersDataContainer players;

    public LobbyButtonsListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.players = PlayersDataContainer.getInstance();
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
        PlayerData playerData = this.players.getOrCreateData(player);
        for (Transaction<BlockSnapshot> transaction : event.getTransactions())
        {
            BlockSnapshot original = transaction.getOriginal();
            BlockType type = original.getState().getType();
            if (type.equals(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE))
            {
                if (plugin.getGame().getStage() == SfGameStageType.PRE_GAME) {
                    for (LobbyRoom room : this.plugin.getLobbyRoomsContainer().getRooms()) {
                        final boolean result = room.getInteractions().standOnPlate(player, playerData, original);
                        if (result) {
                            break;
                        }
                    }
//                    this.plugin.getLobby().standOnPlate(player, playerData, original);
                }
            }
            else if (type.equals(BlockTypes.WOODEN_BUTTON))
            {
                if (plugin.getGame().getStage() == SfGameStageType.PRE_GAME) {
                    for (LobbyRoom room : this.plugin.getLobbyRoomsContainer().getRooms()) {
                        final boolean result = room.getInteractions().pressButton(player, playerData, original);
                        if (result) {
                            break;
                        }
                    }
//                    this.plugin.getLobby().pressButton(player, playerData, original);
                }
            }
        }
    }

}
