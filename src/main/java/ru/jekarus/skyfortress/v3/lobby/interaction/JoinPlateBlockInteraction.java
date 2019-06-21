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

public class JoinPlateBlockInteraction extends PlateBlockInteraction {

    private final LobbyRoom room;

    public JoinPlateBlockInteraction(LobbyRoom room) {
        super(room.getSettings().getJoinPlate());
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

        if (playerData.getTeam() == team) {
            movement.moveToAccepted(player, playerData);
            return true;
        }

        if (!lobbySettings.isCanJoinTeam()) {
            messages.sendCantJoin(player, playerData);
            return true;
        }

        if (plugin.getDistributionController().isEnabled()) {
            messages.sendCantJoinWhenDistribution(player, playerData);
            return true;
        }

        if (team.getPlayers().isEmpty()) {
            messages.sendJoined(player, playerData);
            movement.moveToAccepted(player, playerData);

            state.setCaptain(playerData);
            return true;
        }

        if (state.getWaitingPlayer() == null) {
            messages.sendWaitAccepted(player, playerData);
            room.setWaitingPlayer(player, playerData);
            return true;
        }

        return true;
    }

}
