package ru.jekarus.skyfortress.config_sf2;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.Vec3i;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public enum SfTeam {
    RED(
            "sf_red", "Красная", "Красные", "Красных",
            ChatColor.RED, NamedTextColor.RED,
            new Area3i(
                    new Vec3i(198, 100, 3),
                    new Vec3i(202, 101, 5)
            ), new Vec3i(200, 100, 3),
            new Vec3i(-358, 51, 0), BlockFace.WEST,
            new Vec3i(-347, 82, -9),
            new Vec3i[] {
                    new Vec3i(-359, 51, -6),
                    new Vec3i(-357, 51, -6),
                    new Vec3i(-355, 51, -6),
            }
    ),
    GREEN(
            "sf_green", "Зеленая", "Зеленые", "Зеленых",
            ChatColor.GREEN, NamedTextColor.GREEN,
            new Area3i(
                    new Vec3i(195, 100, -2),
                    new Vec3i(197, 101, 2)
            ), new Vec3i(197, 100, 0),
            new Vec3i(-500, 51, 142), BlockFace.NORTH,
            new Vec3i(-491, 82, 153),
            new Vec3i[] {
                    new Vec3i(-492, 51, 140),
                    new Vec3i(-492, 51, 142),
                    new Vec3i(-492, 51, 144),
            }
    ),
    BLUE(
            "sf_blue", "Синяя", "Синие", "Синих",
            ChatColor.BLUE, NamedTextColor.BLUE,
            new Area3i(
                    new Vec3i(198, 100, -5),
                    new Vec3i(202, 101, -3)
            ), new Vec3i(200, 100, -3),
            new Vec3i(-642, 51, 0), BlockFace.EAST,
            new Vec3i(-653, 82, 9),
            new Vec3i[] {
                    new Vec3i(-639, 51, 7),
                    new Vec3i(-641, 51, 7),
                    new Vec3i(-643, 51, 7),
            }
    ),
    YELLOW(
            "sf_yellow", "Жёлтая", "Жёлтые", "Жёлтых",
            ChatColor.YELLOW, NamedTextColor.YELLOW,
            new Area3i(
                    new Vec3i(203, 100, -2),
                    new Vec3i(205, 101, 2)
            ), new Vec3i(203, 100, 0),
            new Vec3i(-500, 51, -142), BlockFace.SOUTH,
            new Vec3i(-509, 82, -153),
            new Vec3i[] {
                    new Vec3i(-506, 51, -139),
                    new Vec3i(-506, 51, -141),
                    new Vec3i(-506, 51, -143),
            }
    );

    public final String name;
    public final String displayName;
    public final String displayNameTo;
    public final String displayNameOf;
    public final ChatColor chat;
    public final NamedTextColor color;
    public final Area3i join;
    public final Vec3i ready;
    public final Vec3i spawn;
    public final BlockFace face;
    public final Vec3i capture;
    public final Vec3i[] shops;

    private @Nullable Team cachedTeam;

    private SfTeam(
            String name, String displayName, String displayNameTo, String displayNameOf,
            ChatColor chat, NamedTextColor color,
            Area3i join, Vec3i ready,
            Vec3i spawn, BlockFace face,
            Vec3i capture,
            Vec3i[] shops
    ) {
        this.name = name;
        this.displayName = displayName;
        this.displayNameTo = displayNameTo;
        this.displayNameOf = displayNameOf;
        this.chat = chat;
        this.color = color;
        this.join = join;
        this.ready = ready;
        this.spawn = spawn;
        this.face = face;
        this.capture = capture;
        this.shops = shops;
    }

    public static SfTeam get(String name) {
        for (SfTeam sft : SfTeam.values()) {
            if (sft.name.endsWith(name)) return sft;
        }
        return null;
    }

    public static SfTeam get(Player player) {
        final var team = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
        if (team == null) return null;
        return get(team.getName());
    }

    public Team team() {
        if (cachedTeam == null)
            cachedTeam = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam(name);
        return cachedTeam;
    }

    public Collection<Player> players() {
        return team().getEntries().stream().map(entry -> Bukkit.getServer().getPlayer(entry)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Collection<OfflinePlayer> offlinePlayers() {
        return team().getEntries().stream().map(entry -> Bukkit.getServer().getOfflinePlayer(entry)).collect(Collectors.toList());
    }


}