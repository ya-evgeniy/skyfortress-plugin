package ru.jekarus.skyfortress.v3.lobby.interaction;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMessages;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMovement;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomState;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.settings.GlobalLobbySettings;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeavePlateBlockInteraction extends PlateBlockInteraction {

    private final LobbyRoom room;

    public LeavePlateBlockInteraction(LobbyRoom room) {
        super(room.getSettings().getLeavePlate());
        this.room = room;
    }

    @Override
    protected boolean onInteract(Player player, SfPlayer playerData, BlockSnapshot block) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final GlobalLobbySettings lobbySettings = plugin.getSettings().getGlobalLobby();

        final LobbyRoomState state = room.getState();
        final LobbyRoomMovement movement = room.getMovement();
        final LobbyRoomMessages messages = room.getMessages();

        final SfGameTeam team = state.getTeam();

        if (!lobbySettings.isCanLeaveTeam()) {
            messages.sendCantLeave(player, playerData);
            return true;
        }

        movement.moveToLobby(player, playerData);

        if (state.getCaptain() == playerData) {
            state.setCaptain(null);

            Optional<SfPlayer> optionalRandomCaptain = room.getRandomCaptain();
            if (optionalRandomCaptain.isPresent()) {
                final SfPlayer randomCaptain = optionalRandomCaptain.get();
                state.setCaptain(randomCaptain);
            }

            messages.sendTeammateLeave(player, playerData, true);

        }
        else {
            messages.sendTeammateLeave(player, playerData, false);
        }

        if (team.getPlayers().isEmpty()) {

            room.setReady(false);

            final SfPlayer waitingPlayer = state.getWaitingPlayer();
            if (waitingPlayer != null) {
                state.setWaitingPlayer(null);
                state.setCaptain(waitingPlayer);
                messages.sendJoined(null, waitingPlayer);
                movement.moveToAccepted(null, waitingPlayer);
            }

            final Optional<SfPlayer> optionalJoinPlatePlayer = room.getJoinPlatePlayer();
            if (optionalJoinPlatePlayer.isPresent()) {
                final SfPlayer joinPlatePlayer = optionalJoinPlatePlayer.get();
                room.setWaitingPlayer(null, joinPlatePlayer);
                messages.sendWaitAccepted(null, joinPlatePlayer);
            }

        }

        return true;
    }

}
