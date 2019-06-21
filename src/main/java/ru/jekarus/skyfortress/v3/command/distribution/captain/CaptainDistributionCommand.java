package ru.jekarus.skyfortress.v3.command.distribution.captain;

import lombok.val;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.SfCommand;
import ru.jekarus.skyfortress.v3.distribution.Distribution;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.distribution.captain.CaptainController;
import ru.jekarus.skyfortress.v3.distribution.captain.CaptainSettings;
import ru.jekarus.skyfortress.v3.lang.SfDistributionMessages;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class CaptainDistributionCommand extends SfCommand {

    private final SkyFortressPlugin plugin;

    private CaptainSettings settings;

    public CaptainDistributionCommand(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.settings = new CaptainSettings(plugin);
        this.settings.reset();
    }

    @Override
    public CommandSpec create(SkyFortressPlugin plugin) {
        return CommandSpec.builder()
                .child(
                        new Start(this).create(plugin), "start"
                )
                .child(
                        new Cancel().create(plugin), "cancel"
                )
                .child(
                        new Set(this).create(plugin), "set"
                )
                .child(
                        new Setup(this).create(plugin), "setup"
                )
                .child(
                        new Reset(this).create(plugin), "reset"
                )
                .child(
                        new Info(this).create(plugin), "info"
                )
                .child(
                        new SelectNext(this).create(plugin), "select_next"
                )
                .build();
    }

    public static class Start extends SfCommand {

        private final CaptainDistributionCommand command;

        public Start(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {

            Map<String, CaptainSettings.Selector> replacedOfflineTargets = new HashMap<>();
            replacedOfflineTargets.put(CaptainSettings.DISABLED_SELECTOR.getType().name(), CaptainSettings.DISABLED_SELECTOR);
            replacedOfflineTargets.put(CaptainSettings.RANDOM_SELECTOR.getType().name(), CaptainSettings.RANDOM_SELECTOR);

            return CommandSpec.builder()
                    .arguments(
                            GenericArguments.flags()
                                    .flag("-use_existing_teams")
                                    .valueFlag(GenericArguments.choices(Text.of("offline"), replacedOfflineTargets))
                                    .buildWith(GenericArguments.none())
                    )
                    .executor((src, args) -> {

                        if (plugin.getDistributionController().isEnabled()) {
                            throw new CommandException(Text.of(
                                    "Distribution already runned..."
                            ));
                        }

                        List<SfGameTeam> offlineTeamCaptains = this.command.settings.getOfflineTeamCaptains();

                        if (!offlineTeamCaptains.isEmpty()) {
                            Optional<CaptainSettings.Selector> optionalReplaceTarget = args.getOne("offline");
                            if (optionalReplaceTarget.isPresent()) {
                                CaptainSettings.Selector selector = optionalReplaceTarget.get();
                                for (SfGameTeam offlineTeam : offlineTeamCaptains) {
                                    this.command.settings.updateSelector(offlineTeam, selector);
                                }
                            }
                            else {
                                throw new CommandException(Text.of(
                                        "Captains in teams " + offlineTeamCaptains.toString() + " is offline. Use flag '--offline=[random, disable]' to start distribution"
                                ));
                            }
                        }

                        this.command.settings.setUseExistingTeams(args.hasAny("use_existing_teams"));

                        plugin.getDistributionController().runCaptain(
                                this.command.settings,
                                (captainController, resultMessage) -> {
                                    switch (resultMessage) {
                                        case ALREADY_STARTED:
                                            src.sendMessage(Text.of("Already started"));
                                            break;
                                        case LOAD_CONFIG:
                                            src.sendMessage(Text.of("Load config..."));
                                            break;
                                        case ERROR_CONFIG:
                                            src.sendMessage(Text.of("Config parse error"));
                                            break;
                                        case START_DISTRIBUTION:
                                            src.sendMessage(Text.of("Starting..."));
                                            break;
                                    }
                                }
                        );

                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class Cancel extends SfCommand {

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {
            return CommandSpec.builder()
                    .arguments(
                            GenericArguments.flags()
                                .flag("-save_teams")
                                .buildWith(GenericArguments.none())
                    )
                    .executor((src, args) -> {

                        DistributionController controller = plugin.getDistributionController();
                        if (controller.isEnabled()) {
                            controller.cancelCaptain(args.hasAny("save_teams"));
                        }

                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class Set extends SfCommand {

        private final CaptainDistributionCommand command;

        public Set(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {
            Map<String, SfGameTeam> params = new HashMap<>();
            for (SfGameTeam gameTeam : plugin.getTeamContainer().getGameCollection()) {
                params.put(gameTeam.getUniqueId(), gameTeam);
            }
            return CommandSpec.builder()
                    .arguments(
                            GenericArguments.choices(Text.of("team"), params),
                            GenericArguments.flags()
                                    .flag("-disabled")
                                    .flag("-random")
                                    .valueFlag(GenericArguments.player(Text.of("player")), "-player")
                                    .valueFlag(GenericArguments.string(Text.of("offline")), "-debug")
                                    .buildWith(GenericArguments.none())
                    )
                    .executor((src, args) -> {

                        Optional<SfGameTeam> optionalGameTeam = args.getOne("team");
                        if (!optionalGameTeam.isPresent()) {
                            throw new CommandException(Text.of("Empty or unknown team", true));
                        }
                        SfGameTeam gameTeam = optionalGameTeam.get();

                        Optional<Player> optionalPlayer = args.getOne("player");
                        if (optionalPlayer.isPresent()) {
                            this.command.settings.updateSelector(
                                    gameTeam,
                                    new CaptainSettings.PlayerSelector(
                                            SfPlayers.getInstance().getOrCreatePlayer(optionalPlayer.get())
                                    )
                            );

                            return CommandResult.success();
                        }

                        Optional<String> optionalOffline = args.getOne("offline");
                        if (optionalOffline.isPresent()) {
                            String name = optionalOffline.get();
                            this.command.settings.updateSelector(
                                    gameTeam,
                                    new CaptainSettings.DebugSelector(name)
                            );
                            return CommandResult.success();
                        }

                        if (args.hasAny("random")) {
                            this.command.settings.updateSelector(gameTeam, CaptainSettings.RANDOM_SELECTOR);
                        }
                        else if (args.hasAny("disabled")) {
                            this.command.settings.updateSelector(gameTeam, CaptainSettings.DISABLED_SELECTOR);
                        }
                        else {
                            throw new CommandException(Text.of(
                                    "Not enough arguments"
                            ));
                        }

                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class Setup extends SfCommand {

        private final CaptainDistributionCommand command;

        public Setup(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {

//            Map<String, CaptainSettings.Selector> defaultTargetValues = new HashMap<>();
//            defaultTargetValues.put(CaptainSettings.DISABLED_SELECTOR.getType().name(), CaptainSettings.DISABLED_SELECTOR);
//            defaultTargetValues.put(CaptainSettings.RANDOM_SELECTOR.getType().name(), CaptainSettings.RANDOM_SELECTOR);

            return CommandSpec.builder()
                    .arguments(
                            GenericArguments.flags()
                            .flag("-random")
                            .flag("-disabled")
                            .buildWith(GenericArguments.none())
                    )
                    .executor((src, args) -> {

                        boolean randomFlag = args.hasAny("random");
                        boolean disabledFlag = args.hasAny("disabled");

                        if (!randomFlag && !disabledFlag) {

                            return CommandResult.empty();
                        }

                        if (randomFlag) {
                            this.command.settings.reset(CaptainSettings.RANDOM_SELECTOR);
                            this.command.settings.setDefaultSelector(CaptainSettings.RANDOM_SELECTOR);
                        }
                        else {
                            this.command.settings.reset(CaptainSettings.DISABLED_SELECTOR);
                            this.command.settings.setDefaultSelector(CaptainSettings.DISABLED_SELECTOR);
                        }

                        Random random = new Random();
                        for (LobbyRoom room : plugin.getLobbyRoomsContainer().getRooms()) {
                            SfGameTeam gameTeam = room.getState().getTeam();
                            if (gameTeam.getPlayers().isEmpty()) {
                                continue;
                            }
                            SfPlayer captain = room.getState().getCaptain();
                            if (captain == null) {
                                ArrayList<SfPlayer> teamPlayers = new ArrayList<>(gameTeam.getPlayers());
                                int captainIndex = random.nextInt(teamPlayers.size());
                                this.command.settings.updateSelector(
                                        gameTeam,
                                        new CaptainSettings.PlayerSelector(
                                                teamPlayers.get(captainIndex)
                                        )
                                );
                            }
                            else {
                                this.command.settings.updateSelector(
                                        gameTeam,
                                        new CaptainSettings.PlayerSelector(
                                                captain
                                        )
                                );
                            }
                        }

                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class Reset extends SfCommand {

        private final CaptainDistributionCommand command;

        public Reset(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {
            return CommandSpec.builder()
                    .executor((src, args) -> {
                        this.command.settings.reset();
                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class Info extends SfCommand {

        private final CaptainDistributionCommand command;

        public Info(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {
            return CommandSpec.builder()
                    .executor((src, args) -> {

                        Map<SfGameTeam, CaptainSettings.Selector> selectorByTeam = this.command.settings.getSelectorByTeam();

                        if (!(src instanceof Player)) {
                            for (Map.Entry<SfGameTeam, CaptainSettings.Selector> entry : selectorByTeam.entrySet()) {
                                src.sendMessage(Text.of(TextColors.GRAY,
                                        entry.getKey().getUniqueId() + " - " + entry.getValue()
                                ));
                            }
                            return CommandResult.empty();
                        }

                        Player player = (Player) src;
                        SfPlayer sfPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);


                        List<SfTeam> disabledTeams = new ArrayList<>();
                        HashMap<SfGameTeam, CaptainSettings.Selector> enabledTeams = new HashMap<>();

                        for (Map.Entry<SfGameTeam, CaptainSettings.Selector> entry : selectorByTeam.entrySet()) {
                            SfGameTeam team = entry.getKey();
                            CaptainSettings.Selector selector = entry.getValue();

                            if (selector.getType() == CaptainSettings.SelectorType.DISABLED) {
                                disabledTeams.add(team);
                            }
                            else {
                                enabledTeams.put(team, selector);
                            }
                        }

                        SfDistributionMessages distribution = plugin.getMessages().getDistribution();

                        List<Text> settings = new ArrayList<>();

                        for (Map.Entry<SfGameTeam, CaptainSettings.Selector> entry : enabledTeams.entrySet()) {
                            SfGameTeam team = entry.getKey();
                            CaptainSettings.Selector selector = entry.getValue();

                            if (selector.getType() == CaptainSettings.SelectorType.RANDOM) {
                                Text randomText = distribution.commandInfoRandom(sfPlayer, team);
                                settings.add(randomText);
                            }
                            else {
                                CaptainSettings.PlayerSelector playerSelector = (CaptainSettings.PlayerSelector) selector;
                                Text playerText = distribution.commandInfoPlayer(sfPlayer, playerSelector.getPlayer(), team);
                                settings.add(playerText);
                            }
                        }

                        Text header = distribution.commandInfoHeader(sfPlayer);
                        Text disabled = null;
                        if (disabledTeams.size() > 0) {
                            disabled = distribution.commandInfoDisabled(sfPlayer, disabledTeams);
                        }

                        player.sendMessage(header);
                        for (Text setting : settings) {
                            player.sendMessage(setting);
                        }
                        if (disabled != null) {
                            player.sendMessage(disabled);
                        }

                        return CommandResult.success();
                    })
                    .build();
        }

    }

    public static class SelectNext extends SfCommand {

        private final CaptainDistributionCommand command;

        public SelectNext(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {
            return CommandSpec.builder()
                    .executor((src, args) -> {

                        final val distributionController = plugin.getDistributionController();
                        final val current = distributionController.getCurrent();
                        if (current == null) {
                            System.out.println("current");
                            return CommandResult.success();
                        }

                        if (current.getType() != Distribution.Type.CAPTAIN) {
                            System.out.println("not captain");
                            return CommandResult.success();
                        }

                        final val captainController = (CaptainController) current;
                        final val distribution = captainController.getDistribution();

                        if (distribution == null) {
                            System.out.println("dist");
                            return CommandResult.success();
                        }

                        distribution.getCaptainRandomizer().randomSelect();

                        return CommandResult.success();
                    })
                    .build();
        }

    }

}
