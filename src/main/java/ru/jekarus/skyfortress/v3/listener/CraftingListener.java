package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.item.ItemTypes;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class CraftingListener {

    private final SkyFortressPlugin plugin;

    public CraftingListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onCraft(CraftItemEvent.Craft event) {
        if (event.getCrafted().getType().equals(ItemTypes.SHIELD)) {
            event.setCancelled(true);
        }
    }

}
