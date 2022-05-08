package ru.jekarus.skyfortress;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;

public class SfLobby implements Listener {

    private static SfLobby INST;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new SfLobby(sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            INST.onBlockMove(player, new Vec3i(player.getLocation()), new Vec3i(player.getLocation()));
        }
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


    public void onBlockMove(Player player, Vec3i fr, Vec3i to) {
        final var sb = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        for (SfTeam sft : SfTeam.values()) {
            if(sft.join.contains(to)) {
                sft.team().addPlayer(player);
                return;
            }
        }
        if(SfConfig.LEAVE.contains(to)) {
            final var team = sb.getPlayerTeam(player);
            if (team != null) team.removePlayer(player);
        }
    }
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        final var fr = new Vec3i(event.getFrom());
        final var to = new Vec3i(event.getTo());
        if(fr.equals(to)) return;
        onBlockMove(event.getPlayer(), fr, to);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final var fr = new Vec3i(event.getFrom());
        final var to = new Vec3i(event.getTo());
        if(fr.equals(to)) return;
        onBlockMove(event.getPlayer(), fr, to);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final var v = new Vec3i(event.getClickedBlock().getLocation());
            if(SfConfig.FORCE_START.equals(v)) {
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
                if(sft.ready.equals(v)) {
                    this.sf.toggleReady(sft);
                    SfSidebar.updateAll();
                }
            }
        }
    }

}
