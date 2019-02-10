package ru.jekarus.skyfortress.v3.lobby;

import com.flowpowered.math.vector.Vector3d;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.Collection;
import java.util.Optional;

public class SfLobbyTeamSettings {

    public String teamId;
    public SfLocation accepted;

    public SfLocation waitingLocation;
    public Vector3d waitingLeaveButton;

    public Vector3d joinPlate;
    public Vector3d leavePlate;

    public Vector3d acceptButton;
    public Vector3d denyButton;

    public Collection<Vector3d> readyChangedBlocks;
    public Collection<Vector3d> readyButtons;

    public SfGameTeam team;
    public SfPlayer waitingPlayer;

    public boolean ready = false;

    public void init(SkyFortressPlugin plugin)
    {
        Optional<SfTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(this.teamId);
        if (optionalTeam.isPresent())
        {
            SfTeam team = optionalTeam.get();
            if (team.getType() == SfTeam.Type.GAME)
            {
                this.team = (SfGameTeam) team;
            }
        }
    }

}
