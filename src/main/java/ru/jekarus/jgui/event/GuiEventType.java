package ru.jekarus.jgui.event;

import org.spongepowered.api.event.Event;

import java.util.Objects;

public class GuiEventType {

    private final String name;
    private final Class<? extends Event> clazz;

    public GuiEventType(String name, Class<? extends Event> clazz)
    {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName()
    {
        return this.name;
    }

    public Class<? extends Event> getClazz()
    {
        return this.clazz;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        GuiEventType that = (GuiEventType) o;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(clazz);
    }
}
