package ru.jekarus.skyfortress.player;

import ru.jekarus.skyfortress.config.SfTeam;

public class SfPlayerState {

    public SfTeam team;

    public boolean isDeath;

    public long respawnedAt = -1;

    public boolean needsRespawn = false;

}
