package ru.jekarus.skyfortress.v3.lobby;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.Optional;
import java.util.Random;

public class LobbyRoomState {

    @Getter @Setter private SfGameTeam team;

    @Getter @Setter private SfPlayer captain;

    @Getter @Setter private SfPlayer waitingPlayer;

    @Getter @Setter private boolean ready = false;

    @Getter private Server server = Sponge.getServer();

    @Getter private Random random = new Random();

    public void init(SkyFortressPlugin plugin, LobbyRoomSettings settings) {
        final Optional<SfGameTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(settings.getTeamId());
        if (optionalTeam.isPresent()) {
            this.team = optionalTeam.get();
        }
        else {
            throw new UnsupportedOperationException(String.format("Team with id '%s' not found", settings.getTeamId()));
        }
    }

}
