package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.resource.ResourceContainer;

public class ResourcesEngine {

    private final SkyFortressPlugin plugin;
    private final ResourceContainer resourceContainer;

    private Task task;
    private boolean enabled = false;

    public ResourcesEngine(SkyFortressPlugin plugin, ResourceContainer resourceContainer) {
        this.plugin = plugin;
        this.resourceContainer = resourceContainer;
    }

    public void start() {
        if (this.enabled) {
            return;
        }
        this.enabled = true;
        this.task = Task.builder()
                .name(this.getClass().getName())
                .execute(this::run)
                .intervalTicks(1)
                .submit(this.plugin);
    }

    public void stop() {
        if (!this.enabled) {
            return;
        }
        this.enabled = false;
        this.task.cancel();
    }

    private void run() {
        this.resourceContainer.tick();
    }
}
