package ru.jekarus.jgui.gui.slot;

import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.event.GuiEventType;
import ru.jekarus.jgui.event.GuiListener;
import ru.jekarus.jgui.executor.GuiExecutor;
import ru.jekarus.jgui.executor.GuiExecutorContainer;
import ru.jekarus.jgui.gui.slot.item.GuiItem;

import java.util.HashMap;
import java.util.Map;

public class ExecutorGuiSlot extends GuiSlot {

    private Map<GuiEventType, GuiExecutorContainer> executorContainerByType = new HashMap<>();

    public ExecutorGuiSlot(GuiItem item)
    {
        super(item);
    }

    public ExecutorGuiSlot(int index, GuiItem item)
    {
        super(index, item);
    }

    public Map<GuiEventType, GuiExecutorContainer> getExecutors()
    {
        return this.executorContainerByType;
    }

    public void setExecutors(GuiEventType eventType, GuiExecutorContainer container)
    {
        this.executorContainerByType.put(eventType, container);
    }

    public void addExecutor(GuiEventType type, GuiExecutor executor)
    {
        GuiExecutorContainer container = this.executorContainerByType.get(type);
        if (container == null)
        {
            container = new GuiExecutorContainer();
            this.executorContainerByType.put(type, container);
        }
        container.addExecutor(executor);
    }

    public void removeExecutor(GuiEventType type, GuiExecutor executor)
    {
        GuiExecutorContainer container = this.executorContainerByType.get(type);
        if (container == null)
        {
            return;
        }
        container.removeExecutor(executor);
    }

    private void tryExecute(GuiArguments arguments)
    {
        if (arguments.event.type != null)
        {
            GuiExecutorContainer container = this.executorContainerByType.get(arguments.event.type);
            if (container != null)
            {
                container.execute(arguments);
            }
        }
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Primary event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Middle event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Secondary event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Shift.Primary event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Shift.Secondary event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Double event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Drop.Single event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Drop.Full event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.NumberPress event, GuiArguments arguments)
    {
        this.tryExecute(arguments);
    }

}
