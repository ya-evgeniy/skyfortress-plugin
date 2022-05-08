package ru.jekarus.skyfortress.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import ru.jekarus.skyfortress.Vec3i;

public class BlockPlayerMove implements Listener {

    private static BlockPlayerMove INST;

    public static void register(Plugin plugin) {
        if(INST == null) INST = new BlockPlayerMove();
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

    public static class Event extends org.bukkit.event.Event {

        private static final HandlerList handlers = new HandlerList();
        private final Player player;
        private final Vec3i fr;
        private final Vec3i to;

        public Event(Player player, Vec3i fr, Vec3i to) {
            this.player = player;
            this.fr = fr;
            this.to = to;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return handlers;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }

        public Player getPlayer() {
            return player;
        }

        public Vec3i getFr() {
            return fr;
        }

        public Vec3i getTo() {
            return to;
        }

    }

    private final @NotNull PluginManager pluginMan;

    public BlockPlayerMove() {
        pluginMan = Bukkit.getServer().getPluginManager();
    }

    public void onBlockMove(Player player, Vec3i fr, Vec3i to) {
        final var event = new Event(player, fr, to);
        pluginMan.callEvent(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final var fr = new Vec3i(event.getPlayer().getLocation());
        final var to = new Vec3i(event.getRespawnLocation());
        if(fr.eq(to)) return;
        onBlockMove(event.getPlayer(), fr, to);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        final var fr = new Vec3i(event.getFrom());
        final var to = new Vec3i(event.getTo());
        if(fr.eq(to)) return;
        onBlockMove(event.getPlayer(), fr, to);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final var fr = new Vec3i(event.getFrom());
        final var to = new Vec3i(event.getTo());
        if(fr.eq(to)) return;
        onBlockMove(event.getPlayer(), fr, to);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final var fr = new Vec3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        final var to = new Vec3i(event.getPlayer().getLocation());
        onBlockMove(event.getPlayer(), fr, to);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final var fr = new Vec3i(event.getPlayer().getLocation());
        final var to = new Vec3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        onBlockMove(event.getPlayer(), fr, to);
    }

}
