package ru.jekarus.jgui.argument;

import org.spongepowered.api.entity.living.player.Player;

public class GuiArguments {

    public Player player = null;
    public GuiEventArguments event = new GuiEventArguments();

    public GuiArguments(Player player)
    {

        this.player = player;

    }

}
