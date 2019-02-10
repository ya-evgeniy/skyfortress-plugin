package ru.jekarus.jgui.executor;

import ru.jekarus.jgui.argument.GuiArguments;

import java.util.ArrayList;
import java.util.Collection;

public class GuiExecutorContainer extends GuiExecutor {

    private Collection<GuiExecutor> executors = new ArrayList<>();

    public GuiExecutorContainer()
    {

    }

    public void addExecutor(GuiExecutor executor)
    {
        this.executors.add(executor);
    }

    public void removeExecutor(GuiExecutor executor)
    {
        this.executors.remove(executor);
    }

    @Override
    public GuiExecutorCreator getCreator()
    {
        return null; // fixme
    }

    @Override
    public void execute(GuiArguments arguments)
    {
        this.executors.forEach(executor -> {executor.execute(arguments);});
    }

    public Collection<GuiExecutor> asCollection()
    {
        return this.executors;
    }
}
