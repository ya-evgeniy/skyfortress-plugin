package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.module.items.SfSimpleSpawnerModule;
import ru.jekarus.skyfortress.module.items.SfSpawner;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfSimpleEmeraldModule extends SfSimpleSpawnerModule {

    private static SfSimpleEmeraldModule INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : SfSimpleEmeraldModule.class.getSimpleName() + " already registered";
        INS = new SfSimpleEmeraldModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : SfSimpleEmeraldModule.class.getSimpleName() + " is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public SfSimpleEmeraldModule(Plugin plugin, SkyFortress sf) {
        super(plugin, sf);
    }

    @EventHandler
    public void on2(GameStartEvent event) {
        final var location = SfConfig.emerald.toLocation(sf.world);
        spawners.add(new SfSpawner(location, 20, 10, () -> new ItemStack(Material.EMERALD, 1)));
    }

}
