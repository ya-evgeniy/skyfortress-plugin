package ru.jekarus.skyfortress.v3.distribution.captain;

import com.flowpowered.math.vector.Vector3d;
import lombok.Getter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigCaptain;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigPlayer;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CaptainDistribution {

    private final CaptainController controller;
    private final SkyFortressPlugin plugin;
    private final CaptainConfig config;

    @Getter private CaptainsState state;
    @Getter private CaptainSelection selection;
    @Getter private final CaptainRandomizer captainRandomizer;

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

    private void setupCaptains(Map<SfGameTeam, PlayerData> captains) {
        for (CaptainConfigCaptain captainConfig : config.captains) {
            SfGameTeam team = captainConfig.team;
            PlayerData playerData = captains.get(team);

            if (playerData == null) continue;

            Captain captain = new Captain(
                    playerData, team, captainConfig.cell, captainConfig.changedBlocks
            );

            team.addPlayer(plugin, playerData);
            state.captainByTeam.put(team, captain);
        }
    }

    private void setupPlayerCells(List<PlayerData> players, List<PlayerData> playersWithTeam) {
        Iterator<CaptainConfigPlayer> iterator = config.players.iterator();
        setupPlayerCells(iterator, players, true);
        setupPlayerCells(iterator, playersWithTeam, false);
    }

    private void setupPlayerCells(Iterator<CaptainConfigPlayer> iterator, List<PlayerData> players, boolean needDefaultTeam) {
        for (PlayerData playerData : players) {
            if (!iterator.hasNext()) throw new UnsupportedOperationException();
            CaptainConfigPlayer playerConfig = iterator.next();

            CaptainTarget captainTarget = new CaptainTarget(playerData, playerConfig.cell, playerConfig.changedBlocks);
            state.targetByPlayerUniqueId.put(playerData.getUniqueId(), captainTarget);

            if (needDefaultTeam) {
                defaultTeam.addPlayer(plugin, playerData);
            }
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
            if (team != null && team.getType() == SfTeam.Type.GAME) {
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

    private boolean moveToZone(Entity entity, LocationAndRotation cell) {
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

    public void colorizeBlocks(List<LocationAndRotation> blocks, SfGameTeam team) {
        colorizeBlocks(blocks, team.getBlockColor());
    }

    public void colorizeBlocks(List<LocationAndRotation> blocks, DyeColor color) {
        for (LocationAndRotation block : blocks) {
            block.getLocation().offer(Keys.DYE_COLOR, color);
        }
    }

    private void cleanupCells() {
        for (CaptainConfigCaptain captain : config.captains) {
            colorizeBlocks(captain.changedBlocks, DyeColors.WHITE);
        }
        for (CaptainConfigPlayer player : config.players) {
            colorizeBlocks(player.changedBlocks, DyeColors.WHITE);
        }
    }

//    public void startC(Map<SfGameTeam, CaptainDistributionCommand.Target> captains, ArrayList<Player> players, boolean useExistingTeams) {
//        int needCaptainsCount = (int) captains.values().stream().filter(target -> target.getType() != CaptainDistributionCommand.Target.Type.DISABLED).count();
//        if (needCaptainsCount > players.size()) {
//            this.controller.onEnd();
//            throw new UnsupportedOperationException("Not enough players"); // fixme
//        }
//
//        List<SfGameTeam> needRandomCaptain = new ArrayList<>();
//        for (Map.Entry<SfGameTeam, CaptainDistributionCommand.Target> entry : captains.entrySet()) {
//            SfGameTeam team = entry.getKey();
//            CaptainDistributionCommand.Target target = entry.getValue();
//            if (target.getType() == CaptainDistributionCommand.Target.Type.PLAYER) {
//                addCaptain(((CaptainDistributionCommand.PlayerTarget) target).get(), team);
//            }
//            else if (target.getType() == CaptainDistributionCommand.Target.Type.RANDOM) {
//                needRandomCaptain.register(team);
//            }
//        }
//
//        List<PlayerData> sfPlayers = new ArrayList<>();
//        SfPlayers playersData = SfPlayers.getInstance();
//        for (Player player : players) {
//            PlayerData sfPlayer = playersData.getOrCreatePlayer(player);
//            sfPlayers.register(sfPlayer);
//            if (!useExistingTeams) {
//                SfTeam playerTeam = sfPlayer.getTeam();
//                if (playerTeam != null && playerTeam.getType() == SfTeam.Type.GAME) {
//                    Captain teamCaptain = this.state.captainByTeam.get((SfGameTeam) playerTeam);
//                    if (teamCaptain != null && teamCaptain.player == sfPlayer) {
//                        continue;
//                    }
//                }
//                defaultTeam.addPlayer(plugin, sfPlayer);
//            }
//        }
//
//        removeExistingCaptains(sfPlayers);
//
//        Random random = new Random();
//        for (SfGameTeam team : needRandomCaptain) {
//            if (useExistingTeams) {
//                if (!team.getPlayers().isEmpty()) {
//                    ArrayList<PlayerData> teamPlayers = new ArrayList<>(team.getPlayers());
//                    int nextCaptainIndex = random.nextInt(teamPlayers.size());
//                    PlayerData captain = teamPlayers.get(nextCaptainIndex);
//
//                    sfPlayers.unregister(captain);
//                    addCaptain(captain, team);
//
//                    continue;
//                }
//            }
//
//            if (sfPlayers.size() < 1) {
//                throw new UnsupportedOperationException("Developer is so stupid. (Not enough players)");
//            }
//            int nextCaptain = random.nextInt(sfPlayers.size());
//            PlayerData captain = sfPlayers.unregister(nextCaptain);
//            addCaptain(captain, team);
//        }
//
////        for (int i = 0; i < config.players.size() - sfPlayers.size(); i++) {
////            sfPlayers.register(new PlayerData(UUID.randomUUID(), "ENTITY_" + i));
////        }
//
//        start(sfPlayers, useExistingTeams);
//    }
//
//    public void start(List<PlayerData> players, boolean useExistingTeams) {
//        if (state.started) return;
//        state.started = true;
//
//        List<PlayerData> sfPlayers = SfPlayers.getInstance().asList();
//        for (PlayerData sfPlayer : sfPlayers) {
//            if (sfPlayer.getZone() == PlayerZone.CAPTAIN_SYSTEM) {
//                sfPlayer.setZone(PlayerZone.LOBBY);
//                SfTeam team = sfPlayer.getTeam();
//                if (team != null) {
//                    team.removePlayer(plugin, sfPlayer);
//                }
//            }
//        }
//
//        init(players, useExistingTeams);
//        moveToZones();
//        nextCaptain();
//
//        selection.start();
//    }

    public void start(CaptainSettings settings, List<PlayerData> players) {
        if (state.started) return;
        state.started = true;

        CaptainSettings.Setup setup = settings.setup(players);

        cleanupCells();
        setupCaptains(setup.captains);
        setupPlayerCells(setup.players, setup.playersWithTeam);

        moveToZones();
        nextCaptain();

        selection.start();
    }

    public void stop() {
        selection.stop();
        Map<SfGameTeam, LobbyRoom> roomByTeam = new HashMap<>();
        for (LobbyRoom room : plugin.getLobbyRoomsContainer().getRooms()) {
            roomByTeam.put(room.getState().getTeam(), room);
        }
        moveToAccepted(this.state.targetByPlayerUniqueId.values(), roomByTeam, false);
        moveToAccepted(this.state.captainByTeam.values(), roomByTeam, true);

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
        LocationAndRotation center = plugin.getSettings().getLobby().getCenter();

        moveToSpawn(this.state.targetByPlayerUniqueId.values(), noneTeam, center);
        moveToSpawn(this.state.captainByTeam.values(), noneTeam, center);

        this.captainRandomizer.stop();
        this.selection.onEnd();
        this.controller.onEnd();
    }

    private void moveToAccepted(Collection<? extends CaptainTarget> targets, Map<SfGameTeam, LobbyRoom> roomByTeam, boolean isCaptain) {
        for (CaptainTarget target : targets) {
            Optional<? extends Entity> optionalEntity = target.getEntity();
            if (optionalEntity.isPresent()) {
                Entity entity = optionalEntity.get();
                if (entity.getType().equals(EntityTypes.PLAYER)) {
                    target.player.setZone(PlayerZone.TEAM_ROOM);
                    Player player = (Player) entity;

                    LobbyRoom lobbyTeam = roomByTeam.get(target.player.getTeam());
                    if (lobbyTeam == null) {
                        SfTeam noneTeam = plugin.getTeamContainer().getNoneTeam();
                        noneTeam.addPlayer(plugin, target.player);
                        target.player.setZone(PlayerZone.LOBBY);
                        LocationAndRotation center = plugin.getSettings().getLobby().getCenter();
                        player.setLocationAndRotation(
                                center.getLocation(),
                                center.getRotation()
                        );
                        continue;
                    }

                    if (isCaptain) {
                        lobbyTeam.getState().setCaptain(target.player);
                    }

                    player.setLocationAndRotation(
                            lobbyTeam.getSettings().getAccepted().getLocation(),
                            lobbyTeam.getSettings().getAccepted().getRotation()
                    );
                    target.player.setZone(PlayerZone.TEAM_ROOM);
                }
                else {
                    entity.remove();
                }
            }
        }
    }

    private void moveToSpawn(Collection<? extends CaptainTarget> targets, SfTeam spawnTeam, LocationAndRotation center) {
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
                    entity.offer(Keys.GLOWING, false);
                }
                else {
                    entity.remove();
                }
            }
        }
    }

    public void onDisconnect(PlayerData playerData, Player player) {
        CaptainTarget target = this.state.targetByPlayerUniqueId.get(playerData.getUniqueId());
        if (target != null) {
            target.onDisconnect(player);
            SfTeam team = playerData.getTeam();
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
            if (captain.player == playerData) {
                captain.onDisconnect(player);
                return;
            }
        }
    }

    public void onConnect(PlayerData playerData, Player player) {
        CaptainTarget target = this.state.targetByPlayerUniqueId.get(playerData.getUniqueId());
        if (target != null) {
            target.onConnect(player);
            this.captainRandomizer.showBarTo(player, playerData);
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
            if (captain.player == playerData) {
                captain.onConnect(player);
                this.captainRandomizer.showBarTo(player, playerData);
                return;
            }
        }
    }
}