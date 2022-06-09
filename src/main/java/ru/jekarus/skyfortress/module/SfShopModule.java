package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfShop;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SfShopModule implements Listener {

    private static SfShopModule INS;
    private final SkyFortress sf;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "SfWitherModule already registered";
        INS = new SfShopModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "SfWitherModule is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    protected final Map<UUID, Entity> entities = new HashMap<>();

    public SfShopModule(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
    }

    @EventHandler
    public void on(GameStartEvent event) {
        for (SfTeam sft : SfTeam.values()) {
            for (Entity entity : SfShop.spawn(sf.world, sft.shops, sft.face)) {
                entities.put(entity.getUniqueId(), entity);
            }
        }
    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    protected void cleanup() {
        for (Entity obj : entities.values()) {
            obj.remove();
        }
        entities.clear();
    }

}
