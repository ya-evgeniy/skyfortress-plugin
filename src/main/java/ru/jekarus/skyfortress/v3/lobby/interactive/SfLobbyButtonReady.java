package ru.jekarus.skyfortress.v3.lobby.interactive;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLobbyMessages;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.settings.GlobalLobbySettings;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfLobbyButtonReady extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonReady(SkyFortressPlugin plugin, SfLobbyTeam sfLobbyTeam, SfLobbyTeamSettings settings)
    {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        boolean buttonHas = false;
        for (Vector3d button : this.settings.readyButtons)
        {
            if (SfUtils.compare(snapshot.getPosition(), button))
            {
                buttonHas = true;
                break;
            }
        }
        if (!buttonHas)
        {
            return false;
        }

        SfMessages messages = plugin.getMessages();
        SfLobbyMessages lobby = messages.getLobby();

        final GlobalLobbySettings lobbySettings = plugin.getSettings().getGlobalLobby();
        if (this.settings.captain != sfPlayer && lobbySettings.isUseLobbyCaptainSystem()) {
            if (settings.ready) {
                player.sendMessage(
                        lobby.teammateCaptainCantUnready(sfPlayer)
                );
            }
            else {
                player.sendMessage(
                        lobby.teammateCaptainCantReady(sfPlayer)
                );
            }
            return true;
        }

        LobbySettings settings = this.plugin.getSettings().getLobby();
//        if (!settings.canReady && !settings.canUnready) {
//            player.sendMessage(Text.of("(all) Готовность выключена"));
//            return true;
//        }

        boolean newValue = !this.settings.ready;
        if (newValue) {
            if (!lobbySettings.isCanSetReady()) {
                player.sendMessage(
                        lobby.cantReady(sfPlayer)
                );
                return true;
            }
        }
        else {
            if (!lobbySettings.isCanSetUnready()) {
                player.sendMessage(
                        lobby.cantUnready(sfPlayer)
                );
                return true;
            }
        }
        this.setReady(newValue);
        return true;
    }

    public void setReady(boolean value) {
        if (this.settings.ready == value) {
            return;
        }
        this.settings.ready = value;
        DyeColor color;
        if (this.settings.ready)
        {
            this.plugin.getScoreboards().setReady(this.settings.team);
            this.plugin.getLobby().checkStart();
            color = this.settings.team.getBlockColor();
        }
        else
        {
            this.plugin.getScoreboards().setUnready(this.settings.team);
            color = DyeColors.WHITE;
        }
        World world = this.plugin.getWorld();
        for (Vector3d vector3d : this.settings.readyChangedBlocks)
        {
            Location<World> block = world.getLocation(vector3d);
            block.setBlockType(BlockTypes.WOOL);
            block.offer(Keys.DYE_COLOR, color);
        }
    }

}
