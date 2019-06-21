package ru.jekarus.skyfortress.v3.serializer.view;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import jekarus.hocon.config.serializer.converter.SpongeConverters;
import lombok.Getter;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoomSettings;
import ru.jekarus.skyfortress.v3.serializer.VectorConverters;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.ArrayList;
import java.util.List;

@ConfigPath("lobby")
public class LobbyRoomsContainerView {

    @ConfigPath("teams") @Generics(RoomSettingsView.class)
    @Getter private List<RoomSettingsView> rooms = new ArrayList<>();

    public static class RoomSettingsView {

        @ConfigPath("team_id")
        public String teamId;

        public LocationAndRotation accepted;

        @ConfigPath("waiting.location")
        public LocationAndRotation waitingLocation;

        @ConfigPath("waiting.leave_button") @MethodConverter(inClass = VectorConverters.class, method = "vector3i")
        public Vector3i waitingLeaveButton;

        @ConfigPath("join_plate") @MethodConverter(inClass = VectorConverters.class, method = "vector3i")
        public Vector3i joinPlate;

        @ConfigPath("leave_plate") @MethodConverter(inClass = VectorConverters.class, method = "vector3i")
        public Vector3i leavePlate;

        @ConfigPath("accept_button") @MethodConverter(inClass = VectorConverters.class, method = "vector3i")
        public Vector3i acceptButton;

        @ConfigPath("deny_button") @MethodConverter(inClass = VectorConverters.class, method = "vector3i")
        public Vector3i denyButton;

        @ConfigPath("ready.changed_blocks") @Generics(Vector3d.class) @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
        public List<Vector3i> readyChangedBlocks;

        @ConfigPath("ready.buttons") @Generics(Vector3d.class) @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
        public List<Vector3i> readyButtons;

        public LobbyRoom create(SkyFortressPlugin plugin) {
            LobbyRoomSettings settings = new LobbyRoomSettings();

            settings.setTeamId(teamId);

            settings.setAccepted(accepted);

            settings.setWaiting(waitingLocation);
            settings.setWaitingLeaveButton(waitingLeaveButton);

            settings.setJoinPlate(joinPlate);
            settings.setLeavePlate(leavePlate);

            settings.setAcceptButton(acceptButton);
            settings.setDenyButton(denyButton);

            settings.setReadyChangedBlocks(readyChangedBlocks);
            settings.setReadyButtons(readyButtons);

            return new LobbyRoom(plugin, settings);
        }

    }
}
