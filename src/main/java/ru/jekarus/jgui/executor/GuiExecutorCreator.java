package ru.jekarus.jgui.executor;

import ninja.leaping.configurate.ConfigurationNode;

public abstract class GuiExecutorCreator<B extends GuiExecutorBuilder> {

    private final String name;

    public GuiExecutorCreator(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public abstract B builder();

    public abstract GuiExecutor deserialize(ConfigurationNode namedNode, ConfigurationNode executorNode);

    public abstract void serialize(ConfigurationNode executorNode, GuiExecutor executor);

}
