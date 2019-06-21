package ru.jekarus.skyfortress.v3.castle;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SfCastleContainer {

    private Map<String, SfCastle> castlesById = new HashMap<>();

    public void init(SkyFortressPlugin plugin) {
        for (SfCastle castle : this.castlesById.values()) {
            final String teamId = castle.getTeamId();
            Optional<SfGameTeam> optionalTeam = plugin.getTeamContainer().fromUniqueId(teamId);
            castle.setTeam(
                    optionalTeam.orElseThrow(() -> new UnsupportedOperationException(String.format("Team with id '%s' is not registered", teamId)))
            );
            castle.getTeam().setCastle(castle);
        }
    }

    public void register(SfCastle castle) {
        this.castlesById.put(castle.getUniqueId(), castle);
    }

    public void unregister(SfCastle castle) {
        this.castlesById.remove(castle.getUniqueId());
    }

    public Optional<SfCastle> fromUniqueId(String uniqueId) {
        return Optional.ofNullable(this.castlesById.get(uniqueId));
    }

    public List<SfCastle> getCollection() {
        return Collections.unmodifiableList(new ArrayList<>(this.castlesById.values()));
    }

}
