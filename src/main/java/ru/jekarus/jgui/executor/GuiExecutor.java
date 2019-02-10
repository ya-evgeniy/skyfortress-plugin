package ru.jekarus.jgui.executor;

import ru.jekarus.jgui.argument.GuiArguments;

public abstract class GuiExecutor<T extends GuiExecutorBuilder> {

    public abstract GuiExecutorCreator<T> getCreator();

    public abstract void execute(GuiArguments arguments);

}
