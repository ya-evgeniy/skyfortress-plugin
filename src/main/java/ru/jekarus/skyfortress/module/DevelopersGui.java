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
import org.bukkit.util.Vector;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.ChestGuiBase;
import ru.jekarus.skyfortress.state.SkyFortress;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;

import java.util.List;

public class DevelopersGui extends ChestGuiBase {

    private static DevelopersGui INST;

    private final Plugin plugin;
    private final SkyFortress sf;

    public static void register(Plugin plugin, SkyFortress sf) {
        if(INST == null) INST = new DevelopersGui(plugin, sf);
        plugin.getServer().getPluginManager().registerEvents(INST, plugin);
    }

    public static void unregister() {
        if(INST != null) {
            HandlerList.unregisterAll(INST);
            INST = null;
        }
    }

    public DevelopersGui(Plugin plugin, SkyFortress sf) {
        super(6, "Гуи разработчика");

        this.plugin = plugin;
        this.sf = sf;

        int x;
        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            set(
                    x, 0,
                    Material.WHITE_WOOL,
                    "тп join " + sft.name
            ).setOnClick(e -> {
                clickArea(e, sft.join, sft.face);
                if(e.getClick() == ClickType.SHIFT_LEFT) {
                    sf.playerJoin(sft, (Player) e.getWhoClicked());
                }
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
            if(e.getClick() == ClickType.SHIFT_LEFT) {
                sf.playerLeave(((Player) e.getWhoClicked()));
            }
        });

        set(
                SfTeam.values().length, 1,
                Material.STONE_BUTTON,
                "тп force start"
        ).setOnClick(e -> {
            clickLoc(e, SfConfig.FORCE_START, BlockFace.NORTH);
            if(e.getClick() == ClickType.SHIFT_LEFT) {
                sf.gameStart(true);
            } else if(e.getClick() == ClickType.SHIFT_RIGHT) {
                sf.gameStop();
            }
        });

        set(
                SfTeam.values().length + 1, 0,
                Material.EXPERIENCE_BOTTLE,
                "experience",
                "ЛКМ: +=2",
                "Shift+ЛКМ: +=8",
                "ПКМ: +=25",
                "Shift+ПКМ: +=100",
                "Q: =0"
        ).setOnClick(e -> {
            final var player = (Player) e.getWhoClicked();
            final var pstate = sf.getPlayerState(player);
            if (pstate != null && pstate.team != null) {
                final var state = sf.getTeamState(pstate.team);
                if (state != null) {
                    switch (e.getClick()) {
                        case LEFT -> {
                            state.experience += 2.0;
                        }
                        case SHIFT_LEFT -> {
                            state.experience += 8.0;
                        }
                        case RIGHT -> {
                            state.experience += 25.0;
                        }
                        case SHIFT_RIGHT -> {
                            state.experience += 100.0;
                        }
                        case DROP -> {
                            state.experience = 0;
                        }
                    }
                }
            }
        });

        int y;
        y = 0;
        for (SfConfig.DragonLoc dragon : SfConfig.DRAGONS) {
            set(
                    SfTeam.values().length + 2, y,
                    Material.DRAGON_EGG,
                    "dragon"
            ).setOnClick(e -> {
                clickLoc(e, dragon.loc(), dragon.dir().toVector());
            });
            y++;
        }
    }

    public void clickLoc(InventoryClickEvent event, Vec3i v, BlockFace face) {
        clickLoc(event, v, face.getDirection());
    }
    public void clickLoc(InventoryClickEvent event, Vec3i v, Vector vec) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
            final var player = (Player) event.getWhoClicked();
            player.closeInventory();
            AreaOutline.show(player, v);
        }
        if (event.getClick() == ClickType.LEFT) {
            v.teleport(event.getWhoClicked(), vec);
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
                        openInventory(event.getPlayer());
                    }
                }
            }
        }
    }

}
