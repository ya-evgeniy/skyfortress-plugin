package ru.jekarus.skyfortress.state;

import ru.jekarus.skyfortress.config.SfTeam;

public class SfPlayerState {

    public SfTeam team;

    public boolean isDeath;

    public long respawnedAt = -1;

    public boolean needsRespawn = false;

    public int kills = 0;
    public int deaths = 0;
    public int assists = 0;

}
