package ru.jekarus.skyfortress.module;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.module.gui.enchantments.*;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfEnchantmentsModule implements Listener {

    private static SfEnchantmentsModule INST;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new SfEnchantmentsModule(plugin, sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    public static void open(HumanEntity human) {
        new EnchantmentsGui(INST.sf, human).openInventory();
    }

    private final Plugin plugin;
    private final SkyFortress sf;

    public SfEnchantmentsModule(Plugin plugin, SkyFortress sf) {
        this.plugin = plugin;
        this.sf = sf;
    }


    @EventHandler
    public void on(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);
            new EnchantmentsGui(sf, event.getPlayer()).openInventory();
        }
    }

}
