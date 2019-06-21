package ru.jekarus.skyfortress.v3.team;

import lombok.Getter;
import lombok.NonNull;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SfTeamContainer {

    private Map<String, SfGameTeam> teamsById = new HashMap<>();

    @Getter private SfTeam noneTeam;
    @Getter private SfSpectatorTeam spectatorTeam;

    public void init(SkyFortressPlugin plugin) {
        for (SfGameTeam team : this.teamsById.values()) {
            if (team.getCastle() == null) {
                throw new UnsupportedOperationException(String.format("Team with id '%s' no have castle", team.getUniqueId()));
            }
        }
    }

    public boolean contains(@NonNull SfTeam team) {
        return this.noneTeam == team || this.spectatorTeam == team || teamsById.containsKey(team.getUniqueId());
    }

    public void register(@NonNull SfTeam team) {
        if (contains(team)) {
            throw new UnsupportedOperationException();
        }
        final SfTeam.@NonNull Type type = team.getType();
        if (type == SfTeam.Type.GAME) {
            this.teamsById.put(team.getUniqueId(), (SfGameTeam) team);
        }
        else if (type == SfTeam.Type.NONE) {
            this.noneTeam = team;
        }
        else if (type == SfTeam.Type.SPECTATOR) {
            this.spectatorTeam = (SfSpectatorTeam) team;
        }
    }

    public void unregister(@NonNull SfTeam team) {
        final SfTeam.@NonNull Type type = team.getType();
        if (type == SfTeam.Type.GAME) {
            this.teamsById.remove(team.getUniqueId());
        }
        else if (type == SfTeam.Type.NONE) {
            this.noneTeam = null;
        }
        else if (type == SfTeam.Type.SPECTATOR) {
            this.spectatorTeam = null;
        }
    }

    public Optional<SfGameTeam> fromUniqueId(@NonNull String uniqueId) {
        return Optional.ofNullable(this.teamsById.get(uniqueId));
    }

    public List<SfTeam> getCollection() {
        final ArrayList<SfTeam> result = new ArrayList<>(this.teamsById.values());
        if (this.noneTeam != null) {
            result.add(this.noneTeam);
        }
        if (this.spectatorTeam != null) {
            result.add(this.spectatorTeam);
        }
        return Collections.unmodifiableList(result);
    }

    public List<SfGameTeam> getGameCollection() {
        return Collections.unmodifiableList(new ArrayList<>(this.teamsById.values()));
    }

}
