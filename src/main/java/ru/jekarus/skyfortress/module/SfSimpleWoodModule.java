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

public class SfSimpleWoodModule extends SfSimpleSpawnerModule {

    private static SfSimpleWoodModule INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : SfSimpleWoodModule.class.getSimpleName() + " already registered";
        INS = new SfSimpleWoodModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : SfSimpleWoodModule.class.getSimpleName() + " is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public SfSimpleWoodModule(Plugin plugin, SkyFortress sf) {
        super(plugin, sf);
    }

    @EventHandler
    public void on2(GameStartEvent event) {
        for (SfTeam sft : SfTeam.values()) {
            final var location = sft.wood.toLocation(sf.world);
            spawners.add(new SfSpawner(location, 16, 5, () -> new ItemStack(Material.OAK_WOOD, 3)));
        }
    }

}
