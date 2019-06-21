package ru.jekarus.skyfortress.v3.distribution.captain;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class CaptainSettings {

    public static final Selector RANDOM_SELECTOR = new Selector(SelectorType.RANDOM);
    public static final Selector DISABLED_SELECTOR = new Selector(SelectorType.DISABLED);

    private final SkyFortressPlugin plugin;

    @Getter private Map<SfGameTeam, Selector> selectorByTeam = new HashMap<>();
    @Getter private Selector defaultSelector = RANDOM_SELECTOR;

    @Getter @Setter private boolean useExistingTeams = false;

    public CaptainSettings(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void reset() {
        reset(RANDOM_SELECTOR);
    }

    public void reset(Selector selector) {
        if (selector.getType() == SelectorType.PLAYER) {
            throw new UnsupportedOperationException();
        }
        SfTeamContainer teamContainer = this.plugin.getTeamContainer();
        for (SfGameTeam gameTeam : teamContainer.getGameCollection()) {
            this.selectorByTeam.put(gameTeam, selector);
        }
    }

    public void setDefaultSelector(Selector selector) {
        if (selector.getType() == SelectorType.PLAYER) {
            throw new UnsupportedOperationException();
        }
        else if (selector.getType() == SelectorType.RANDOM) {
            this.defaultSelector = RANDOM_SELECTOR;
        }
        else {
            this.defaultSelector = DISABLED_SELECTOR;
        }
    }

    public void updateSelector(SfGameTeam team, Selector selector) {
        if (selector.getType() == SelectorType.RANDOM) {
            this.selectorByTeam.put(team, RANDOM_SELECTOR);
        }
        else if (selector.getType() == SelectorType.DISABLED) {
            this.selectorByTeam.put(team, DISABLED_SELECTOR);
        }
        else {
            PlayerSelector playerSelector = (PlayerSelector) selector;
            updatePlayerSelector(team, playerSelector);
        }
    }

    public void updatePlayerSelector(SfGameTeam team, PlayerSelector selector) {
        List<SfGameTeam> setDefSelectorTeams = this.selectorByTeam.entrySet().stream()
                .filter(entry -> entry.getValue().getType() == SelectorType.PLAYER && ((PlayerSelector) entry.getValue()).getPlayer() == selector.getPlayer())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (SfGameTeam gameTeam : setDefSelectorTeams) {
            this.selectorByTeam.put(gameTeam, defaultSelector);
        }
        this.selectorByTeam.put(team, selector);
    }

    public Setup setup(List<SfPlayer> players) {
        Setup result = new Setup();
        Random random = new Random();

        List<SfGameTeam> needRandomizedTeams = new ArrayList<>();
        for (Map.Entry<SfGameTeam, Selector> entry : this.selectorByTeam.entrySet()) {
            SfGameTeam team = entry.getKey();
            Selector selector = entry.getValue();

            if (selector.getType() == SelectorType.PLAYER) {
                PlayerSelector playerSelector = (PlayerSelector) selector;
                SfPlayer player = playerSelector.getPlayer();
                players.remove(player);
                result.captains.put(team, player);
            }
            else if (selector.getType() == SelectorType.RANDOM) {
                if (useExistingTeams && !team.getPlayers().isEmpty()) {
                    List<SfPlayer> teamPlayers = players.stream().filter(player -> player.getTeam() == team).collect(Collectors.toList());
                    if (!teamPlayers.isEmpty()) {
                        int nextCaptainIndex = random.nextInt(teamPlayers.size());
                        SfPlayer player = teamPlayers.get(nextCaptainIndex);
                        players.remove(player);
                        result.captains.put(team, player);
                        continue;
                    }
                }
                needRandomizedTeams.add(team);
            }
        }

        for (SfPlayer player : players) {
            SfTeam team = player.getTeam();
            if (useExistingTeams && (team == null || team.getType() == SfTeam.Type.GAME)) {
                result.playersWithTeam.add(player);
            }
            else {
                result.players.add(player);
            }
        }

        for (SfGameTeam team : needRandomizedTeams) {
            int nextCaptainIndex = random.nextInt(result.players.size());
            result.captains.put(team, result.players.remove(nextCaptainIndex));
        }

        return result;
    }

    public List<SfGameTeam> getOfflineTeamCaptains() {
        List<SfGameTeam> result = new ArrayList<>();
        for (Map.Entry<SfGameTeam, Selector> entry : this.selectorByTeam.entrySet()) {
            SfGameTeam team = entry.getKey();
            Selector selector = entry.getValue();

            if (selector.getType() != SelectorType.PLAYER) continue;
            if (selector instanceof DebugSelector) continue;

            PlayerSelector playerSelector = (PlayerSelector) selector;
            Optional<Player> optionalPlayer = playerSelector.getPlayer().getPlayer();
            if (!optionalPlayer.isPresent()) {
                result.add(team);
            }
        }
        return result;
    }

    public static class DebugSelector extends PlayerSelector {

        public DebugSelector(String name) {
            super(new SfPlayer(UUID.randomUUID(), name));
        }

    }

    public static class PlayerSelector extends Selector {

        @Getter private final SfPlayer player;

        public PlayerSelector(SfPlayer player) {
            super(SelectorType.PLAYER);
            this.player = player;
        }
    }

    public static class Selector {

        @Getter private final SelectorType type;

        public Selector(SelectorType type) {
            this.type = type;
        }

    }

    public static enum SelectorType {

        PLAYER,
        RANDOM,
        DISABLED

    }

    public static class Setup {

        @Getter @Setter Map<SfGameTeam, SfPlayer> captains = new HashMap<>();
        @Getter @Setter List<SfPlayer> players = new ArrayList<>();
        @Getter @Setter List<SfPlayer> playersWithTeam = new ArrayList<>();

    }

}
