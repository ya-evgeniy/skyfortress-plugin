package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.state.SkyFortress;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;

public class SfLobby implements Listener {

    private static SfLobby INST;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new SfLobby(sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    private final SkyFortress sf;

    public SfLobby(SkyFortress sf) {
        this.sf = sf;
    }


    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (!sf.isGameStarted()) {
            final var sr = Bukkit.getServer().getSpawnRadius();
            final var pl = event.getPlayer().getLocation();
            final var sl = sf.world.getSpawnLocation();
            final var xmatch = Math.abs(pl.getBlockX() - sl.getBlockX()) <= sr;
            final var zmatch = Math.abs(pl.getBlockZ() - sl.getBlockZ()) <= sr;
            if(xmatch && zmatch) {
                event.getPlayer().teleport(SfConfig.LEAVE.middle().toLocation(sf.world));
            }
        }
    }

    @EventHandler
    public void on(BlockPlayerMove.Event event) {
        for (SfTeam sft : SfTeam.values()) {
            if(sft.join.contains(event.getTo())) {
                sf.playerJoin(sft, event.getPlayer());
                return;
            }
        }
        if(SfConfig.LEAVE.contains(event.getTo())) {
            sf.playerLeave(event.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final var v = new Vec3i(event.getClickedBlock().getLocation());
            if(SfConfig.FORCE_START.eq(v)) {
                if(!sf.isGameStarted()) {
                    sf.gameStart();
                } else {
                    final var sft = SfTeam.get(event.getPlayer());
                    if (sft != null) {
                        sft.spawn.teleport(event.getPlayer(), sft.face);
                    }
                    SfSidebar.updateAll();
                }
            }
            for (SfTeam sft : SfTeam.values()) {
                if(sft.ready.eq(v)) {
                    this.sf.toggleReady(sft);
                    SfSidebar.updateAll();
                }
            }
        }
    }

}
