package ru.jekarus.skyfortress;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameStopEvent extends org.bukkit.event.Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameStopEvent() {

    }

}
