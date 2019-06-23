package ru.jekarus.skyfortress.v3.lobby;

import com.flowpowered.math.vector.Vector3i;
import lombok.Getter;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LobbyRoom {

    @Getter private SkyFortressPlugin plugin;

    @Getter private LobbyRoomSettings settings;

    @Getter private LobbyRoomInteractions interactions;

    @Getter private LobbyRoomState state;

    @Getter private LobbyRoomMovement movement;

    @Getter private LobbyRoomMessages messages;

    public LobbyRoom(SkyFortressPlugin plugin, LobbyRoomSettings settings) {
        this.plugin = plugin;
        this.settings = settings;

        this.interactions = new LobbyRoomInteractions(this);
        this.state = new LobbyRoomState();
        this.movement = new LobbyRoomMovement(this);
        this.messages = new LobbyRoomMessages(this);
    }

    public void init() {
        this.state.init(this.plugin, this.settings);
        this.interactions.init();
    }

    public void setWaitingPlayer(Player player, PlayerData playerData) {
        if (playerData == null) return;

        final PlayerData waitingPlayer = state.getWaitingPlayer();
        if (waitingPlayer != null) {

            if (playerData == waitingPlayer) {
                return;
            }

            movement.moveToLobby(waitingPlayer.getPlayer().orElse(null), waitingPlayer);
        }

        state.setWaitingPlayer(playerData);
        movement.moveToWaiting(player, playerData);
    }

    public Optional<PlayerData> getRandomCaptain() {
        final SfGameTeam team = state.getTeam();
        final Random random = state.getRandom();

        final List<PlayerData> players = new ArrayList<>(team.getPlayers());
        if (players.isEmpty()) return Optional.empty();

        final int randomCaptainIndex = random.nextInt(players.size());
        final PlayerData randomCaptain = players.get(randomCaptainIndex);
        return Optional.of(randomCaptain);
    }

    public Optional<PlayerData> getJoinPlatePlayer() {
        final Vector3i joinPlate = settings.getJoinPlate();
        for (Player player : state.getServer().getOnlinePlayers()) {
            final Vector3i playerPosition = player.getLocation().getBlockPosition();
            if (joinPlate.getX() != playerPosition.getX()) continue;
            if (joinPlate.getY() != playerPosition.getY()) continue;
            if (joinPlate.getZ() != playerPosition.getZ()) continue;

            return Optional.of(PlayersDataContainer.getInstance().getOrCreateData(player));
        }
        return Optional.empty();
    }

    public void setReady(boolean value) {
        if (state.isReady() == value) {
            return;
        }
        state.setReady(value);

        DyeColor color;
        if (state.isReady()) {
            System.out.println("setting ready");
            plugin.getScoreboards().setReady(state.getTeam());
            plugin.getGame().checkStart();
            color = state.getTeam().getBlockColor();
        }
        else {
            System.out.println("setting unready");
            plugin.getScoreboards().setUnready(state.getTeam());
            color = DyeColors.WHITE;
        }
        World world = plugin.getWorld();
        for (Vector3i blockPosition : settings.getReadyChangedBlocks()) {
            Location<World> block = world.getLocation(blockPosition);
            block.setBlockType(BlockTypes.WOOL);
            block.offer(Keys.DYE_COLOR, color);
        }
    }

}
