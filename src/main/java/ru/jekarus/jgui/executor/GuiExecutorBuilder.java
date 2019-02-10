package ru.jekarus.jgui.executor;

public abstract class GuiExecutorBuilder<E extends GuiExecutor> {

    public abstract E build();

}
