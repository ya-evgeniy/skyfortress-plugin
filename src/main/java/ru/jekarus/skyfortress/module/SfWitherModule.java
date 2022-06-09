package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.object.SfObjectModule;
import ru.jekarus.skyfortress.module.object.SfWither;
import ru.jekarus.skyfortress.state.SkyFortress;

public class SfWitherModule extends SfObjectModule<SfWither> {

    private static SfWitherModule INS;
    private final SkyFortress sf;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "SfWitherModule already registered";
        INS = new SfWitherModule(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "SfWitherModule is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public SfWitherModule(Plugin plugin, SkyFortress sf) {
        super(plugin);
        this.sf = sf;
        initState();
        initMoving();
    }

    @EventHandler
    public void on(GameStartEvent event) {
        for(SfConfig.ObjectLoc objectLoc : SfConfig.WITHERS) {
            final var location = objectLoc.toLocation(sf.world);
            sf.world.spawnEntity(location, EntityType.WITHER, CreatureSpawnEvent.SpawnReason.DEFAULT, entity -> {
                entities.put(entity.getUniqueId(), new SfWither(plugin, (Wither) entity, location));
            });
            break;
        }
    }

    @EventHandler
    public void on(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() != EntityType.WITHER_SKULL) return;
        final var skull = (WitherSkull) event.getEntity();
        if (skull.getShooter() instanceof Wither wither) {
            final var sfw = entities.get(wither.getUniqueId());
            if (sfw == null) return;
            if(sfw.target == null || !sfw.agro) {
                event.setCancelled(true);
                return;
            }
            skull.setVelocity(skull.getVelocity().multiply(0.1));
        }
    }

//    @EventHandler
//    public void on(ProjectileCollideEvent event) {
//        if (event.getEntity().getType() != EntityType.WITHER_SKULL) return;
//        final var skull = (WitherSkull) event.getEntity();
//        if (skull.getShooter() instanceof Wither wither) {
//            final var sfw = withers.get(wither.getUniqueId());
//            if (sfw == null) return;
////            event.setCancelled(true);
//        }
//    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.WITHER_SKULL) return;
        final var skull = (WitherSkull) event.getEntity();
        if (skull.getShooter() instanceof Wither wither) {
            final var sfw = entities.get(wither.getUniqueId());
            if (sfw == null) return;
            final var hitBlock = event.getHitBlock();
            if(hitBlock != null) {
                if(hitBlock.getLocation().distanceSquared(sfw.location) < (6*6)) {
                    hitBlock.breakNaturally();
                }
            }
        }
    }

    @EventHandler
    public void on(ExplosionPrimeEvent event) {
        if (event.getEntity().getType() != EntityType.WITHER_SKULL) return;
        final var skull = (WitherSkull) event.getEntity();
        if (skull.getShooter() instanceof Wither wither) {
            final var sfw = entities.get(wither.getUniqueId());
            if (sfw == null) return;
            event.setRadius(0.01f);
//            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageByBlockEvent event) {
        final var sfw = entities.get(event.getEntity().getUniqueId());
        if (sfw == null) return;
        event.setCancelled(true);
    }

}
