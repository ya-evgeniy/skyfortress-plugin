package ru.jekarus.skyfortress;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.respawn.SfRespawn;

import java.util.Arrays;

public class SkyFortressPlugin extends JavaPlugin {

    private final SkyFortress sf = new SkyFortress();

    @Override
    public void onEnable() {
        final var sb = getServer().getScoreboardManager().getMainScoreboard();
        if(!sb.getTeams().stream().map(Team::getName).toList().containsAll(
                Arrays.stream(SfTeam.values()).map(v -> v.name).toList()
        )) {
            getLogger().info("reset teams");
            sb.getTeams().forEach(Team::unregister);
            for (SfTeam sft : SfTeam.values()) {
                sb.registerNewTeam(sft.name).color(sft.color);
            }
        }

        BlockPlayerMove.register(this);
        CaptureSystem.register(this, sf);
        SfSidebar.register(this, sf);
        SfLobby.register(this, sf);
        DevelopersGui.register(this);
        AreaOutline.register(this);
        SfRespawn.register(this, sf);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    @Override
    public void onDisable() {
        AreaOutline.unregister();
        DevelopersGui.unregister();
        SfLobby.unregister();
        SfSidebar.unregister();
        SfRespawn.unregister();
        CaptureSystem.unregister();
        BlockPlayerMove.unregister();
    }

}
