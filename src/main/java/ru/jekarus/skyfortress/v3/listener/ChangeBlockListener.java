package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class ChangeBlockListener {

    private final SkyFortressPlugin plugin;

    public ChangeBlockListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player) {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            BlockSnapshot snapshot = transaction.getFinal();
            BlockState state = snapshot.getState();
            if (state.getType().equals(BlockTypes.BED)) {
                event.setCancelled(true);
            }
        }
    }

}
