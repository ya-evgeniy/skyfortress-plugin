package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.resource.SfResource;
import ru.jekarus.skyfortress.v3.resource.SfResourceContainer;

public class ResourcesEngine {

    private final SkyFortressPlugin plugin;
    private final SfResourceContainer container;

    private Task task;
    private boolean enabled = false;

    public ResourcesEngine(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.container = this.plugin.getResourceContainer();
    }

    public void start()
    {
        if (this.enabled)
        {
            return;
        }
        this.enabled = true;
        this.task = Task.builder()
                .name(this.getClass().getName())
                .execute(this::run)
                .intervalTicks(1)
                .submit(this.plugin);
    }

    public void stop()
    {
        if (!this.enabled)
        {
            return;
        }
        this.enabled = false;
        this.task.cancel();
    }

    private void run()
    {
        for (SfResource resource : this.container.getCollection())
        {
            resource.tick();
        }
    }

}
