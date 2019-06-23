package ru.jekarus.skyfortress.v3.lobby.interaction;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMessages;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMovement;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomState;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.settings.GlobalLobbySettings;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.Optional;

public class AcceptButtonBlockInteraction extends ButtonBlockInteraction {

    private final LobbyRoom room;

    public AcceptButtonBlockInteraction(LobbyRoom room) {
        super(room.getSettings().getAcceptButton());
        this.room = room;
    }

    @Override
    protected boolean onInteract(Player player, PlayerData playerData, BlockSnapshot block) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final GlobalLobbySettings lobbySettings = plugin.getSettings().getGlobalLobby();

        final LobbyRoomState state = room.getState();
        final LobbyRoomMovement movement = room.getMovement();
        final LobbyRoomMessages messages = room.getMessages();

        final SfGameTeam team = state.getTeam();

        final PlayerData waitingPlayer = state.getWaitingPlayer();
        if (waitingPlayer == null) {
            return true;
        }

        if (!lobbySettings.isCanUseAcceptButton()) {
            messages.sendCantAccept(player, playerData);
            return true;
        }

        if (lobbySettings.isUseLobbyCaptainSystem() && state.getCaptain() != playerData) {
            messages.sendCantAcceptCaptain(player, playerData);
            return true;
        }

        messages.sendAccepted(player, playerData, null, waitingPlayer);
        movement.moveToAccepted(null, waitingPlayer);

        state.setWaitingPlayer(null);

        final Optional<PlayerData> optionalJoinPlatePlayer = room.getJoinPlatePlayer();
        if (optionalJoinPlatePlayer.isPresent()) {
            final PlayerData joinPlatePlayer = optionalJoinPlatePlayer.get();
            room.setWaitingPlayer(null, joinPlatePlayer);
            messages.sendWaitAccepted(null, joinPlatePlayer);
        }

        return true;
    }

}
