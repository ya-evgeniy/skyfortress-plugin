package ru.jekarus.skyfortress.v3.distribution;

import lombok.Getter;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.captain.CaptainController;
import ru.jekarus.skyfortress.v3.distribution.captain.CaptainSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

import java.util.function.BiConsumer;

public class DistributionController {

    private final SkyFortressPlugin plugin;
    @Getter private Distribution current = null;

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

    public void runCaptain(CaptainSettings settings, BiConsumer<CaptainController, CaptainController.ResultMessage> consumer) {
        if (current != null) {
            return;
        }
        this.plugin.getLobbyRoomsContainer().clearWaitingPlayers();

        current = new CaptainController(this.plugin, this, consumer);
        ((CaptainController) current).start(settings);
    }

    public void cancelCaptain(boolean saveTeams) {
        if (current == null) return;
        if (current.getType() != Distribution.Type.CAPTAIN) return;

        ((CaptainController) current).cancel(saveTeams);
    }

}
