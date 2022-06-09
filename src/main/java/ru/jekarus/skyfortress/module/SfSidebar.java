package ru.jekarus.skyfortress.module;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.module.damage.SfDamageImpl;
import ru.jekarus.skyfortress.state.SfPlayerState;
import ru.jekarus.skyfortress.state.SkyFortress;
import ru.jekarus.skyfortress.config.SfTeam;

import java.util.*;

public class SfSidebar implements Listener {

    private static SfSidebar INST;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new SfSidebar(sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
        SfSidebar.updateAll();
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    private final SkyFortress sf;
    private final Map<UUID, FastBoard> sidebars = new HashMap<>();

    public SfSidebar(SkyFortress sf) {
        this.sf = sf;
    }

    public static void updateAll() {
        final var sfs = INST;
        if(sfs == null) return;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            sfs.formatSidebar(player);
        }
    }

    public FastBoard get(Player player) {
        var board = sidebars.get(player.getUniqueId());
        if(board == null) {
            board = new FastBoard(player);
            sidebars.put(player.getUniqueId(), board);
        }
        return board;
    }

    public void renderDamageScale(List<String> lines, Player player) {
        final SfPlayerState state;
        if((state = sf.getPlayerState(player)) != null && state.team != null) {
            final var ts = sf.getTeamState(state.team);

            lines.add(ChatColor.GRAY + "Опыт: " + ChatColor.WHITE + "%.2f".formatted(ts.experience));
            lines.add(ChatColor.GRAY + "Уровень: " + ChatColor.WHITE + "%d".formatted(ts.getLevel()));
            lines.add(ChatColor.GRAY + "Скейл: " + ChatColor.WHITE + "%.2f".formatted(SfDamageImpl.calcScale(1, ts.getLevel())));
        }
    }

    public void formatSidebar(Player player) {
        var fb = get(player);
        fb.updateTitle(ChatColor.GOLD + "Sky Fortress " + ChatColor.GRAY + "3.0.0");
        if (!sf.isGameStarted()) {
            final var lines = new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + "Создатели:",
                    ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + "JekaRUS",
                    ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + "DiaLight",
                    ""
            ));
            for (SfTeam sft : SfTeam.values()) {
                final var ready = this.sf.isReady(sft);
                lines.add(
                        (ready ? ChatColor.GREEN + "+ " : ChatColor.RED + "- ") +
                                ChatColor.GRAY + sft.displayNameTo
                );
            }
            renderDamageScale(lines, player);
            fb.updateLines(lines);
        } else {
            final var lines = new ArrayList<String>();
            for (SfTeam sft : SfTeam.values()) {
                final var state = this.sf.getTeamState(sft);

                lines.add(ChatColor.GRAY + "Команда " + sft.chat + sft.displayNameOf + ": " + ChatColor.WHITE + state.health);
            }
            renderDamageScale(lines, player);
            fb.updateLines(lines);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        formatSidebar(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        sidebars.remove(event.getPlayer().getUniqueId());
    }


}
