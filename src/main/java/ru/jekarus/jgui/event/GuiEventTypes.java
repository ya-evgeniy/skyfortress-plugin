package ru.jekarus.jgui.event;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuiEventTypes {

    private static final GuiEventTypes instance = new GuiEventTypes();

    private Map<String, GuiEventType> eventTypeByName = new HashMap<>();
    private Map<Class<? extends Event>, GuiEventType> eventTypeByClass = new HashMap<>(); // fixme

    public GuiEventTypes()
    {
        this.register(new GuiEventType("primary", ClickInventoryEvent.Primary.class));
    }

    public void register(GuiEventType eventType)
    {
        this.eventTypeByName.put(eventType.getName(), eventType);
        this.eventTypeByClass.put(eventType.getClazz(), eventType);
    }

    @Deprecated
    public void unregister(GuiEventType eventType)
    {
        this.eventTypeByName.remove(eventType.getName());
        this.eventTypeByClass.remove(eventType.getClazz());
    }

    public Optional<GuiEventType> from(String name)
    {
        return Optional.ofNullable(this.eventTypeByName.get(name.toLowerCase()));
    }

    public Optional<GuiEventType> from(Class<? extends Event> clazz)
    {
        return Optional.ofNullable(this.eventTypeByClass.get(clazz));
    }

    public static GuiEventTypes getInstance()
    {
        return instance;
    }

    public Collection<GuiEventType> asCollection()
    {
        return this.eventTypeByName.values();
    }
}
