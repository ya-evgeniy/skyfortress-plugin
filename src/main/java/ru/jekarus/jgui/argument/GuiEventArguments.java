package ru.jekarus.jgui.argument;

import org.spongepowered.api.event.Event;
import ru.jekarus.jgui.event.GuiEventType;

public class GuiEventArguments {

    public Class<? extends Event> clazz = null;
    public GuiEventType type = null;
    public Event instance = null;

}
