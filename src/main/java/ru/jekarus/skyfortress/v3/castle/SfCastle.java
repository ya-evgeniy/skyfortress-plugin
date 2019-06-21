package ru.jekarus.skyfortress.v3.castle;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.settings.SettingsContainer;
import ru.jekarus.skyfortress.v3.settings.WorldMapSettings;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.Objects;

public class SfCastle {

    private static int ID = 0;
    private int id = ID++;

    @Getter @Setter private String uniqueId;
    @Getter @Setter private String teamId;
    @Getter @Setter private SfCastlePositions positions;

    @Getter @Setter private SfGameTeam team;

    @Getter private int health;
    @Getter private boolean alive = true;

    @Getter private int deathSeconds;
    @Getter @Setter private boolean showDeathSeconds = false;
    @Getter @Setter private long deathTime = -1;

    @Getter private boolean played = true;
    @Getter @Setter private int place = 0;

    public SfCastle(@NonNull String uniqueId, @NonNull String teamId, @NonNull SfCastlePositions positions) {
        this.uniqueId = uniqueId;
        this.teamId = teamId;
        this.positions = positions;

        SettingsContainer settings = SkyFortressPlugin.getInstance().getSettings();
        final WorldMapSettings worldMapSettings = settings.getWorldMap();
        this.health = worldMapSettings.getCastleHealth();
        this.deathSeconds = worldMapSettings.getCastleDeathSeconds();
    }

    public boolean capture(SfScoreboards scoreboards) {
        return this.capture(scoreboards, -1);
    }

    public boolean capture(SfScoreboards scoreboards, int value) {
        if (this.isCaptured() || !this.isAlive()) {
            return true;
        }
        this.health += value;
        scoreboards.updateLeftSeconds(this.team);
        return this.isCaptured();
    }

    public boolean isCaptured() {
        return this.health < 1;
    }

    public void setDeath(boolean needBroadcast) {
        this.alive = false;
        SkyFortressPlugin plugin = SkyFortressPlugin.getInstance();

        if (needBroadcast) {
            SfMessages messages = plugin.getMessages();
            messages.broadcast(
                    messages.getGame().teamLost(this.getTeam()), true
            );
        }
        else {
            this.played = false;
        }

        this.health = 0;
        this.place = CastleDeathEngine.getCastlePlace(plugin);

        SfScoreboards scoreboards = plugin.getScoreboards();
        scoreboards.updateLeftSeconds(this.team);
        scoreboards.updateDeath(this.team);

        plugin.getGame().checkWin();
    }

    public void setDeathSeconds(SfScoreboards scoreboards, int deathSeconds) {
        this.deathSeconds = deathSeconds;
        scoreboards.updateLeftSeconds(this.team);
    }

    public void resetDeathSeconds(SfScoreboards scoreboards) {
        this.deathSeconds = SkyFortressPlugin.getInstance().getSettings().getWorldMap().getCastleDeathSeconds(); // fixme
        scoreboards.updateLeftSeconds(this.team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SfCastle castle = (SfCastle) o;
        return id == castle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
