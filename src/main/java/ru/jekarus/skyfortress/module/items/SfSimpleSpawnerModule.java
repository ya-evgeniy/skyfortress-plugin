package ru.jekarus.skyfortress.module.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.ArrayList;
import java.util.List;

public class SfSimpleSpawnerModule implements Listener {

    private final Plugin plugin;
    protected final SkyFortress sf;
    protected final List<SfSpawner> spawners = new ArrayList<>();
    private BukkitTask task;

    public SfSimpleSpawnerModule(Plugin plugin, SkyFortress sf) {
        this.plugin = plugin;
        this.sf = sf;
    }

    @EventHandler
    public void on(GameStartEvent event) {
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (SfSpawner spawner : spawners) {
                spawner.tick();
            }
        }, 20, 20);
    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    protected void cleanup() {
        spawners.clear();
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

}
