package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.spec.CommandSpec;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.settings.SfLobbySettingsCommand;

public class SfSettingsCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin) {
        return CommandSpec.builder()
                .permission("skyfortress.settings")
                .child(new SfLobbySettingsCommand().create(plugin), "lobby")
                .build();
    }

}
