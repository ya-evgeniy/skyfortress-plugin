package ru.jekarus.skyfortress;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
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
    public void onBlockMove(BlockPlayerMove.Event event) {
        final var sb = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        for (SfTeam sft : SfTeam.values()) {
            if(sft.join.contains(event.getTo())) {
                sft.team().addPlayer(event.getPlayer());
                return;
            }
        }
        if(SfConfig.LEAVE.contains(event.getTo())) {
            final var team = sb.getPlayerTeam(event.getPlayer());
            if (team != null) team.removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
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
