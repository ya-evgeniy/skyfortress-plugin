package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.game.SfGame;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;

public class SfGameStartCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin)
    {
        SfGame game = plugin.getGame();
        return CommandSpec.builder()
                .permission("sky_fortress_v3.start")
                .executor((src, args) -> {
                    if (game.getStage() == SfGameStageType.PRE_GAME)
                    {
                        game.start();
                        return CommandResult.success();
                    }
                    return CommandResult.empty();
                })
                .build();
    }

}
