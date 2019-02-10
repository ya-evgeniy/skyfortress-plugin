package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;

public class SfUtils {

    public static boolean checkLocation(Player player, Location<World> location)
    {
        Location<World> playerLocation = player.getLocation();
        return playerLocation.getBlockX() == location.getBlockX() &&
                playerLocation.getBlockY() == location.getBlockY() &&
                playerLocation.getBlockZ() == location.getBlockZ();
    }

    public static boolean compare(Vector3d v1, Vector3d v2)
    {
        return v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
    }

    public static boolean compare(Vector3i v1, Vector3d v2)
    {
        return v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
    }

    public static boolean compare(Location<World> v1, Vector3d v2)
    {
        return v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
    }

    public static void setPlayerSpectator(Player player)
    {
        SkyFortressPlugin plugin = SkyFortressPlugin.getInstance();
        SfPlayers players = SfPlayers.getInstance();

        SfPlayer sfPlayer = players.getOrCreatePlayer(player);
        plugin.getTeamContainer().getSpectatorTeam().addPlayer(plugin, sfPlayer);

        player.offer(Keys.GAME_MODE, GameModes.SPECTATOR);

        Location<World> centerLocation = new Location<>(plugin.getWorld(), 2500, 127, 0);
        player.setLocation(centerLocation);
    }

}
