package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.spec.CommandSpec;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public abstract class SfCommand {

    public abstract CommandSpec create(SkyFortressPlugin plugin);

}
