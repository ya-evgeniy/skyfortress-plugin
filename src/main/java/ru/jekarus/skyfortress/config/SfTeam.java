package ru.jekarus.skyfortress.config;

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
            Area3i.of(
                    new Vec3i(498, 101, 4),
                    new Vec3i(502, 103, 9)
            ),
            new Vec3i(500, 101, 4),
            new Vec3i(2606, 97, 0), BlockFace.WEST,
            new Area3i(new Vec3i(2614, 122, 10))
    ),
    GREEN(
            "sf_green", "Зеленая", "Зеленые", "Зеленых",
            ChatColor.GREEN, NamedTextColor.GREEN,
            Area3i.of(
                    new Vec3i(492, 101, -2),
                    new Vec3i(496, 103, 2)
            ),
            new Vec3i(496, 101, 0),
            new Vec3i(2500, 97, 106), BlockFace.NORTH,
            new Area3i(new Vec3i(2490, 122, 114))
    ),
    BLUE(
            "sf_blue", "Синяя", "Синие", "Синих",
            ChatColor.BLUE, NamedTextColor.BLUE,
            Area3i.of(
                    new Vec3i(498, 101, -8),
                    new Vec3i(502, 103, -4)
            ),
            new Vec3i(500, 101, -4),
            new Vec3i(2394, 97, 0), BlockFace.EAST,
            new Area3i(new Vec3i(2386, 122, -10))
    ),
    YELLOW(
            "sf_yellow", "Жёлтая", "Жёлтые", "Жёлтых",
            ChatColor.YELLOW, NamedTextColor.YELLOW,
            Area3i.of(
                    new Vec3i(504, 101, -2),
                    new Vec3i(508, 103, 2)
            ),
            new Vec3i(504, 101, 0),
            new Vec3i(2500, 97, -106), BlockFace.SOUTH,
            new Area3i(new Vec3i(2510, 122, -114))
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
    public final Area3i capture;

    private @Nullable Team cachedTeam;

    private SfTeam(
            String name, String displayName, String displayNameTo, String displayNameOf,
            ChatColor chat, NamedTextColor color,
            Area3i join, Vec3i ready,
            Vec3i spawn, BlockFace face,
            Area3i capture
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
    }

    @Nullable
    public static SfTeam get(String name) {
        for (SfTeam sft : SfTeam.values()) {
            if (sft.name.endsWith(name)) return sft;
        }
        return null;
    }

    @Nullable
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
