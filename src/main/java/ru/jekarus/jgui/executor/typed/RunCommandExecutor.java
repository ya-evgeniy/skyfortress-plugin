package ru.jekarus.jgui.executor.typed;

import org.spongepowered.api.Sponge;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.executor.GuiExecutor;
import ru.jekarus.jgui.executor.GuiExecutorCreator;
import ru.jekarus.jgui.executor.creator.RunCommandExecutorCreator;

public class RunCommandExecutor extends GuiExecutor {

    private final RunCommandExecutorCreator creator;
    private final String command;

    public RunCommandExecutor(RunCommandExecutorCreator creator, String command)
    {
        this.creator = creator;
        this.command = command;
    }

    public String getCommand()
    {
        return this.command;
    }

    @Override
    public GuiExecutorCreator getCreator()
    {
        return this.creator;
    }

    @Override
    public void execute(GuiArguments arguments)
    {
        Sponge.getCommandManager().process(arguments.player, this.command);
    }

}
