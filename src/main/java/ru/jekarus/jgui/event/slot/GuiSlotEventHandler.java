package ru.jekarus.jgui.event.slot;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.event.GuiListener;
import ru.jekarus.jgui.gui.slot.GuiSlot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GuiSlotEventHandler {

    private Map<Class<? extends Event>, GuiSlotEventMethodInvoker> methods = new HashMap<>();

    public void clear()
    {
        this.methods.clear();
    }

    public void updateClass(Class<? extends GuiSlot> clazz)
    {
        this.clear();
        for (Method method : clazz.getMethods())
        {
            GuiListener annotation = method.getAnnotation(GuiListener.class);
            if (annotation == null)
            {
                continue;
            }
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length < 1)
            {
                continue;
            }

            boolean needArguments = false;

            Class<?> eventParameter = parameters[0];
            if (!ClickInventoryEvent.class.isAssignableFrom(eventParameter))
            {
                continue;
            }

            Class<?> argumentsParameter;
            if (parameters.length > 1)
            {
                argumentsParameter = parameters[1];
                if (GuiArguments.class.isAssignableFrom(argumentsParameter))
                {
                    needArguments = true;
                }
            }

            this.methods.put((Class<? extends Event>) eventParameter, new GuiSlotEventMethodInvoker(method, needArguments));
        }
    }

    public <E extends Event> void handleEvent(Object object, GuiArguments arguments)
    {
        GuiSlotEventMethodInvoker method = this.methods.get(arguments.event.clazz);
        if (method != null)
        {
            try
            {
                method.invoke(object, arguments);
            }
            catch (InvocationTargetException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

}
