package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.HashMap;
import java.util.Map;

public class SfTeamCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin)
    {

        Map<String, SfTeam> teamsMap = new HashMap<>();
        for (SfTeam team : plugin.getTeamContainer().getCollection())
        {
            teamsMap.put(team.getUniqueId(), team);
        }

        return CommandSpec.builder()
                .permission("sky_fortress_v3.team")
                .arguments(
                        GenericArguments.choices(Text.of("team_id"), teamsMap),
                        GenericArguments.optional(GenericArguments.player(Text.of("player_name")))
                )
                .executor((src, args) ->
                {
                    SfTeam team = args.<SfTeam>getOne("team_id").orElse(null);

                    if (team == null)
                    {
                        src.sendMessage(Text.of("Команда не найдена"));
                        return CommandResult.empty();
                    }

                    Player player = args.<Player>getOne("player_name").orElse(null);

                    if (player == null)
                    {
                        if (src instanceof Player)
                        {
                            player = (Player) src;
                        }
                        else
                        {
                            src.sendMessage(Text.of("Игрок не найден"));
                            return CommandResult.empty();
                        }
                    }

                    SfPlayer sfPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);
                    team.addPlayer(plugin, sfPlayer);

                    src.sendMessage(Text.of(sfPlayer.getName() + " joined " + team.getUniqueId()));

                    return CommandResult.success();
                })
                .build();
    }

}
