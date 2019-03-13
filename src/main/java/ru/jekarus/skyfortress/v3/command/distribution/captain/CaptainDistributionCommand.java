package ru.jekarus.skyfortress.v3.command.distribution.captain;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.SfCommand;
import ru.jekarus.skyfortress.v3.distribution.DistributionController;
import ru.jekarus.skyfortress.v3.lang.SfDistributionMessages;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.util.*;

public class CaptainDistributionCommand extends SfCommand {

    private static final Target RANDOM = new Target(Target.Type.RANDOM);
    private static final Target DISABLED = new Target(Target.Type.DISABLED);

    private final SkyFortressPlugin plugin;

    private Map<SfGameTeam, Target> targetsByTeam = new HashMap<>();

    public CaptainDistributionCommand(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.reset();
    }

    public void reset() {
        SfTeamContainer teamContainer = this.plugin.getTeamContainer();
        for (SfGameTeam gameTeam : teamContainer.getGameCollection()) {
            this.targetsByTeam.put(gameTeam, RANDOM);
        }
    }

    private void setFor(SfGameTeam gameTeam, Target target) {
        this.targetsByTeam.put(gameTeam, target);
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
                .build();
    }

    public static class Start extends SfCommand {

        private final CaptainDistributionCommand command;

        public Start(CaptainDistributionCommand command) {
            this.command = command;
        }

        @Override
        public CommandSpec create(SkyFortressPlugin plugin) {

            Map<String, Target> replacedOfflineTargets = new HashMap<>();
            replacedOfflineTargets.put(DISABLED.type.name(), DISABLED);
            replacedOfflineTargets.put(RANDOM.type.name(), RANDOM);

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

                        List<SfGameTeam> offlineTeamCaptains = new ArrayList<>();
                        for (Map.Entry<SfGameTeam, Target> entry : this.command.targetsByTeam.entrySet()) {
                            SfGameTeam team = entry.getKey();
                            Target target = entry.getValue();
                            if (target.getType() == Target.Type.PLAYER) {
                                SfPlayer sfPlayer = ((PlayerTarget) target).get();
                                Optional<Player> optionalPlayer = sfPlayer.getPlayer();
                                if (!optionalPlayer.isPresent()) {
                                    offlineTeamCaptains.add(team);
                                }
                            }
                        }

                        if (offlineTeamCaptains.size() > 0) {
                            Optional<Target> optionalReplaceTarget = args.getOne("offline");
                            if (optionalReplaceTarget.isPresent()) {
                                Target target = optionalReplaceTarget.get();
                                for (SfGameTeam offlineTeamCaptain : offlineTeamCaptains) {
                                    this.command.targetsByTeam.put(offlineTeamCaptain, target);
                                }
                            }
                            else {
                                throw new CommandException(Text.of(
                                        "Captains in teams " + offlineTeamCaptains.toString() + " is offline. Use flag '--offline=[random, disable]' to start distribution"
                                ));
                            }
                        }

                        plugin.getDistributionController().runCaptain(
                                this.command.targetsByTeam,
                                args.hasAny("use_existing_teams")
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
                            this.command.setFor(gameTeam, new PlayerTarget(
                                    SfPlayers.getInstance().getOrCreatePlayer(optionalPlayer.get())
                            ));
                        }
                        else {
                            if (args.hasAny("random")) {
                                this.command.setFor(gameTeam, RANDOM);
                            }
                            else if (args.hasAny("disabled")) {
                                this.command.setFor(gameTeam, DISABLED);
                            }
                            else {
                                throw new CommandException(Text.of(
                                        "Not enough arguments"
                                ));
                            }
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

            Map<String, Target> defaultTargetValues = new HashMap<>();
            defaultTargetValues.put(DISABLED.type.name(), DISABLED);
            defaultTargetValues.put(RANDOM.type.name(), RANDOM);

            System.out.println(DISABLED);
            System.out.println(RANDOM);
            System.out.println(defaultTargetValues);

            return CommandSpec.builder()
                    .arguments(
                            GenericArguments.optional(GenericArguments.choices(Text.of("empty_team"), defaultTargetValues), RANDOM)
                    )
                    .executor((src, args) -> {
                        Optional<Target> defaultTarget = args.getOne("empty_team");
                        if (defaultTarget.isPresent()) {
                            Target target = defaultTarget.get();

                            for (SfGameTeam team : command.targetsByTeam.keySet()) {
                                command.targetsByTeam.put(team, target);
                            }

//                            List<SfGameTeam> notPlayerTeams = new ArrayList<>();
//                            for (Map.Entry<SfGameTeam, Target> entry : this.command.targetsByTeam.entrySet()) {
//                                if (entry.getValue().getType() != Target.Type.PLAYER) {
//                                    notPlayerTeams.add(entry.getKey());
//                                }
//                            }
//                            for (SfGameTeam team : notPlayerTeams) {
//                                this.command.targetsByTeam.put(team, target);
//                            }
                        }

                        Random random = new Random();
                        for (SfLobbyTeam team : plugin.getLobby().getTeams()) {
                            SfGameTeam gameTeam = team.getSettings().team;
                            if (gameTeam.getPlayers().isEmpty()) {
                                continue;
                            }
                            SfPlayer captain = team.getSettings().captain;
                            if (captain == null) {
                                ArrayList<SfPlayer> teamPlayers = new ArrayList<>(gameTeam.getPlayers());
                                int captainIndex = random.nextInt(teamPlayers.size());
                                this.command.targetsByTeam.put(
                                        gameTeam, new PlayerTarget(teamPlayers.get(captainIndex))
                                );
                            }
                            else {
                                this.command.targetsByTeam.put(
                                        gameTeam, new PlayerTarget(captain)
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
                        this.command.reset();
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

                        if (!(src instanceof Player)) {
                            for (Map.Entry<SfGameTeam, Target> entry : this.command.targetsByTeam.entrySet()) {
                                src.sendMessage(Text.of(TextColors.GRAY,
                                        entry.getKey().getUniqueId() + " - " + entry.getValue()
                                ));
                            }
                            return CommandResult.empty();
                        }

                        Player player = (Player) src;
                        SfPlayer sfPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);


                        List<SfTeam> disabledTeams = new ArrayList<>();
                        HashMap<SfGameTeam, Target> enabledTeams = new HashMap<>();

                        for (Map.Entry<SfGameTeam, Target> entry : this.command.targetsByTeam.entrySet()) {
                            SfGameTeam team = entry.getKey();
                            Target target = entry.getValue();

                            if (target.getType() == Target.Type.DISABLED) {
                                disabledTeams.add(team);
                            }
                            else {
                                enabledTeams.put(team, target);
                            }
                        }

                        SfDistributionMessages distribution = plugin.getMessages().getDistribution();

                        List<Text> settings = new ArrayList<>();

                        for (Map.Entry<SfGameTeam, Target> entry : enabledTeams.entrySet()) {
                            SfGameTeam team = entry.getKey();
                            Target target = entry.getValue();

                            if (target.getType() == Target.Type.RANDOM) {
                                Text randomText = distribution.commandInfoRandom(sfPlayer, team);
                                settings.add(randomText);
                            }
                            else {
                                PlayerTarget playerTarget = (PlayerTarget) target;
                                Text playerText = distribution.commandInfoPlayer(sfPlayer, playerTarget.player, team);
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

    public static class Target {

        private final Type type;

        public Target(Type type) {
            this.type = type;
        }

        public Type getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return String.format("Target{type=%s}", type);
        }

        public enum Type {
            PLAYER,
            RANDOM,
            DISABLED
        }

    }

    public static class PlayerTarget extends Target {

        private final SfPlayer player;

        public PlayerTarget(SfPlayer player) {
            super(Type.PLAYER);
            this.player = player;
        }

        public SfPlayer get() {
            return this.player;
        }

        @Override
        public String toString() {
            return String.format("PlayerTarget{type=%s, player=%s}", getType(), player.getName());
        }
    }

}
