package ru.jekarus.skyfortress.v3.lobby;

import ru.jekarus.skyfortress.v3.utils.SfLocation;

public class SfLobbySettings {

    public boolean canSpectate = true;

    public boolean canJoin = true;
    public boolean canLeave = true;
    public boolean canReady = true;
    public boolean canUnready = true;

    public boolean canAccept = true;
    public boolean canCancel = true;

    public boolean useLobbyCaptainSystem = true;

    public SfLocation center;
    public double min_y;

}
