package ru.jekarus.skyfortress.module;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.module.gui.ChestGuiBase;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.stream.Stream;

public class SfGuiModule implements Listener {

    private static SfGuiModule INST;

    private final Plugin plugin;
    private final SkyFortress sf;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new SfGuiModule(plugin, sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    public SfGuiModule(Plugin plugin, SkyFortress sf) {
        this.plugin = plugin;
        this.sf = sf;
    }

    @EventHandler
    public void on(final InventoryOpenEvent e) {
        if(e.getInventory().getHolder() instanceof ChestGuiBase gui) {
            gui.open(e);
        }
    }

    @EventHandler
    public void on(final InventoryClickEvent e) {
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if(e.getView().getTopInventory().getHolder() instanceof ChestGuiBase gui) {
                gui.move(e);
            }
        } else if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            if(e.getView().getTopInventory().getHolder() instanceof ChestGuiBase gui) {
                gui.collect(e);
            }
        } else {
            if(e.getInventory().getHolder() instanceof ChestGuiBase gui) {
                gui.click(e);
            }
        }
    }

    @EventHandler
    public void on(final InventoryDragEvent e) {
        if(e.getInventory().getHolder() instanceof ChestGuiBase gui) {
            gui.drag(e);
        }
    }

    @EventHandler
    public void on(final InventoryCloseEvent e) {
        if(e.getInventory().getHolder() instanceof ChestGuiBase gui) {
            gui.close(e);
        }
    }

}
