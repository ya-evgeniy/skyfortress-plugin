package ru.jekarus.jgui.executor;

import ru.jekarus.jgui.executor.creator.RunCommandExecutorCreator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuiExecutors {

    private static final GuiExecutors instance = new GuiExecutors();

    private Map<String, GuiExecutorCreator> executorCreatorByName = new HashMap<>();

    public GuiExecutors()
    {
        this.register(new RunCommandExecutorCreator());
    }

    public void register(GuiExecutorCreator creator)
    {
        this.executorCreatorByName.put(creator.getName(), creator);
    }

    public void unregister(GuiExecutorCreator creator)
    {
        this.executorCreatorByName.remove(creator.getName());
    }

    public Optional<? extends GuiExecutorCreator> from(String name)
    {
        return Optional.ofNullable(this.executorCreatorByName.get(name.toLowerCase()));
    }

    public Collection<GuiExecutorCreator> asCollection()
    {
        return this.executorCreatorByName.values();
    }

    public static GuiExecutors getInstance()
    {
        return instance;
    }
}
