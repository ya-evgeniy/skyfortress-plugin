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

    @Getter @Setter private int initialHealth;
    @Getter @Setter private int additionalHealth;

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
        this.initialHealth = worldMapSettings.getCastleHealth();
        this.deathSeconds = worldMapSettings.getCastleDeathSeconds();
    }

    public int capture() {
        return this.capture(-1);
    }

    public int capture(int value) {
        if (this.isCaptured() || !this.isAlive()) {
            return 0;
        }
        int capturePoints = Math.abs(value);
        if (initialHealth >= capturePoints) {
            initialHealth -= capturePoints;
            return capturePoints;
        }

        int giveAdditionalPoints = initialHealth;
        capturePoints -= initialHealth;

        initialHealth = 0;

        if (additionalHealth >= capturePoints) {
            additionalHealth -= capturePoints;
            return giveAdditionalPoints;
        }

        additionalHealth = 0;
        return giveAdditionalPoints;
    }

    public int getHealth() {
        return this.initialHealth + this.additionalHealth;
    }

    public boolean isCaptured() {
        return this.getHealth() < 1;
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

        this.initialHealth = 0;
        this.additionalHealth = 0;
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
