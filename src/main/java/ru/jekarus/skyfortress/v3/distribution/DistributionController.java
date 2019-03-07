package ru.jekarus.skyfortress.v3.distribution;

import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.command.distribution.captain.CaptainDistributionCommand;
import ru.jekarus.skyfortress.v3.distribution.captain.CaptainController;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.List;
import java.util.Map;

public class DistributionController {

    private final SkyFortressPlugin plugin;
    private Distribution current = null;

    public DistributionController(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void disposeDistribution() {
        this.current = null;
    }

    public boolean isEnabled() {
        return this.current != null;
    }

    public void onDisconnect(SfPlayer sfPlayer, Player player) {
        if (current == null) return;

        if (current.getType() == Distribution.Type.CAPTAIN) {
            CaptainController captainDistribution = (CaptainController) this.current;
            captainDistribution.onDisconnect(sfPlayer, player);
        }
    }

    public void onConnect(SfPlayer sfPlayer, Player player) {
        if (current == null) return;

        if (current.getType() == Distribution.Type.CAPTAIN) {
            CaptainController captainDistribution = (CaptainController) this.current;
            captainDistribution.onConnect(sfPlayer, player);
        }
    }

    public void serverStopping() {
        if (this.current != null) {
            this.current.serverStopping();
        }
    }

    public void runCaptain(Map<SfGameTeam, CaptainDistributionCommand.Target> captains, boolean useExistingTeams) {
        if (current != null) {
            System.out.println("ALREADY RUNNED");
        }
        this.plugin.getLobby().clearWaitingPlayers();

        current = new CaptainController(this.plugin, this);
        ((CaptainController) current).start(captains, useExistingTeams);
    }

    public void cancelCaptain(boolean saveTeams) {
        if (current == null) return;
        if (current.getType() != Distribution.Type.CAPTAIN) return;

        ((CaptainController) current).cancel(saveTeams);
    }

}
