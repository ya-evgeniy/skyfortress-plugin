package ru.jekarus.skyfortress.v3.lobby.interactive;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.SfLobbySettings;
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


        if (this.settings.captain != sfPlayer && plugin.getLobby().getSettings().useLobbyCaptainSystem) {
            player.sendMessage(Text.of("Ты не капитан :("));
            return true;
        }

        SfLobbySettings settings = this.plugin.getLobby().getSettings();
        if (!settings.canReady && !settings.canUnready) {
            player.sendMessage(Text.of("(all) Готовность выключена"));
            return true;
        }

        boolean newValue = !this.settings.ready;
        if (newValue) {
            if (!settings.canReady) {
                player.sendMessage(Text.of("Готовность выключена"));
                return true;
            }
        }
        else {
            if (!settings.canUnready) {
                player.sendMessage(Text.of("(Ан)готовность выключена"));
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
