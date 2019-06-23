package ru.jekarus.skyfortress.v3.listener;

import lombok.val;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

public class PlayerInteractListener {

    private final SkyFortressPlugin plugin;
    private final PlayersDataContainer playersData;

    public PlayerInteractListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.playersData = plugin.getPlayersDataContainer();
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onEntityInteract(InteractEntityEvent event, @First Player player) {
        Entity entity = event.getTargetEntity();
        if (entity.getType().equals(EntityTypes.ILLUSION_ILLAGER)) {
            final val sfPlayer = this.playersData.getOrCreateData(player);
            final val shop = this.plugin.getShops().getShop(sfPlayer.getLocale());
            if (shop != null) {
                player.openInventory(shop.getInventory());
            }
        }
    }

}