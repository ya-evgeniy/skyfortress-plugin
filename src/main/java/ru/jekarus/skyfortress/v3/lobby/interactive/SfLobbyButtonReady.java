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
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfLobbyButtonReady extends SfLobbyButton {

    private final SkyFortressPlugin plugin;
    private final SfLobbyTeamSettings settings;

    public SfLobbyButtonReady(SkyFortressPlugin plugin, SfLobbyTeamSettings settings)
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

        this.settings.ready = !this.settings.ready;
        DyeColor color;
        if (this.settings.ready)
        {
            this.plugin.getScoreboards().setReady(this.settings.team);
            this.plugin.getLobby().checkStart();
            color = DyeColors.LIME;
        }
        else
        {
            this.plugin.getScoreboards().setUnready(this.settings.team);
            color = DyeColors.RED;
        }
        World world = this.plugin.getWorld();
        for (Vector3d vector3d : this.settings.readyChangedBlocks)
        {
            Location<World> block = world.getLocation(vector3d);
            block.setBlockType(BlockTypes.WOOL);
            block.offer(Keys.DYE_COLOR, color);
        }

        return true;
    }

}
