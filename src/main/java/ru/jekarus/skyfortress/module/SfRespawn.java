package ru.jekarus.skyfortress.module;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import ru.jekarus.skyfortress.SkyFortress;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SfRespawn implements Listener {

    private static SfRespawn INS;

    private final Plugin plugin;
    private final SkyFortress sf;

    private final Map<UUID, OfflinePlayer> deathsPlayers = new HashMap<>();

    private BukkitTask respawnTask;

    public SfRespawn(Plugin plugin, SkyFortress sf) {
        this.plugin = plugin;
        this.sf = sf;
    }

    public static void register(Plugin plugin, SkyFortress sf) {
        assert INS == null : "SfRespawn already registered";
        INS = new SfRespawn(plugin, sf);

        Bukkit.getPluginManager().registerEvents(INS, plugin);
    }

    public static void unregister() {
        assert INS != null : "SfRespawn is not registered";
        HandlerList.unregisterAll(INS);

        INS = null;
    }

    private void respawnTick() {
        if (this.deathsPlayers.isEmpty()) {
            this.respawnTask.cancel();
            this.respawnTask = null;

            return;
        }

        final var currentTime = System.currentTimeMillis();

        final var iterator = this.deathsPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            final var offlinePlayer = next.getValue();

            final var sfpState = sf.getPlayerState(offlinePlayer);
            if (sfpState.respawnedAt < 0) {
                iterator.remove();
                continue;
            }

            final var respawnLeft = sfpState.respawnedAt - currentTime;

            final var player = offlinePlayer.getPlayer();
            if (respawnLeft < 1) {
                iterator.remove();

                if (player == null || !player.isOnline()) {
                    sfpState.needsRespawn = true;
                }
                else {
                    respawn(player);
                }
                continue;
            }

            if (player != null) {
                player.showTitle(Title.title(
                        Component.empty(),
                        Component.text(TimeUnit.MILLISECONDS.toSeconds(respawnLeft)),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                ));
            }
        }
    }

    private void respawn(Player player) {
        final var sfpState = sf.getPlayerState(player);
        final var sft = sfpState.team;
        if (sft != null) {
            player.teleport(sft.spawn.toLocation(player.getWorld(), sft.face));
            player.setGameMode(GameMode.SURVIVAL);
        }
        sfpState.respawnedAt = -1;
        sfpState.needsRespawn = false;
        sfpState.isDeath = false;
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        if (!sf.isGameStarted()) return;

        final var player = event.getEntity();
        final var mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        final var playerTeam = mainScoreboard.getPlayerTeam(player);
        if (playerTeam == null) {
            return;
        }

        event.setCancelled(true);

        this.deathsPlayers.put(player.getUniqueId(), player);
        player.setGameMode(GameMode.SPECTATOR);

        final var deathLocation = player.getLocation();
        final var world = deathLocation.getWorld();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) world.dropItemNaturally(deathLocation, item);
        }
        player.getInventory().clear();

        final var droppedExp = event.getDroppedExp();
        if (droppedExp > 0) {
            world.spawnEntity(deathLocation, EntityType.EXPERIENCE_ORB, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
                if (entity instanceof ExperienceOrb orb) {
                    orb.setExperience(droppedExp);
                }
            });
        }
        player.setTotalExperience(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5f);

        new ArrayList<>(player.getActivePotionEffects())
                .stream()
                .map(PotionEffect::getType)
                .forEach(player::removePotionEffect);

        final var deathMessage = event.deathMessage();
        if (deathMessage != null) Bukkit.broadcast(deathMessage);

        final var respawnTime = 5; // getTeam().getRespawnTime();
        final var playerState = sf.getPlayerState(player);
        playerState.respawnedAt = System.currentTimeMillis() + respawnTime * 1000;

        if (this.deathsPlayers.size() == 1) {
            this.respawnTask = Bukkit.getScheduler().runTaskTimer(plugin, this::respawnTick, 0, 1);
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (!sf.isGameStarted()) return;

        final var player = event.getPlayer();
        final var sfpState = sf.getPlayerState(player);

        if (sfpState.needsRespawn) {
            respawn(player);
        }
    }

}