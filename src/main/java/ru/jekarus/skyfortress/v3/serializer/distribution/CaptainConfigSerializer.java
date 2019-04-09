package ru.jekarus.skyfortress.v3.serializer.distribution;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigCaptain;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfigPlayer;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;

public class CaptainConfigSerializer implements TypeSerializer<CaptainConfig> {

    @Nullable
    @Override
    public CaptainConfig deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode node) throws ObjectMappingException {
        CaptainConfig config = new CaptainConfig();

        config.maxSelectTime = node.getNode("select_time").getInt();

        ConfigurationNode mainCaptainNode = node.getNode("captain");
        config.mainCaptainCell = mainCaptainNode.getNode("location").getValue(TypeToken.of(SfLocation.class));

        for (ConfigurationNode changedBlock : mainCaptainNode.getNode("changed_blocks").getChildrenList()) {
            SfLocation location = changedBlock.getValue(TypeToken.of(SfLocation.class));
            if (location != null) {
                config.mainCaptainCellChangedBlocks.add(location);
            }
        }

        config.captains = new ArrayList<>();
        ConfigurationNode captainsNode = node.getNode("captains");
        for (ConfigurationNode captainNode : captainsNode.getChildrenList()) {
            CaptainConfigCaptain captainConfigCaptain = new CaptainConfigCaptain();
            captainConfigCaptain.teamId = captainNode.getNode("team_id").getString();
            captainConfigCaptain.cell = captainNode.getNode("location").getValue(TypeToken.of(SfLocation.class));
            for (ConfigurationNode changedBlock : captainNode.getNode("changed_blocks").getChildrenList()) {
                SfLocation location = changedBlock.getValue(TypeToken.of(SfLocation.class));
                if (location != null) {
                    captainConfigCaptain.changedBlocks.add(location);
                }
            }
            config.captains.add(captainConfigCaptain);
        }

        config.players = new ArrayList<>();
        ConfigurationNode playersNode = node.getNode("players");
        for (ConfigurationNode playerNode : playersNode.getChildrenList()) {
            CaptainConfigPlayer captainConfigPlayer = new CaptainConfigPlayer();
            captainConfigPlayer.cell = playerNode.getNode("location").getValue(TypeToken.of(SfLocation.class));
            for (ConfigurationNode changedBlock : playerNode.getNode("changed_blocks").getChildrenList()) {
                SfLocation location = changedBlock.getValue(TypeToken.of(SfLocation.class));
                if (location != null) {
                    captainConfigPlayer.changedBlocks.add(location);
                }
            }
            config.players.add(captainConfigPlayer);
        }

        return config;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable CaptainConfig obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }

}
