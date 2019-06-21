package ru.jekarus.skyfortress.v3.listener;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
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
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.Optional;

public class PlayerDeathListener {

    private final SkyFortressPlugin plugin;
    private final SfPlayers players;

    public PlayerDeathListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.players = SfPlayers.getInstance();
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
        for (Player anotherPlayer : event.getCause().allOf(Player.class))
        {
            if (anotherPlayer != player)
            {
                Location<World> location = anotherPlayer.getLocation();
                Inventory hotbar = anotherPlayer.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
                InventoryTransactionResult result = hotbar.offer(ItemStack.of(ItemTypes.GOLD_INGOT, 1));
                if (!result.getRejectedItems().isEmpty())
                {
                    Inventory playerInventory = anotherPlayer.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
                    for (ItemStackSnapshot stackSnapshot : result.getRejectedItems())
                    {
                        InventoryTransactionResult resultOffer = playerInventory.offer(stackSnapshot.createStack());
                        for (ItemStackSnapshot itemStackSnapshot : resultOffer.getRejectedItems())
                        {
                            Item item = (Item) location.createEntity(EntityTypes.ITEM);
                            item.offer(Keys.REPRESENTED_ITEM, itemStackSnapshot);
                            item.offer(Keys.VELOCITY, Vector3d.from(0));
                            location.spawnEntity(item);
                        }
                    }
                }
            }
        }

        this.checkPlayerLost(player);
        Task.builder().delayTicks(1).execute(player::respawnPlayer).submit(this.plugin);
    }

    private void checkPlayerLost(Player player)
    {
        Optional<SfPlayer> optionalSfPlayer = this.players.getPlayer(player);
        if (!optionalSfPlayer.isPresent())
        {
            return;
        }
        SfPlayer sfPlayer = optionalSfPlayer.get();
        SfTeam team = sfPlayer.getTeam();
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
        sfPlayer.setZone(PlayerZone.LOBBY);

        plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, sfPlayer);
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
