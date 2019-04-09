package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLobbyMessages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        SfPlayers players = SfPlayers.getInstance();

        SfMessages messages = plugin.getMessages();
        SfLobbyMessages lobby = messages.getLobby();

        return CommandSpec.builder()
                .permission("sky_fortress_v3.team")
                .arguments(
                        GenericArguments.choices(Text.of("team_id"), teamsMap),
                        GenericArguments.optional(GenericArguments.player(Text.of("target"))),
                        GenericArguments.flags().flag("-no_tp").buildWith(GenericArguments.none())
                )
                .executor((src, args) ->
                {
                    if (!(src instanceof Player)) {
                        return CommandResult.empty();
                    }

                    Player player = (Player) src;
                    SfPlayer sfPlayer = players.getOrCreatePlayer(player);


//                    if (team == null)
//                    {
//                        lobby.commandPlayerTeamNotFound(sfPlayer, )
//                        src.sendMessage(Text.of("Команда не найдена"));
//                        return CommandResult.empty();
//                    }

                    SfTeam team = args.<SfTeam>getOne("team_id").orElse(null);
                    if (team == null) return CommandResult.empty();
                    Player target = args.<Player>getOne("target").orElse(null);
                    boolean needTp = !args.hasAny("no_tp");

                    if (target == null)
                    {
                        target = player;
                    }

                    SfPlayer sfTarget = players.getOrCreatePlayer(target);

                    if (team.getPlayers().contains(sfTarget)) {
                        if (sfPlayer == sfTarget) {
                            player.sendMessage(
                                    lobby.commandPlayerYouAlreadyInTeam(sfTarget)
                            );
                        }
                        else {
                            player.sendMessage(
                                    lobby.commandPlayerAlreadyInTeam(sfPlayer, sfTarget, team)
                            );
                        }
                    }
                    else {
                        List<Player> onlinePlayers = new ArrayList<>(Sponge.getServer().getOnlinePlayers());
                        onlinePlayers.remove(target);

                        if (sfPlayer == sfTarget) {
                            player.sendMessage(
                                    lobby.commandPlayerChangeSelfTeam(sfPlayer, team)
                            );
                            messages.sendToPlayers(
                                    onlinePlayers,
                                    lobby.commandGlobalSetSelfTeam(sfPlayer, team)
                            );
                            needTp = false;
                        }
                        else {
                            onlinePlayers.remove(player);
                            player.sendMessage(
                                    lobby.commandPlayerChangePlayerTeam(sfPlayer, sfTarget, team)
                            );
                            target.sendMessage(
                                    lobby.commandTargetSetTeam(sfTarget, sfPlayer, team)
                            );
                            messages.sendToPlayers(
                                    onlinePlayers,
                                    lobby.commandGlobalSetPlayerTeam(sfPlayer, sfTarget, team)
                            );
                        }

                        team.addPlayer(plugin, sfTarget);
                        boolean finded = false;
                        for (SfLobbyTeam lobbyTeam : plugin.getLobby().getTeams()) {
                            if (lobbyTeam.getSettings().team != team) continue;
                            if (needTp) {
                                teleport(plugin, target, sfTarget, lobbyTeam);
                                finded = true;
                            }
                            if (lobbyTeam.getSettings().captain != null) break;
                            lobbyTeam.getSettings().captain = sfTarget;
                        }
                        if (needTp && !finded) {
                            teleport(plugin, target, sfTarget, null);
                        }
                    }

                    return CommandResult.success();
                })
                .build();
    }

    private static void teleport(SkyFortressPlugin plugin, Player player, SfPlayer sfPlayer, SfLobbyTeam lobbyTeam) {
        if (lobbyTeam != null) {
            SfLobbyTeamSettings settings = lobbyTeam.getSettings();
            player.setLocationAndRotation(
                    settings.accepted.getLocation(),
                    settings.accepted.getRotation()
            );
            sfPlayer.setZone(PlayerZone.TEAM_ROOM);
            return;
        }

        SfTeam team = sfPlayer.getTeam();
        if (team.getType() == SfTeam.Type.NONE) {
            player.setLocationAndRotation(
                    plugin.getLobby().getSettings().center.getLocation(),
                    plugin.getLobby().getSettings().center.getRotation()
            );
            sfPlayer.setZone(PlayerZone.LOBBY);
            return;
        }

        if (team.getType() == SfTeam.Type.SPECTATOR) {

        }
    }

}
