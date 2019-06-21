package ru.jekarus.skyfortress.v3.lobby;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import jekarus.hocon.config.serializer.converter.SpongeConverters;
import lombok.Getter;
import lombok.Setter;
import ru.jekarus.skyfortress.v3.serializer.VectorConverters;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.List;

public class LobbyRoomSettings {

    @ConfigPath("team_id")
    @Getter @Setter private String teamId;

    @ConfigPath("accepted")
    @Getter @Setter private LocationAndRotation accepted;

    @ConfigPath("waiting.location")
    @Getter @Setter private LocationAndRotation waiting;

    @ConfigPath("waiting.leave_button")
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private Vector3i waitingLeaveButton;

    @ConfigPath("join_plate")
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private Vector3i joinPlate;

    @ConfigPath("leave_plate")
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private Vector3i leavePlate;

    @ConfigPath("accept_button")
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private Vector3i acceptButton;

    @ConfigPath("deny_button")
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private Vector3i denyButton;

    @ConfigPath("ready.changed_blocks") @Generics(Vector3d.class)
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private List<Vector3i> readyChangedBlocks;

    @ConfigPath("ready.buttons") @Generics(Vector3d.class)
    @GenericMethodConverters(@MethodConverter(inClass = VectorConverters.class, method = "vector3i"))
    @Getter @Setter private List<Vector3i> readyButtons;

}
