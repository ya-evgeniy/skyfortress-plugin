package ru.jekarus.skyfortress;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.jekarus.skyfortress.config.SfTeam;

import java.util.*;

public class CaptureSystem implements Listener {

    private static CaptureSystem INST;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new CaptureSystem(plugin, sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST.stop();
            INST = null;
        }
    }

    private final SkyFortress sf;
    private final BukkitTask task;
    private final Map<SfTeam, List<Player>> capturePlayers = new EnumMap<>(SfTeam.class);

    public CaptureSystem(Plugin plugin, SkyFortress sf) {
        this.sf = sf;
        this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::run, 20, 20);
    }

    private void run() {
        if(!sf.isGameStarted()) return;
        boolean changed = false;
        for (Map.Entry<SfTeam, List<Player>> entry : capturePlayers.entrySet()) {
            final var sft = entry.getKey();
            final var players = entry.getValue();
            if(players.isEmpty()) continue;
            final var state = sf.getTeamState(sft);
            if(state.health > 0) {
                state.health -= 1;
                changed = true;
                final var target = players.get(0);  // first player entered the capture zone
                target.damage(1);
                if(state.health == 0) {
                    // team captured
                }
            }
        }
        if(changed) SfSidebar.updateAll();
    }

    private void stop() {
        this.task.cancel();
    }

    private List<Player> getPlayers(SfTeam sft) {
        return capturePlayers.computeIfAbsent(sft, k -> new ArrayList<>());
    }
    private void addLast(SfTeam sft, Player player) {
        final var players = getPlayers(sft);
        for(Player member : players) {
            if (member.getUniqueId().equals(player.getUniqueId())) return;
        }
        players.add(player);
    }
    private void remove(SfTeam sft, Player player) {
        final var players = getPlayers(sft);
        players.removeIf(member -> member.getUniqueId().equals(player.getUniqueId()));
    }

    @EventHandler
    public void onBlockMove(BlockPlayerMove.Event event) {
        if(!sf.isGameStarted()) return;
        for (SfTeam sft : SfTeam.values()) {
            final var psft = SfTeam.get(event.getPlayer());
            boolean isTeammate = psft != null && psft.equals(sft);
            if(isTeammate) continue;
            boolean frContains = sft.capture.contains(event.getFr());
            boolean toContains = sft.capture.contains(event.getTo());
            if(!frContains && toContains) {  // enter
                addLast(sft, event.getPlayer());
            }
            if(frContains && !toContains) {  // exit
                remove(sft, event.getPlayer());
            }
        }
    }

}
