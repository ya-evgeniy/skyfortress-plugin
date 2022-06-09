package ru.jekarus.skyfortress.module.gui;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.module.AreaOutline;
import ru.jekarus.skyfortress.module.SfSidebar;
import ru.jekarus.skyfortress.state.SkyFortress;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.config.SfTeam;

public class DevelopersGui extends ChestGuiBase {

    private final SkyFortress sf;

    public DevelopersGui(SkyFortress sf) {
        super(6, "Гуи разработчика");
        this.sf = sf;
        build(getView());
    }

    public void build(ChestGuiView view) {
        int x;
        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            view.set(x, 0, new SfGuiElement(
                    Material.WHITE_WOOL,
                    "тп join " + sft.name
            ).setOnClick(e -> {
                clickArea(e, sft.join, sft.face);
                if(e.getClick() == ClickType.SHIFT_LEFT) {
                    sf.playerJoin(sft, (Player) e.getWhoClicked());
                }
            }));
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            view.set(x, 1, new SfGuiElement(
                    Material.STONE_BUTTON,
                    "тп ready " + sft.name
            ).setOnClick(e -> {
                clickLoc(e, sft.ready, sft.face);
            }));
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            view.set(x, 2, new SfGuiElement(
                    Material.RED_BED,
                    "тп спавн " + sft.name
            ).setOnClick(e -> {
                clickLoc(e, sft.spawn, sft.face);
            }));
            x++;
        }

        x = 0;
        for (SfTeam sft : SfTeam.values()) {
            view.set(x, 3, new SfGuiElement(
                    Material.BEDROCK,
                    "тп захват " + sft.name
            ).setOnClick(e -> {
                clickArea(e, sft.capture, sft.face);
            }));
            x++;
        }

        view.set(SfTeam.values().length, 0, new SfGuiElement(
                Material.BEDROCK,
                "тп exit area"
        ).setOnClick(e -> {
            clickArea(e, SfConfig.LEAVE, BlockFace.NORTH);
            if(e.getClick() == ClickType.SHIFT_LEFT) {
                sf.playerLeave(((Player) e.getWhoClicked()));
            }
        }));

        view.set(SfTeam.values().length, 1, new SfGuiElement(
                Material.STONE_BUTTON,
                "тп force start"
        ).setOnClick(e -> {
            clickLoc(e, SfConfig.FORCE_START, BlockFace.NORTH);
            if(e.getClick() == ClickType.SHIFT_LEFT) {
                sf.gameStart(true);
            } else if(e.getClick() == ClickType.SHIFT_RIGHT) {
                sf.gameStop();
            }
        }));

        view.set(SfTeam.values().length + 1, 0, new SfGuiElement(
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
                    SfSidebar.updateAll();
                }
            }
        }));

        int y;
        y = 0;
        for (SfConfig.ObjectLoc obj : SfConfig.DRAGONS) {
            view.set(SfTeam.values().length + 2, y, new SfGuiElement(
                    Material.DRAGON_EGG,
                    "dragon"
            ).setOnClick(e -> {
                clickArea(e, Area3i.of(
                        obj.loc().add(new Vec3i(-1, -1, -1).mul(SfConfig.OBJECT_HOME_RADIUS)),
                        obj.loc().add(new Vec3i(1, 1, 1).mul(SfConfig.OBJECT_HOME_RADIUS))
                ), obj.dir().toVector());
            }));
            y++;
        }
        for (SfConfig.ObjectLoc obj : SfConfig.WITHERS) {
            view.set(SfTeam.values().length + 2, y, new SfGuiElement(
                    Material.WITHER_SKELETON_SKULL,
                    "wither"
            ).setOnClick(e -> {
                clickArea(e, Area3i.of(
                        obj.loc().add(new Vec3i(-1, -1, -1).mul(SfConfig.OBJECT_HOME_RADIUS)),
                        obj.loc().add(new Vec3i(1, 1, 1).mul(SfConfig.OBJECT_HOME_RADIUS))
                ), obj.dir().toVector());
            }));
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
        clickArea(event, a, face.getDirection());
    }
    public void clickArea(InventoryClickEvent event, Area3i a, Vector vec) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
            final var player = (Player) event.getWhoClicked();
            player.closeInventory();
            AreaOutline.show(player, a);
        }
        if (event.getClick() == ClickType.LEFT) {
            a.middle().teleport(event.getWhoClicked(), vec);
        }
    }

}
