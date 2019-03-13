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
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import static org.spongepowered.api.command.args.GenericArguments.*;

public class SfLobbySettingsCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin) {
        SfLobbySettings settings = plugin.getLobby().getSettings();

        CommandFlags.Builder builder = GenericArguments.flags();
        builder.valueFlag(optional(bool(Text.of("can_spectate_value"))), "-can_spectate");
        builder.valueFlag(optional(bool(Text.of("can_join_value"))), "-can_join");
        builder.valueFlag(optional(bool(Text.of("can_leave_value"))), "-can_leave");
        builder.valueFlag(optional(bool(Text.of("can_ready_value"))), "-can_ready");
        builder.valueFlag(optional(bool(Text.of("can_unready_value"))), "-can_unready");
        builder.valueFlag(optional(bool(Text.of("can_accept_value"))), "-can_accept");
        builder.valueFlag(optional(bool(Text.of("can_cancel_value"))), "-can_cancel");
        builder.valueFlag(optional(bool(Text.of("use_lobby_captain_system_value"))), "-use_lobby_captain_system");
        builder.valueFlag(optional(location(Text.of("center_value"))), "-center");
        builder.valueFlag(optional(doubleNum(Text.of("min_y_value"))),"-min_y");

        return CommandSpec.builder()
                .arguments(builder.buildWith(none()))
                .executor((src, args) -> {
                    args.getOne("can_spectate_value").ifPresent(value -> {
                        settings.canSpectate = (Boolean) value;
                    });
                    args.getOne("can_join_value").ifPresent(value -> {
                        settings.canJoin = (Boolean) value;
                    });
                    args.getOne("can_leave_value").ifPresent(value -> {
                        settings.canLeave = (Boolean) value;
                    });
                    args.getOne("can_ready_value").ifPresent(value -> {
                        settings.canReady = (Boolean) value;
                    });
                    args.getOne("can_unready_value").ifPresent(value -> {
                        settings.canUnready = (Boolean) value;
                    });
                    args.getOne("can_accept_value").ifPresent(value -> {
                        settings.canAccept = (Boolean) value;
                    });
                    args.getOne("can_cancel_value").ifPresent(value -> {
                        settings.canCancel = (Boolean) value;
                    });
                    args.getOne("use_lobby_captain_system_value").ifPresent(value -> {
                        settings.useLobbyCaptainSystem = (Boolean) value;
                    });
                    args.getOne("center_value").ifPresent(value -> {
                        settings.center = new SfLocation((Location<World>) value, settings.center.getRotation());
                    });
                    args.getOne("min_y_value").ifPresent(value -> {
                        settings.min_y = (Double) value;
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
//                        if (value.isPresent()) settings.canCancel = value.get();
//                        else src.sendMessage(Text.of("can_cancel = " + settings.canCancel));
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
//                        if (value.isPresent()) settings.center = new SfLocation(value.get(), settings.center.getRotation());
//                        else src.sendMessage(Text.of("center = " + settings.center));
//                    }
//                    if (args.hasAny("min_y")) {
//                        System.out.println("has min_y");
//                        Optional<Double> value = args.getOne("min_y_value");
//                        if (value.isPresent()) settings.min_y = value.get();
//                        else src.sendMessage(Text.of("min_y = " + settings.min_y));
//                    }
                    return CommandResult.success();
                })
                .build();
    }



}
