package ru.jekarus.skyfortress.v3.lobby;

import com.flowpowered.math.vector.Vector3d;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.Collection;
import java.util.Optional;

public class SfLobbyTeamSettings {

    public String teamId;
    public LocationAndRotation accepted;

    public LocationAndRotation waitingLocation;
    public Vector3d waitingLeaveButton;

    public Vector3d joinPlate;
    public Vector3d leavePlate;

    public Vector3d acceptButton;
    public Vector3d denyButton;

    public Collection<Vector3d> readyChangedBlocks;
    public Collection<Vector3d> readyButtons;

    public SfGameTeam team;
    public SfPlayer waitingPlayer;
    public SfPlayer captain;

    public boolean ready = false;

    public void init(SkyFortressPlugin plugin) {
        Optional<SfGameTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(this.teamId);
        optionalTeam.ifPresent(gameTeam -> this.team = gameTeam);
    }

}
