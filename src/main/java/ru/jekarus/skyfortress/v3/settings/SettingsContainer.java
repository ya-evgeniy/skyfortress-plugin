package ru.jekarus.skyfortress.v3.settings;

import lombok.Getter;
import lombok.Setter;

public class SettingsContainer {

    @Getter @Setter private GlobalSettings global = new GlobalSettings();

    @Getter @Setter private LobbySettings lobby = new LobbySettings();

    @Getter @Setter private GlobalLobbySettings globalLobby = new GlobalLobbySettings();

    @Getter @Setter private WorldMapSettings worldMap = new WorldMapSettings();

}
