package ru.jekarus.jgui.executor.builder;

import ru.jekarus.jgui.executor.GuiExecutorBuilder;
import ru.jekarus.jgui.executor.creator.RunCommandExecutorCreator;
import ru.jekarus.jgui.executor.typed.RunCommandExecutor;

public class RunCommandExecutorBuilder extends GuiExecutorBuilder<RunCommandExecutor> {

    private final RunCommandExecutorCreator creator;
    private String command;

    public RunCommandExecutorBuilder(RunCommandExecutorCreator creator)
    {
        this.creator = creator;
    }

    public RunCommandExecutorBuilder command(String command)
    {
        this.command = command;
        return this;
    }

    @Override
    public RunCommandExecutor build()
    {
        return new RunCommandExecutor(this.creator, this.command);
    }

}
