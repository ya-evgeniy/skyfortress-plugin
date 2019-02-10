package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;

public class SfLobbyTeamSerializer implements TypeSerializer<SfLobbyTeam> {

    @Override
    public SfLobbyTeam deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfLobbyTeamSettings settings = new SfLobbyTeamSettings();

        settings.teamId = node.getNode("team_id").getString();
        settings.accepted = node.getNode("accepted").getValue(TypeToken.of(SfLocation.class));

        ConfigurationNode waiting = node.getNode("waiting");
        settings.waitingLocation = waiting.getNode("location").getValue(TypeToken.of(SfLocation.class));
        settings.waitingLeaveButton = waiting.getNode("leave_button").getValue(TypeToken.of(Vector3d.class));

        settings.joinPlate = node.getNode("join_plate").getValue(TypeToken.of(Vector3d.class));
        settings.leavePlate = node.getNode("leave_plate").getValue(TypeToken.of(Vector3d.class));

        settings.acceptButton = node.getNode("accept_button").getValue(TypeToken.of(Vector3d.class));
        settings.denyButton = node.getNode("deny_button").getValue(TypeToken.of(Vector3d.class));

        ConfigurationNode ready = node.getNode("ready");
        settings.readyChangedBlocks = new ArrayList<>();
        for (ConfigurationNode block : ready.getNode("changed_blocks").getChildrenList())
        {
            settings.readyChangedBlocks.add(block.getValue(TypeToken.of(Vector3d.class)));
        }

        settings.readyButtons = new ArrayList<>();
        for (ConfigurationNode block : ready.getNode("buttons").getChildrenList())
        {
            settings.readyButtons.add(block.getValue(TypeToken.of(Vector3d.class)));
        }

        return new SfLobbyTeam(settings);
    }

    @Override
    public void serialize(TypeToken<?> type, SfLobbyTeam obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
