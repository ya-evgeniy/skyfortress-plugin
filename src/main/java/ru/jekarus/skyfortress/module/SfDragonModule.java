package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.object.SfObjectModule;
import ru.jekarus.skyfortress.module.object.SfDragon;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfDragonModule extends SfObjectModule<SfDragon> {

    private static SfDragonModule INS;
    private final SkyFortress sf;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "SfDragonModule already registered";
        INS = new SfDragonModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "SfDragonModule is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public SfDragonModule(Plugin plugin, SkyFortress sf) {
        super(plugin);
        this.sf = sf;
        initState();
        initMoving();
    }

    @EventHandler
    public void on(GameStartEvent event) {
        for(SfConfig.ObjectLoc objectLoc : SfConfig.DRAGONS) {
            final var location = objectLoc.toLocation(sf.world);
            sf.world.spawnEntity(location, EntityType.ENDER_DRAGON, CreatureSpawnEvent.SpawnReason.DEFAULT, entity -> {
                entities.put(entity.getUniqueId(), new SfDragon((EnderDragon) entity, location));
            });
        }
    }

    @EventHandler
    public void on(EnderDragonChangePhaseEvent event) {
        final var sfd = entities.get(event.getEntity().getUniqueId());
        if (sfd == null) return;
        if(event.getNewPhase() == EnderDragon.Phase.LEAVE_PORTAL) return;
        if(event.getNewPhase() == EnderDragon.Phase.BREATH_ATTACK) return;
        event.setNewPhase(EnderDragon.Phase.HOVER);
    }

}
