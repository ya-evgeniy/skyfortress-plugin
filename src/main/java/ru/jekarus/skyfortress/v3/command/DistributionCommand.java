package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.spec.CommandSpec;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.distribution.captain.CaptainDistributionCommand;

public class DistributionCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin) {
        return CommandSpec.builder()
                .child(
                    new CaptainDistributionCommand(plugin).create(plugin), "captain"
                )
                .build();
    }

}
