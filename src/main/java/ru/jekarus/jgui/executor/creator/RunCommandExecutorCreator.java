package ru.jekarus.jgui.executor.creator;

import ninja.leaping.configurate.ConfigurationNode;
import ru.jekarus.jgui.executor.GuiExecutor;
import ru.jekarus.jgui.executor.GuiExecutorCreator;
import ru.jekarus.jgui.executor.builder.RunCommandExecutorBuilder;
import ru.jekarus.jgui.executor.typed.RunCommandExecutor;

public class RunCommandExecutorCreator extends GuiExecutorCreator<RunCommandExecutorBuilder> {

    public static final String NAME = "run_command";

    public RunCommandExecutorCreator()
    {
        super(NAME);
    }

    @Override
    public RunCommandExecutorBuilder builder()
    {
        return new RunCommandExecutorBuilder(this);
    }

    @Override
    public GuiExecutor deserialize(ConfigurationNode namedNode, ConfigurationNode executorNode)
    {
        String command = namedNode.getString();
        return new RunCommandExecutor(this, command);
    }

    @Override
    public void serialize(ConfigurationNode executorNode, GuiExecutor executor)
    {
        if (executor instanceof RunCommandExecutor)
        {
            RunCommandExecutor runCommandExecutor = (RunCommandExecutor) executor;
            executorNode.getNode(NAME).setValue(runCommandExecutor.getCommand());
        }
    }

}
