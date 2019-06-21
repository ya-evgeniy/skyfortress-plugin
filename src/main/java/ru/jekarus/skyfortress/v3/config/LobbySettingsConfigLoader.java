package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.LobbyRoom;
import ru.jekarus.skyfortress.v3.serializer.view.LobbyRoomsContainerView;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;

import java.nio.file.Paths;
import java.util.List;

public class LobbySettingsConfigLoader extends ConfigLoader {

    public LobbySettingsConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("lobby.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        this.plugin.getSettings().setLobby(
                serializer.deserialize(node, LobbySettings.class)
        );

        final List<LobbyRoom> rooms = plugin.getLobbyRoomsContainer().getRooms();
        final LobbyRoomsContainerView roomsContainerView = serializer.deserialize(node, LobbyRoomsContainerView.class);
        for (LobbyRoomsContainerView.RoomSettingsView roomView : roomsContainerView.getRooms()) {
            rooms.add(roomView.create(plugin));
        }
//        serializer.deserialize(node, LobbyTeamsView.class).setTo(plugin.getLobby());
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {

    }

}
