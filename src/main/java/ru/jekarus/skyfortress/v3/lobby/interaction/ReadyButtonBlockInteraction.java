package ru.jekarus.skyfortress.v3.lobby.interaction;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMessages;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomMovement;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomSettings;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomState;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.settings.GlobalLobbySettings;

public class ReadyButtonBlockInteraction extends ButtonBlockInteraction {

    private final LobbyRoom room;

    public ReadyButtonBlockInteraction(LobbyRoom room) {
        super(room.getSettings().getReadyButtons());
        this.room = room;
    }

    @Override
    protected boolean onInteract(Player player, SfPlayer playerData, BlockSnapshot block) {
        final SkyFortressPlugin plugin = room.getPlugin();
        final GlobalLobbySettings lobbySettings = plugin.getSettings().getGlobalLobby();

        final LobbyRoomState state = room.getState();
        final LobbyRoomMovement movement = room.getMovement();
        final LobbyRoomMessages messages = room.getMessages();

        if (state.isReady() && !lobbySettings.isCanSetUnready()) {
            messages.sendCantUnready(
                    player, playerData
            );
            return true;
        }

        if (!state.isReady() && !lobbySettings.isCanSetReady()) {
            messages.sendCantReady(
                    player, playerData
            );
            return true;
        }

        if (!lobbySettings.isUseLobbyCaptainSystem()) {
            room.setReady(!state.isReady());
            return true;
        }

        if (state.isReady()) {
            if (state.getCaptain() == playerData) {
                room.setReady(!state.isReady());
                return true;
            }
            messages.sendCantUnreadyCaptain(
                    player, playerData
            );
            return true;
        }

        if (state.getCaptain() == playerData) {
            room.setReady(!state.isReady());
            return true;
        }
        messages.sendCantReadyCaptain(
                player, playerData
        );

        return true;
    }

}
