package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class SfCommandManager {

    private final SkyFortressPlugin plugin;

    public SfCommandManager(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void init()
    {
        CommandSpec sfCmd = CommandSpec.builder()
                .child(
                        new SfTeamCommand().create(this.plugin), "team"
                )
                .child(
                        new SfGameStartCommand().create(this.plugin), "start"
                )
                .child(
                        new SfLanguageCommand().create(this.plugin), "language"
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, sfCmd, "skyfortress", "sf");
    }

}
