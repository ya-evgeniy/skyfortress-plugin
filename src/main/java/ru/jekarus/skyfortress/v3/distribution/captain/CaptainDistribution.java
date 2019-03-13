package ru.jekarus.skyfortress.v3.distribution.captain;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.distribution.captain.CaptainDistributionCommand;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigCaptain;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigPlayer;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.*;

public class CaptainDistribution {

    private final CaptainController controller;
    private final SkyFortressPlugin plugin;
    private final CaptainConfig config;

    private CaptainsState state;
    private CaptainSelection selection;
    private final CaptainRandomizer captainRandomizer;

    public SfTeam defaultTeam;

    public CaptainDistribution(CaptainController controller, SkyFortressPlugin plugin, CaptainConfig config) {
        this.controller = controller;
        this.plugin = plugin;
        this.config = config;

        this.state = new CaptainsState();
        this.selection = new CaptainSelection(plugin, this);
        captainRandomizer = new CaptainRandomizer(plugin, this, config);

        defaultTeam = plugin.getTeamContainer().getNoneTeam();

        state.maxCaptains = 2;//config.captains.size();
    }

    public CaptainsState getState() {
        return this.state;
    }

    public CaptainSelection getSelection() {
        return this.selection;
    }

    public void addCaptain(SfPlayer player) {
        if (state.started) return;

        Optional<SfGameTeam> optionalTeam = getFreeTeam();
        optionalTeam.ifPresent(sfGameTeam -> addCaptain(player, sfGameTeam));
        System.out.println(optionalTeam);
    }

    public void addCaptain(SfPlayer player, SfGameTeam team) {
        System.out.println(state.captainByTeam.size());
        System.out.println(state.maxCaptains);
        if (state.captainByTeam.size() < state.maxCaptains || state.captainByTeam.containsKey(team)) {
            CaptainConfigCaptain config = getCaptainConfig(team);
            Captain captain = new Captain(
                    player, team, config.cell, config.changedBlocks
            );
            team.addPlayer(plugin, player);
            state.captainByTeam.put(team, captain);
            System.out.println("ADDED");
        }
        System.out.println("SKIPPED");
    }

    private CaptainConfigCaptain getCaptainConfig(SfGameTeam team) {
        for (CaptainConfigCaptain captain : config.captains) {
            if (captain.team == team) {
                return captain;
            }
        }
        throw new UnsupportedOperationException();
    }

    private Optional<SfGameTeam> getFreeTeam() {
        for (CaptainConfigCaptain captain : config.captains) {
            if (!state.captainByTeam.containsKey(captain.team)) {
                System.out.println(captain);
                return Optional.of(captain.team);
            }
        }
        return Optional.empty();
    }

    private void init(List<SfPlayer> players, boolean useExistingTeams) {
//        removeExistingCaptains(players);
//        removeMissingCaptains(players);

        Iterator<SfPlayer> playerIterator = players.iterator();

//        List<>
        for (CaptainConfigPlayer playerConfigState : config.players) {
            if (!playerIterator.hasNext()) break;
            SfPlayer player = playerIterator.next();

            CaptainTarget captainTarget = new CaptainTarget(player, playerConfigState.cell, playerConfigState.changedBlocks);
//            playerState.changedBlocks = playerConfigState.changedBlocks;
            state.targetByPlayerUniqueId.put(player.getUniqueId(), captainTarget);

            if (player.getTeam() == null || !useExistingTeams || player.getTeam().getType() != SfTeam.Type.GAME) {
                defaultTeam.addPlayer(plugin, player);
            }
        }

        if (playerIterator.hasNext()) {
//            new UnsupportedOperationException().printStackTrace();
        }
    }

    private void removeExistingCaptains(List<SfPlayer> players) {
        Iterator<SfPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            SfPlayer nextPlayer = iterator.next();
            for (Captain captain : state.captainByTeam.values()) {
                if (captain.player == nextPlayer) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void removeMissingCaptains(List<SfPlayer> players) {
        int currentCaptains = state.captainByTeam.size();
        int needCaptains = state.maxCaptains - currentCaptains;

        if (needCaptains < 1) {
            System.out.println("skipped ... ");
            return;
        }

        Random random = new Random();
        while (needCaptains-- > 0) {
            int nextCaptainIndex = random.nextInt(players.size());
            addCaptain(players.get(nextCaptainIndex));
            players.remove(nextCaptainIndex);
        }
    }

    public void nextCaptain() {
        selection.updateUnselected();
        if (state.unselectedTargets.size() < 1) {
            this.stop();
            return;
        }

        if (selection.choosingCaptain != null) {
            Captain captain = selection.choosingCaptain.captain;
            captain.getEntity().ifPresent(entity -> {
                entity.setLocationAndRotation(
                        captain.cell.getLocation(),
                        captain.cell.getRotation()
                );
            });
        }

        Set<SfGameTeam> teams = state.captainByTeam.keySet();
        Optional<SfGameTeam> optionalTeam = teams.stream().min(Comparator.comparingInt(team -> team.getPlayers().size()));
        if (!optionalTeam.isPresent()) {
            throw new UnsupportedOperationException();
        }

        SfGameTeam team = optionalTeam.get();
        Captain captain = state.captainByTeam.get(team);

        selection.choosingCaptain = new ChoosingCaptain(captain, (SfGameTeam) captain.player.getTeam());

        captain.getEntity().ifPresent(entity -> {
            entity.setLocationAndRotation(
                    config.mainCaptainCell.getLocation(),
                    config.mainCaptainCell.getRotation()
            );
        });

        this.captainRandomizer.reset();
        colorizeBlocks(config.mainCaptainCellChangedBlocks, captain.team);
    }

    private void moveToZones() {
        for (Captain captain : state.captainByTeam.values()) {
            captain.player.setZone(PlayerZone.CAPTAIN_SYSTEM);
            colorizeBlocks(captain.changedBlocks, captain.team);
            if (moveToZone(captain.getEntity().orElse(null), captain.cell)) {
                captain.setOffline();
            }
        }
        for (CaptainTarget target : state.targetByPlayerUniqueId.values()) {
            target.player.setZone(PlayerZone.CAPTAIN_SYSTEM);
            SfTeam team = target.player.getTeam();
            if (team == null || team.getType() != SfTeam.Type.GAME) {
                colorizeBlocks(target.changedBlocks, DyeColors.WHITE);
            }
            else {
                colorizeBlocks(target.changedBlocks, (SfGameTeam) team);
            }
            if (moveToZone(target.getEntity().orElse(null), target.cell)) {
                target.setOffline();
                target.getEntity().ifPresent(entity -> {
                    if (entity instanceof Living) {
                        ((Living) entity).lookAt(config.mainCaptainCell.getLocation().getPosition());
                    }
                });
            }
        }
    }

    private boolean moveToZone(Entity entity, SfLocation cell) {
        if (entity == null) {
            return true;
        }

        if (cell.getRotation() == Vector3d.ZERO) {
            entity.setLocation(cell.getLocation());
            if (entity instanceof Living) {
                ((Living) entity).lookAt(config.mainCaptainCell.getLocation().getPosition());
            }
        }
        else {
            entity.setLocationAndRotation(
                    cell.getLocation(),
                    cell.getRotation()
            );
        }

        return false;
    }

    public void colorizeBlocks(List<SfLocation> blocks, SfGameTeam team) {
        System.out.println("\n\n" + blocks.size() + "\n" + team.getUniqueId() + "\n\n");
        colorizeBlocks(blocks, team.getBlockColor());
    }

    public void colorizeBlocks(List<SfLocation> blocks, DyeColor color) {
        for (SfLocation block : blocks) {
            block.getLocation().offer(Keys.DYE_COLOR, color);
        }
    }

    public void startC(Map<SfGameTeam, CaptainDistributionCommand.Target> captains, ArrayList<Player> players, boolean useExistingTeams) {
        int needCaptainsCount = (int) captains.values().stream().filter(target -> target.getType() != CaptainDistributionCommand.Target.Type.DISABLED).count();
        if (needCaptainsCount > players.size()) {
            this.controller.onEnd();
            throw new UnsupportedOperationException("Not enough players"); // fixme
        }

        List<SfGameTeam> needRandomCaptain = new ArrayList<>();
        for (Map.Entry<SfGameTeam, CaptainDistributionCommand.Target> entry : captains.entrySet()) {
            SfGameTeam team = entry.getKey();
            CaptainDistributionCommand.Target target = entry.getValue();
            if (target.getType() == CaptainDistributionCommand.Target.Type.PLAYER) {
                addCaptain(((CaptainDistributionCommand.PlayerTarget) target).get(), team);
            }
            else if (target.getType() == CaptainDistributionCommand.Target.Type.RANDOM) {
                needRandomCaptain.add(team);
            }
        }

        List<SfPlayer> sfPlayers = new ArrayList<>();
        SfPlayers playersData = SfPlayers.getInstance();
        for (Player player : players) {
            SfPlayer sfPlayer = playersData.getOrCreatePlayer(player);
            sfPlayers.add(sfPlayer);
            if (!useExistingTeams) {
                SfTeam playerTeam = sfPlayer.getTeam();
                if (playerTeam != null && playerTeam.getType() == SfTeam.Type.GAME) {
                    Captain teamCaptain = this.state.captainByTeam.get((SfGameTeam) playerTeam);
                    if (teamCaptain != null && teamCaptain.player == sfPlayer) {
                        continue;
                    }
                }
                defaultTeam.addPlayer(plugin, sfPlayer);
            }
        }

        removeExistingCaptains(sfPlayers);

        Random random = new Random();
        for (SfGameTeam team : needRandomCaptain) {
            if (useExistingTeams) {
                if (!team.getPlayers().isEmpty()) {
                    ArrayList<SfPlayer> teamPlayers = new ArrayList<>(team.getPlayers());
                    int nextCaptainIndex = random.nextInt(teamPlayers.size());
                    SfPlayer captain = teamPlayers.get(nextCaptainIndex);

                    sfPlayers.remove(captain);
                    addCaptain(captain, team);

                    continue;
                }
            }

            if (sfPlayers.size() < 1) {
                throw new UnsupportedOperationException("Developer is so stupid. (Not enough players)");
            }
            int nextCaptain = random.nextInt(sfPlayers.size());
            SfPlayer captain = sfPlayers.remove(nextCaptain);
            addCaptain(captain, team);
        }

//        for (int i = 0; i < config.players.size() - sfPlayers.size(); i++) {
//            sfPlayers.add(new SfPlayer(UUID.randomUUID(), "ENTITY_" + i));
//        }

        start(sfPlayers, useExistingTeams);
    }

    public void start(List<SfPlayer> players, boolean useExistingTeams) {
        if (state.started) return;
        state.started = true;

        List<SfPlayer> sfPlayers = SfPlayers.getInstance().asList();
        for (SfPlayer sfPlayer : sfPlayers) {
            if (sfPlayer.getZone() == PlayerZone.CAPTAIN_SYSTEM) {
                sfPlayer.setZone(PlayerZone.LOBBY);
                SfTeam team = sfPlayer.getTeam();
                if (team != null) {
                    team.removePlayer(plugin, sfPlayer);
                }
            }
        }

        init(players, useExistingTeams);
        moveToZones();
        nextCaptain();

        selection.start();
    }

    public void stop() {
        selection.stop();
        Map<SfGameTeam, SfLobbyTeam> lobbyTeamByPlayerTeam = new HashMap<>();
        for (SfLobbyTeam team : plugin.getLobby().getTeams()) {
            lobbyTeamByPlayerTeam.put(team.getSettings().team, team);
        }
        moveToAccepted(this.state.targetByPlayerUniqueId.values(), lobbyTeamByPlayerTeam, false);
        moveToAccepted(this.state.captainByTeam.values(), lobbyTeamByPlayerTeam, true);

        this.captainRandomizer.stop();
        this.selection.onEnd();
        this.controller.onEnd();
    }

    public void cancel(boolean saveTeams) {
        if (saveTeams) {
            stop();
            return;
        }
        selection.stop();

        SfTeam noneTeam = plugin.getTeamContainer().getNoneTeam();
        SfLocation center = plugin.getLobby().getSettings().center;

        moveToSpawn(this.state.targetByPlayerUniqueId.values(), noneTeam, center);
        moveToSpawn(this.state.captainByTeam.values(), noneTeam, center);

        this.captainRandomizer.stop();
        this.selection.onEnd();
        this.controller.onEnd();
    }

    private void moveToAccepted(Collection<? extends CaptainTarget> targets, Map<SfGameTeam, SfLobbyTeam> lobbyTeamByPlayerTeam, boolean isCaptain) {
        for (CaptainTarget target : targets) {
            Optional<? extends Entity> optionalEntity = target.getEntity();
            if (optionalEntity.isPresent()) {
                Entity entity = optionalEntity.get();
                if (entity.getType().equals(EntityTypes.PLAYER)) {
                    target.player.setZone(PlayerZone.TEAM_ROOM);
                    Player player = (Player) entity;

                    SfLobbyTeam lobbyTeam = lobbyTeamByPlayerTeam.get(target.player.getTeam());
                    if (lobbyTeam == null) {
                        SfTeam noneTeam = plugin.getTeamContainer().getNoneTeam();
                        noneTeam.addPlayer(plugin, target.player);
                        target.player.setZone(PlayerZone.LOBBY);
                        SfLocation center = plugin.getLobby().getSettings().center;
                        player.setLocationAndRotation(
                                center.getLocation(),
                                center.getRotation()
                        );
                        continue;
                    }

                    if (isCaptain) {
                        lobbyTeam.getSettings().captain = target.player;
                    }

                    player.setLocationAndRotation(
                            lobbyTeam.getSettings().accepted.getLocation(),
                            lobbyTeam.getSettings().accepted.getRotation()
                    );
                    target.player.setZone(PlayerZone.TEAM_ROOM);
                }
                else {
                    entity.remove();
                }
            }
        }
    }

    private void moveToSpawn(Collection<? extends CaptainTarget> targets, SfTeam spawnTeam, SfLocation center) {
        for (CaptainTarget target : targets) {
            spawnTeam.addPlayer(plugin, target.player);
            target.player.setZone(PlayerZone.LOBBY);
            Optional<? extends Entity> optionalEntity = target.getEntity();
            if (optionalEntity.isPresent()) {
                Entity entity = optionalEntity.get();
                if (entity.getType().equals(EntityTypes.PLAYER)) {
                    entity.setLocationAndRotation(
                            center.getLocation(),
                            center.getRotation()
                    );
                }
                else {
                    entity.remove();
                }
            }
        }
    }

    public void onDisconnect(SfPlayer sfPlayer, Player player) {
        CaptainTarget target = this.state.targetByPlayerUniqueId.get(sfPlayer.getUniqueId());
        if (target != null) {
            target.onDisconnect(player);
            SfTeam team = sfPlayer.getTeam();
            if (team == null || team.getType() != SfTeam.Type.GAME) {
                player.getLocation().getExtent().getEntity(target.entityUniqueId).ifPresent(entity -> {
                    entity.offer(Keys.GLOWING, true);
                });
            }
            player.offer(Keys.GLOWING, false);
            ChoosingCaptain choosingCaptain = this.selection.choosingCaptain;
            if (choosingCaptain != null && choosingCaptain.target == target) {
                this.selection.unmarkMember(choosingCaptain.team, Text.of(player.getName()));
                this.selection.markMember(choosingCaptain.team, Text.of(target.entityUniqueId));
            }
            return;
        }
        for (Captain captain : this.state.captainByTeam.values()) {
            if (captain.player == sfPlayer) {
                captain.onDisconnect(player);
                return;
            }
        }
    }

    public void onConnect(SfPlayer sfPlayer, Player player) {
        CaptainTarget target = this.state.targetByPlayerUniqueId.get(sfPlayer.getUniqueId());
        if (target != null) {
            target.onConnect(player);
            this.captainRandomizer.showBarTo(player, sfPlayer);
            SfTeam team = target.player.getTeam();
            if (team == null || team.getType() != SfTeam.Type.GAME) {
                player.offer(Keys.GLOWING, true);
            }
            if (this.selection.choosingCaptain != null && this.selection.choosingCaptain.target == target) {
                this.selection.markTarget(this.selection.choosingCaptain, target);
            }
            return;
        }
        for (Captain captain : this.state.captainByTeam.values()) {
            if (captain.player == sfPlayer) {
                captain.onConnect(player);
                this.captainRandomizer.showBarTo(player, sfPlayer);
                return;
            }
        }
    }
}