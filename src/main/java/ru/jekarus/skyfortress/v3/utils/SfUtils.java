package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

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

    public static boolean compareByInt(Location<World> v1, Vector3d v2) {
        return v1.getBlockX() == v2.getFloorX() && v1.getBlockY() == v2.getFloorY() && v1.getBlockZ() == v2.getFloorZ();
    }

    public static void setPlayerSpectator(Player player)
    {
        SkyFortressPlugin plugin = SkyFortressPlugin.getInstance();
        PlayersDataContainer players = plugin.getPlayersDataContainer();

        PlayerData playerData = players.getOrCreateData(player);
        plugin.getTeamContainer().getSpectatorTeam().addPlayer(plugin, playerData);

        player.offer(Keys.GAME_MODE, GameModes.SPECTATOR);

        Location<World> centerLocation = new Location<>(plugin.getWorld(), 2500, 127, 0);
        player.setLocation(centerLocation);
    }

    public static DyeColor getDyeColorFromTextColor(TextColor color) {
        if (color.equals(TextColors.AQUA)) {
            return DyeColors.LIGHT_BLUE;
        }
        if (color.equals(TextColors.BLACK)) {
            return DyeColors.BLACK;
        }
        if (color.equals(TextColors.BLUE)) {
            return DyeColors.BLUE;
        }
        if (color.equals(TextColors.DARK_AQUA)) {
            return DyeColors.CYAN;
        }
        if (color.equals(TextColors.DARK_BLUE)) {
            return DyeColors.MAGENTA;
        }
        if (color.equals(TextColors.DARK_GRAY)) {
            return DyeColors.GRAY;
        }
        if (color.equals(TextColors.DARK_GREEN)) {
            return DyeColors.GREEN;
        }
        if (color.equals(TextColors.DARK_PURPLE)) {
            return DyeColors.PURPLE;
        }
        if (color.equals(TextColors.DARK_RED)) {
            return DyeColors.BROWN;
        }
        if (color.equals(TextColors.GOLD)) {
            return DyeColors.ORANGE;
        }
        if (color.equals(TextColors.GRAY)) {
            return DyeColors.SILVER;
        }
        if (color.equals(TextColors.GREEN)) {
            return DyeColors.LIME;
        }
        if (color.equals(TextColors.LIGHT_PURPLE)) {
            return DyeColors.PINK;
        }
        if (color.equals(TextColors.RED)) {
            return DyeColors.RED;
        }
        if (color.equals(TextColors.YELLOW)) {
            return DyeColors.YELLOW;
        }
        return DyeColors.WHITE;
    }



}
