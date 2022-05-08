package ru.jekarus.skyfortress.module;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.ChestGuiBase;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;

import java.util.List;

public class DevelopersGui extends ChestGuiBase {

    private static DevelopersGui INST;

    public static void register(Plugin plugin) {
        if(INST == null) INST = new DevelopersGui();
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    public static void open(Player player) {
        if(INST == null) INST = new DevelopersGui();
        INST.openInventory(player);
    }

    public DevelopersGui() {
        super(6, "Гуи разработчика");

        int x;
        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            set(
                    x, 0,
                    Material.WHITE_WOOL,
                    "тп join " + sft.name
            ).setOnClick(e -> {
                clickArea(e, sft.join, sft.face);
            });
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            set(
                    x, 1,
                    Material.STONE_BUTTON,
                    "тп ready " + sft.name
            ).setOnClick(e -> {
                clickLoc(e, sft.ready, sft.face);
            });
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            set(
                    x, 2,
                    Material.RED_BED,
                    "тп спавн " + sft.name
            ).setOnClick(e -> {
                clickLoc(e, sft.spawn, sft.face);
            });
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            set(
                    x, 3,
                    Material.BEDROCK,
                    "тп захват " + sft.name
            ).setOnClick(e -> {
                clickArea(e, sft.capture, sft.face);
            });
            x++;
        }

        set(
                SfTeam.values().length, 0,
                Material.BEDROCK,
                "тп exit area"
        ).setOnClick(e -> {
            clickArea(e, SfConfig.LEAVE, BlockFace.NORTH);
        });

        set(
                SfTeam.values().length, 1,
                Material.STONE_BUTTON,
                "тп force start"
        ).setOnClick(e -> {
            clickLoc(e, SfConfig.FORCE_START, BlockFace.NORTH);
        });
    }

    public void clickLoc(InventoryClickEvent event, Vec3i v, BlockFace face) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
            final var player = (Player) event.getWhoClicked();
            player.closeInventory();
            AreaOutline.show(player, v);
        }
        if (event.getClick() == ClickType.LEFT) {
            v.teleport(event.getWhoClicked(), face);
        }
    }

    public void clickArea(InventoryClickEvent event, Area3i a, BlockFace face) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
            final var player = (Player) event.getWhoClicked();
            player.closeInventory();
            AreaOutline.show(player, a);
        }
        if (event.getClick() == ClickType.LEFT) {
            a.middle().teleport(event.getWhoClicked(), face);
        }
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
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer().isOp()) {
            if (event.getItem() != null && event.getItem().getType() == Material.EMERALD) {
                final var lore = event.getItem().getItemMeta().getLore();
                if(lore != null && !lore.isEmpty()) {
                    if (lore.get(lore.size() - 1).equals("!dev")) {
                        openInventory(event.getPlayer());
                    }
                }
            }
        }
    }

}
