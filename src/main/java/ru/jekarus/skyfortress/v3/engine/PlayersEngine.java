package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;

public class PlayersEngine {

    private final SkyFortressPlugin plugin;
    private final SfPlayers players;

    private boolean enabled = false;
    private Task task;

    public PlayersEngine(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.players = SfPlayers.getInstance();
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

    private void run() {
        for (SfPlayer sfPlayer : this.players.asList()) {
            if (sfPlayer.captureMessageTimeout > 0) sfPlayer.captureMessageTimeout--;
        }
    }

}
