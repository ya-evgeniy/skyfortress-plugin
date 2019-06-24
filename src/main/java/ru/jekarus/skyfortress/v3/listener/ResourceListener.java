package ru.jekarus.skyfortress.v3.listener;

import lombok.val;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.resource.BlockResource;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class ResourceListener {

    private final SkyFortressPlugin plugin;

    public ResourceListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onBlockDestroy(ChangeBlockEvent.Break event) {
        final val resourceContainer = this.plugin.getResourceContainer();
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            final val finalBlock = transaction.getFinal();
            for (BlockResource resource : resourceContainer.getBlocks()) {
                if (!SfUtils.compare(finalBlock.getPosition(), resource.getLocation().getPosition())) {
                    continue;
                }
                if (resource.isRunned()) {
                    transaction.setValid(false);
                }
                else {
                    Task.builder().delayTicks(1).execute(() -> resource.getLocation().setBlock(resource.getWhileWaitBlock())).submit(plugin);
                    resource.setRunned(true);
                }
            }
        }
    }

}
