package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.module.items.SfSimpleSpawnerModule;
import ru.jekarus.skyfortress.module.items.SfSpawner;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfSimpleIronModule extends SfSimpleSpawnerModule {

    private static SfSimpleIronModule INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : SfSimpleIronModule.class.getSimpleName() + " already registered";
        INS = new SfSimpleIronModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : SfSimpleIronModule.class.getSimpleName() + " is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public SfSimpleIronModule(Plugin plugin, SkyFortress sf) {
        super(plugin, sf);
    }

    @EventHandler
    public void on2(GameStartEvent event) {
        for (SfTeam sft : SfTeam.values()) {
            final var location = sft.iron.toLocation(sf.world);
            spawners.add(new SfSpawner(location, 16, 2, () -> new ItemStack(Material.IRON_INGOT, 1)));
        }
    }

}
