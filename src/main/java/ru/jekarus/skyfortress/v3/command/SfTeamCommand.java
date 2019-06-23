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
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomSettings;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
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
                    PlayerData playerData = players.getOrCreatePlayer(player);


//                    if (team == null)
//                    {
//                        globalLobby.commandPlayerTeamNotFound(playerData, )
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

                    PlayerData sfTarget = players.getOrCreatePlayer(target);

                    if (team.getPlayers().contains(sfTarget)) {
                        if (playerData == sfTarget) {
                            player.sendMessage(
                                    lobby.commandPlayerYouAlreadyInTeam(sfTarget)
                            );
                        }
                        else {
                            player.sendMessage(
                                    lobby.commandPlayerAlreadyInTeam(playerData, sfTarget, team)
                            );
                        }
                    }
                    else {
                        List<Player> onlinePlayers = new ArrayList<>(Sponge.getServer().getOnlinePlayers());
                        onlinePlayers.remove(target);

                        if (playerData == sfTarget) {
                            player.sendMessage(
                                    lobby.commandPlayerChangeSelfTeam(playerData, team)
                            );
                            messages.sendToPlayers(
                                    onlinePlayers,
                                    lobby.commandGlobalSetSelfTeam(playerData, team)
                            );
                            needTp = false;
                        }
                        else {
                            onlinePlayers.remove(player);
                            player.sendMessage(
                                    lobby.commandPlayerChangePlayerTeam(playerData, sfTarget, team)
                            );
                            target.sendMessage(
                                    lobby.commandTargetSetTeam(sfTarget, playerData, team)
                            );
                            messages.sendToPlayers(
                                    onlinePlayers,
                                    lobby.commandGlobalSetPlayerTeam(playerData, sfTarget, team)
                            );
                        }

                        team.addPlayer(plugin, sfTarget);
                        boolean finded = false;
                        for (LobbyRoom room : plugin.getLobbyRoomsContainer().getRooms()) {
                            if (room.getState().getTeam() != team) continue;
                            if (needTp) {
                                teleport(plugin, target, sfTarget, room);
                                finded = true;
                            }
                            if (room.getState().getCaptain() != null) break;
                            room.getState().setCaptain(sfTarget);
                        }
                        if (needTp && !finded) {
                            teleport(plugin, target, sfTarget, null);
                        }
                    }

                    return CommandResult.success();
                })
                .build();
    }

    private static void teleport(SkyFortressPlugin plugin, Player player, PlayerData playerData, LobbyRoom room) {
        if (room != null) {
            LobbyRoomSettings settings = room.getSettings();
            player.setLocationAndRotation(
                    settings.getAccepted().getLocation(),
                    settings.getAccepted().getRotation()
            );
            playerData.setZone(PlayerZone.TEAM_ROOM);
            return;
        }

        SfTeam team = playerData.getTeam();
        if (team.getType() == SfTeam.Type.NONE) {
            player.setLocationAndRotation(
                    plugin.getSettings().getLobby().getCenter().getLocation(),
                    plugin.getSettings().getLobby().getCenter().getRotation()
            );
            playerData.setZone(PlayerZone.LOBBY);
            return;
        }

        if (team.getType() == SfTeam.Type.SPECTATOR) {

        }
    }

}
