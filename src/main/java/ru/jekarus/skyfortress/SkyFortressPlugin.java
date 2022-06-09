package ru.jekarus.skyfortress;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import ru.jekarus.skyfortress.config.SfTeam;
import ru.jekarus.skyfortress.module.*;
import ru.jekarus.skyfortress.module.SfFightSystem;
import ru.jekarus.skyfortress.module.SfSimpleEmeraldModule;
import ru.jekarus.skyfortress.module.SfSimpleIronModule;
import ru.jekarus.skyfortress.module.SfSimpleStoneModule;
import ru.jekarus.skyfortress.module.SfSimpleWoodModule;
import ru.jekarus.skyfortress.module.gui.DevelopersGui;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.Arrays;
import java.util.List;

public class SkyFortressPlugin extends JavaPlugin implements Listener {

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

        sf.init();
        BlockPlayerMove.register(this);
        CaptureSystem.register(this, sf);
        SfSidebar.register(this, sf);
        SfLobby.register(this, sf);
        SfGuiModule.register(this, sf);
        AreaOutline.register(this);
        SfRespawn.register(this, sf);
        SfDamageModule.register(this, sf);
        SfFightSystem.register(this, sf);
        SfDragonModule.register(this, sf);
        SfWitherModule.register(this, sf);
        SfShopModule.register(this, sf);
        SfSimpleWoodModule.register(this, sf);
        SfSimpleStoneModule.register(this, sf);
        SfSimpleIronModule.register(this, sf);
        SfSimpleEmeraldModule.register(this, sf);
        SfEnchantmentsModule.register(this, sf);
        Bukkit.getPluginManager().registerEvents(this, this);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.closeInventory();
        }

        for (SfTeam sft : SfTeam.values()) {
            for (OfflinePlayer player : sft.offlinePlayers()) {
                sf.playerJoin(sft, player);
            }
        }
////        final var entity = getServer().getEntity(UUID.fromString("6b407693-93ca-1250-840b-29687d1aeea6"));
//        final var entity = getServer().getEntity(UUID.fromString("387d7d42-e56f-10f2-8f29-e2e5409aab5d"));
////        final var entity = getServer().getEntity(UUID.fromString("53830d63-a4f2-18e8-8833-4cb2a529a3cb"));
//        final var en = ((CraftVillager) entity).getHandle();
//        System.out.println(en.f(new NBTTagCompound()).e_());
        getServer().getScheduler().runTask(this, () -> {
            Bukkit.broadcastMessage("Sf3 reloaded");
        });
    }


    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
        SfEnchantmentsModule.unregister();
        SfSimpleEmeraldModule.unregister();
        SfSimpleIronModule.unregister();
        SfSimpleStoneModule.unregister();
        SfSimpleWoodModule.unregister();
        SfShopModule.unregister();
        SfWitherModule.unregister();
        SfDragonModule.unregister();
        SfFightSystem.unregister();
        SfDamageModule.unregister();
        SfRespawn.unregister();
        AreaOutline.unregister();
        SfGuiModule.unregister();
        SfLobby.unregister();
        SfSidebar.unregister();
        CaptureSystem.unregister();
        BlockPlayerMove.unregister();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        final var msg = PlainTextComponentSerializer.plainText().serialize(event.message());
        if(event.getPlayer().isOp()) {
            if(msg.equals("!dev")) {
                final var item = new ItemStack(Material.EMERALD);
                final ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "Инструмент разработчиков");
                meta.setLore(List.of("!dev"));
                item.setItemMeta(meta);
                event.getPlayer().getInventory().addItem(item);
                event.setCancelled(true);
            }
            if (msg.equals("!exp")) {
                final var sfpState = sf.getPlayerState(event.getPlayer());
                if (sfpState.team != null) {
                    final var sftState = sf.getTeamState(sfpState.team);
                    event.getPlayer().sendMessage("lvl: " + sftState.getLevel() + " exp: " + sftState.experience);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer().isOp()) {
            if (event.getItem() != null && event.getItem().getType() == Material.EMERALD) {
                final var lore = event.getItem().getItemMeta().getLore();
                if(lore != null && !lore.isEmpty()) {
                    if (lore.get(lore.size() - 1).equals("!dev")) {
                        final DevelopersGui dev = new DevelopersGui(sf);
                        dev.openInventory(event.getPlayer());
                    }
                }
            }
        }
    }

}
