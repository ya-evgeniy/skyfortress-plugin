package ru.jekarus.jgui.event.slot;

import ru.jekarus.jgui.argument.GuiArguments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GuiSlotEventMethodInvoker {

    private final Method method;
    private final boolean needArguments;

    public GuiSlotEventMethodInvoker(Method method, boolean needArguments)
    {
        this.method = method;
        this.needArguments = needArguments;
    }

    public void invoke(Object object, GuiArguments arguments)
            throws InvocationTargetException, IllegalAccessException
    {
        if (this.needArguments)
        {
            this.method.invoke(object, arguments.event.instance, arguments);
        }
        else
        {
            this.method.invoke(object, arguments.event.instance);
        }
    }

}
