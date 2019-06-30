package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

import java.util.Optional;

public class FriendFireListener {

    private final SkyFortressPlugin plugin;
    private final PlayersDataContainer playersData;

    public FriendFireListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.playersData = plugin.getPlayersDataContainer();
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void on(DamageEntityEvent event, @First EntityDamageSource source) {
        if (!event.getTargetEntity().getType().equals(EntityTypes.PLAYER)) {
            return;
        }

        Player target = (Player) event.getTargetEntity();
        Player damager = null;

        Entity killerEntity = source.getSource();
        if (killerEntity.getType().equals(EntityTypes.PLAYER)) {
            damager = (Player) killerEntity;
        }
        else if (killerEntity instanceof Projectile) {
            final Projectile projectile = (Projectile) killerEntity;
            final ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                damager = (Player) shooter;
            }
        }

        if (damager == null) {
            return;
        }

        final Optional<PlayerData> optionalDamagerData = playersData.get(damager);
        if (!optionalDamagerData.isPresent()) return;

        final Optional<PlayerData> optionalTargetData = playersData.get(target);
        if (!optionalTargetData.isPresent()) return;

        final PlayerData damagerData = optionalDamagerData.get();
        final PlayerData targetData = optionalTargetData.get();

        if (damagerData.getTeam() == targetData.getTeam()) {
            event.setCancelled(true);
        }
    }

}
