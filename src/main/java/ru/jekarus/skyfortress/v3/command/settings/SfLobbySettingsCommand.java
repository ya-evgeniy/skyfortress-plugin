package ru.jekarus.skyfortress.v3.command.settings;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandFlags;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.SfCommand;
import ru.jekarus.skyfortress.v3.settings.GlobalLobbySettings;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;
import ru.jekarus.skyfortress.v3.settings.SettingsContainer;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import static org.spongepowered.api.command.args.GenericArguments.bool;
import static org.spongepowered.api.command.args.GenericArguments.doubleNum;
import static org.spongepowered.api.command.args.GenericArguments.location;
import static org.spongepowered.api.command.args.GenericArguments.none;
import static org.spongepowered.api.command.args.GenericArguments.optional;

public class SfLobbySettingsCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin) {
        final SettingsContainer settings = plugin.getSettings();
        final GlobalLobbySettings globalLobbySettings = settings.getGlobalLobby();
        final LobbySettings lobbySettings = settings.getLobby();

        CommandFlags.Builder builder = GenericArguments.flags();
//        builder.valueFlag(optional(bool(Text.of("can_spectate_value"))), "-can_spectate");
        builder.valueFlag(optional(bool(Text.of("can_join_value"))), "-can_join");
        builder.valueFlag(optional(bool(Text.of("can_leave_value"))), "-can_leave");
        builder.valueFlag(optional(bool(Text.of("can_ready_value"))), "-can_ready");
        builder.valueFlag(optional(bool(Text.of("can_unready_value"))), "-can_unready");
        builder.valueFlag(optional(bool(Text.of("can_accept_value"))), "-can_accept");
        builder.valueFlag(optional(bool(Text.of("can_cancel_value"))), "-can_cancel");
        builder.valueFlag(optional(bool(Text.of("use_lobby_captain_system_value"))), "-use_lobby_captain_system");
        builder.valueFlag(optional(location(Text.of("center_value"))), "-center");
        builder.valueFlag(optional(doubleNum(Text.of("min_y_value"))),"-minY");

        return CommandSpec.builder()
                .arguments(builder.buildWith(none()))
                .executor((src, args) -> {
//                    args.getOne("can_spectate_value").ifPresent(value -> {
//                        settings.set;
//                    });
                    args.getOne("can_join_value").ifPresent(value -> {
                        globalLobbySettings.setCanJoinTeam((Boolean) value);
                    });
                    args.getOne("can_leave_value").ifPresent(value -> {
                        globalLobbySettings.setCanLeaveTeam((Boolean) value);
                    });
                    args.getOne("can_ready_value").ifPresent(value -> {
                        globalLobbySettings.setCanSetReady((Boolean) value);
                    });
                    args.getOne("can_unready_value").ifPresent(value -> {
                        globalLobbySettings.setCanSetUnready((Boolean) value);
                    });
                    args.getOne("can_accept_value").ifPresent(value -> {
                        globalLobbySettings.setCanUseAcceptButton((Boolean) value);
                    });
                    args.getOne("can_cancel_value").ifPresent(value -> {
                        globalLobbySettings.setCanUseDenyButton((Boolean) value);
                    });
                    args.getOne("use_lobby_captain_system_value").ifPresent(value -> {
                        globalLobbySettings.setUseLobbyCaptainSystem((Boolean) value);
                    });
                    args.getOne("center_value").ifPresent(value -> {
                        lobbySettings.setCenter(
                                new LocationAndRotation((Location<World>) value, lobbySettings.getCenter().getRotation())
                        );
                    });
                    args.getOne("min_y_value").ifPresent(value -> {
                        lobbySettings.setMinY((Double) value);
                    });
//                    if (args.hasAny("can_spectate")) {
//                        System.out.println("has can_spectate");
//                        Optional<Boolean> value = args.getOne("can_spectate_value");
//                        if (value.isPresent()) settings.canSpectate = value.get();
//                        else src.sendMessage(Text.of("can_spectate = " + settings.canSpectate));
//                    }
//                    if (args.hasAny("can_join")) {
//                        System.out.println("has can_join");
//                        Optional<Boolean> value = args.getOne("can_join_value");
//                        if (value.isPresent()) settings.canJoin = value.get();
//                        else src.sendMessage(Text.of("can_join = " + settings.canJoin));
//                    }
//                    if (args.hasAny("can_leave")) {
//                        System.out.println("has can_leave");
//                        Optional<Boolean> value = args.getOne("can_leave_value");
//                        if (value.isPresent()) settings.canLeave = value.get();
//                        else src.sendMessage(Text.of("can_leave = " + settings.canLeave));
//                    }
//                    if (args.hasAny("can_ready")) {
//                        System.out.println("has can_ready");
//                        Optional<Boolean> value = args.getOne("can_ready_value");
//                        if (value.isPresent()) settings.canReady = value.get();
//                        else src.sendMessage(Text.of("can_ready = " + settings.canReady));
//                    }
//                    if (args.hasAny("can_unready")) {
//                        System.out.println("has can_unready");
//                        Optional<Boolean> value = args.getOne("can_unready_value");
//                        if (value.isPresent()) settings.canUnready = value.get();
//                        else src.sendMessage(Text.of("can_unready = " + settings.canUnready));
//                    }
//                    if (args.hasAny("can_cancel")) {
//                        System.out.println("has can_cancel");
//                        Optional<Boolean> value = args.getOne("can_cancel_value");
//                        if (value.isPresent()) settings.canDeny = value.get();
//                        else src.sendMessage(Text.of("can_cancel = " + settings.canDeny));
//                    }
//                    if (args.hasAny("use_lobby_captain_system")) {
//                        System.out.println("has use_lobby_captain_system");
//                        Optional<Boolean> value = args.getOne("use_lobby_captain_system_value");
//                        if (value.isPresent()) settings.useLobbyCaptainSystem = value.get();
//                        else src.sendMessage(Text.of("use_lobby_captain_system = " + settings.useLobbyCaptainSystem));
//                    }
//                    if (args.hasAny("center")) {
//                        System.out.println("has center");
//                        Optional<Location<World>> value = args.getOne("center_value");
//                        if (value.isPresent()) settings.center = new LocationAndRotation(value.get(), settings.center.getRotation());
//                        else src.sendMessage(Text.of("center = " + settings.center));
//                    }
//                    if (args.hasAny("minY")) {
//                        System.out.println("has minY");
//                        Optional<Double> value = args.getOne("min_y_value");
//                        if (value.isPresent()) settings.minY = value.get();
//                        else src.sendMessage(Text.of("minY = " + settings.minY));
//                    }
                    return CommandResult.success();
                })
                .build();
    }



}
