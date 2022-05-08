package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.GameStartEvent;
import ru.jekarus.skyfortress.GameStopEvent;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DragonObject implements Listener {

    private static DragonObject INS;

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "DragonObject already registered";
        INS = new DragonObject(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "DragonObject is not registered";
        HandlerList.unregisterAll(INS);
        INS.cleanup();
        INS = null;
    }

    public static class SfDragon {
        public final EnderDragon entity;
        public EnderDragon.Phase phase;

        public SfDragon(EnderDragon entity) {
            this.entity = entity;
            entity.setSilent(true);
            entity.getPathfinder().stopPathfinding();
            Bukkit.getMobGoals().removeAllGoals(entity);
        }

        public void setPhase(EnderDragon.Phase phase) {
            this.phase = phase;
            this.entity.setPhase(phase);
        }
    }

    private final SkyFortress sf;
    private final Map<UUID, SfDragon> dragons = new HashMap<>();

    public DragonObject(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
//        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
//            for (SfDragon dragon : dragons.values()) {
////                System.out.println(Bukkit.getMobGoals().getAllGoals(dragon));
////                System.out.println(dragon.getLocation());
//                break;
//            }
//        }, 20, 20);
    }

    @EventHandler
    public void on(GameStartEvent event) {
        final var world = Bukkit.getServer().getWorlds().get(0);
        for(SfConfig.DragonLoc dragonLoc : SfConfig.DRAGONS) {
            final var location = dragonLoc.toLocation(world);
            world.spawnEntity(location, EntityType.ENDER_DRAGON, CreatureSpawnEvent.SpawnReason.COMMAND, entity -> {
                final var dragon = (EnderDragon) entity;
                final var sfd = new SfDragon(dragon);
                sfd.setPhase(EnderDragon.Phase.BREATH_ATTACK);
                dragons.put(dragon.getUniqueId(), sfd);
            });
        }
    }

    @EventHandler
    public void on(EnderDragonChangePhaseEvent event) {
        final var sfd = dragons.get(event.getEntity().getUniqueId());
        if (sfd == null) return;
        if(event.getNewPhase() != sfd.phase) event.setNewPhase(EnderDragon.Phase.HOVER);
    }

    @EventHandler
    public void on(GameStopEvent event) {
        cleanup();
    }

    private void cleanup() {
        for (SfDragon dragon : dragons.values()) {
            dragon.entity.remove();
        }
        dragons.clear();
    }


}
