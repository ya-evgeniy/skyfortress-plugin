package ru.jekarus.skyfortress.v3.listener;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.InventoryUtils;

import java.util.Collections;
import java.util.Optional;

public class PlayerDeathListener {

    private final SkyFortressPlugin plugin;
    private final PlayersDataContainer playersData;

    public PlayerDeathListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.playersData = plugin.getPlayersDataContainer();
    }

    public void register()
    {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player)
    {
        final Optional<User> optionalUser = event.getContext().get(EventContextKeys.NOTIFIER);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final Optional<Player> optionalKiller = user.getPlayer();
            optionalKiller.ifPresent(this::giveIngot);
        }
        else {
            Optional<EntityDamageSource> optionalDamageSource = event.getCause().first(EntityDamageSource.class);
            if (optionalDamageSource.isPresent()) {
                EntityDamageSource damageSource = optionalDamageSource.get();
                Entity killerEntity = damageSource.getSource();
                if (killerEntity.getType().equals(EntityTypes.PLAYER)) {
                    giveIngot((Player) killerEntity);
                }
                else if (killerEntity instanceof Projectile) {
                    final Projectile projectile = (Projectile) killerEntity;
                    final ProjectileSource shooter = projectile.getShooter();
                    if (shooter instanceof Player) {
                        giveIngot((Player) shooter);
                    }
                }
            }
            else {
                System.out.println(event);
                System.out.println(event.getCause());
                System.out.println(event.getContext());
            }
        }


        Task.builder().delayTicks(1).execute(player::respawnPlayer).submit(this.plugin);

        final Optional<PlayerData> optionalPlayerData = playersData.get(player);
        if (!optionalPlayerData.isPresent()) {
            return;
        }

        final PlayerData playerData = optionalPlayerData.get();
        this.checkPlayerLost(player, playerData);

        playerData.setCapturePoints(0);
    }

    private void giveIngot(Player player) {
        final ItemStack goldIngot = ItemStack.of(ItemTypes.GOLD_INGOT, 1);
        InventoryUtils.drop(
                player.getLocation(),
                InventoryUtils.put(player, Collections.singletonList(goldIngot))
        );
    }

    private void checkPlayerLost(Player player, PlayerData playerData)
    {
        SfTeam team = playerData.getTeam();
        if (team.getType() != SfTeam.Type.GAME)
        {
            return;
        }
        SfGameTeam gameTeam = (SfGameTeam) team;
        SfCastle castle = gameTeam.getCastle();
        if (!castle.isCaptured())
        {
            return;
        }
        playerData.setZone(PlayerZone.LOBBY);

        plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, playerData);
        player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
        player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
            effects.addElement(
                    PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
            );
            player.offer(effects);
        });

//        SfUtils.setPlayerSpectator(player);
        CastleDeathEngine.checkCapturedCastle(this.plugin, castle);
    }

}
