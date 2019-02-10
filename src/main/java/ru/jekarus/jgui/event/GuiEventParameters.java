package ru.jekarus.jgui.event;

import org.spongepowered.api.entity.living.player.Player;

public class GuiEventParameters {

    public GuiEventType eventType = null;
    public Player player = null;

    public GuiEventParameters(Player player)
    {
        this.player = player;
    }

}
