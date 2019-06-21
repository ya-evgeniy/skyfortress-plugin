package ru.jekarus.skyfortress.v3.serializer.view;

import com.flowpowered.math.vector.Vector3d;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import jekarus.hocon.config.serializer.converter.SpongeConverters;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.List;

@ConfigPath("lobby")
public class LobbyTeamsView {

    @Generics(TeamView.class)
    public List<TeamView> teams;

    public void setTo(SfLobby lobby) {
        for (TeamView team : teams) {
            lobby.add(team.create());
        }
    }

    public static class TeamView {

        @ConfigPath("team_id")
        public String teamId;

        public SfLocation accepted;

        @ConfigPath("waiting.location")
        public SfLocation waitingLocation;

        @ConfigPath("waiting.leave_button") @MethodConverter(inClass = SpongeConverters.class, method = "vector3d")
        public Vector3d waitingLeaveButton;

        @ConfigPath("join_plate") @MethodConverter(inClass = SpongeConverters.class, method = "vector3d")
        public Vector3d joinPlate;

        @ConfigPath("leave_plate") @MethodConverter(inClass = SpongeConverters.class, method = "vector3d")
        public Vector3d leavePlate;

        @ConfigPath("accept_button") @MethodConverter(inClass = SpongeConverters.class, method = "vector3d")
        public Vector3d acceptButton;

        @ConfigPath("deny_button") @MethodConverter(inClass = SpongeConverters.class, method = "vector3d")
        public Vector3d denyButton;

        @ConfigPath("ready.changed_blocks") @Generics(Vector3d.class) @GenericMethodConverters(@MethodConverter(inClass = SpongeConverters.class, method = "vector3d"))
        public List<Vector3d> readyChangedBlocks;

        @ConfigPath("ready.buttons") @Generics(Vector3d.class) @GenericMethodConverters(@MethodConverter(inClass = SpongeConverters.class, method = "vector3d"))
        public List<Vector3d> readyButtons;

        public SfLobbyTeam create() {
            SfLobbyTeamSettings settings = new SfLobbyTeamSettings();

            settings.teamId = teamId;

            settings.accepted = accepted;
            settings.waitingLocation = waitingLocation;
            settings.waitingLeaveButton = waitingLeaveButton;

            settings.joinPlate = joinPlate;
            settings.leavePlate = leavePlate;

            settings.acceptButton = acceptButton;
            settings.denyButton = denyButton;

            settings.readyChangedBlocks = readyChangedBlocks;
            settings.readyButtons = readyButtons;

            return new SfLobbyTeam(settings);
        }

    }

}
