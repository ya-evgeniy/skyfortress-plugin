package ru.jekarus.skyfortress.module.gui.enchantments;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.jekarus.skyfortress.module.gui.ChestGuiBase;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;
import ru.jekarus.skyfortress.module.gui.SfGuiElement;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EnchantmentsGui extends ChestGuiBase {

    private final ToolsEnchantmentSec tools;
    private final ArmorEnchantmentSec armor;
    private final SwordsEnchantmentSec swords;
    private final BowEnchantmentSec bow;
    private final CrossbowEnchantmentSec crossbow;
    private final TridentEnchantmentSec trident;
    private final List<IEnchGuiSection> sections;

    private final ChestGuiView main;

    private final SfGuiElement exit;
    private final SfGuiElement input;
    private final SfGuiElement output;
    private final List<SfGuiElement> sectionEls;
    private final HumanEntity human;

    public EnchantmentsGui(SkyFortress sf, HumanEntity human) {
        super(3, "Стол разочарований");
        this.human = human;

        main = getView();
        tools = new ToolsEnchantmentSec(this);
        armor = new ArmorEnchantmentSec(this);
        swords = new SwordsEnchantmentSec(this);
        bow = new BowEnchantmentSec(this);
        crossbow = new CrossbowEnchantmentSec(this);
        trident = new TridentEnchantmentSec(this);

        sections = Arrays.asList(
                tools, armor, swords, bow, crossbow, trident
        );
        exit = new SfGuiElement(
                Material.REDSTONE_BLOCK,
                "Выход"
        ).setOnClick(e -> {
            if (e.getClick() == ClickType.LEFT) {
                clearInput(e.getWhoClicked(), e.getView().getBottomInventory());
                setView(main);
            }
        });

        this.output = new SfGuiElement(new ItemStack(Material.AIR))
                .setOnClick(e -> {
                    final var item = e.getCursor();
                    if(inputChanged(item)) e.setCancelled(false);
                }).setOnDrag(e -> {
                    if (e.getRawSlots().size() == 1) {
                        final var item = e.getNewItems().values().iterator().next();
                        if(item != null && item.getType() != Material.AIR) {
                            e.setCancelled(false);
                        }
                    }
                }).setOnCollect(e -> {
                    if(e.touched.size() == 1) {
                        var item = e.touched.iterator().next();
                        if(inputChanged(null)) e.event.setCancelled(false);
                    }
                }).setOnMove(e -> {
                    if(e.moveFrom) {
                        if(inputChanged(null)) {
                            e.event.setCancelled(false);
                        }
                    } else {
                        final var item = e.event.getCurrentItem();
                        if(inputChanged(item)) e.event.setCancelled(false);
                    }
                });
        this.input = new SfGuiElement(null)
                .setOnClick(e -> {
                    final var item = e.getCursor();
                    if(inputChanged(item)) e.setCancelled(false);
                }).setOnDrag(e -> {
                    if (e.getRawSlots().size() == 1) {
                        final var item = e.getNewItems().values().iterator().next();
                        if(inputChanged(item)) e.setCancelled(false);
                    }
                }).setOnCollect(e -> {
                    if(e.touched.size() == 1) {
                        if(inputChanged(null)) e.event.setCancelled(false);
                    }
                }).setOnMove(e -> {
                    if(e.moveFrom) {
                        if(inputChanged(null)) {
                            e.event.setCancelled(false);
                        }
                    } else {
                        final var item = e.event.getCurrentItem();
                        if(inputChanged(item)) e.event.setCancelled(false);
                    }
                });

        sectionEls = Arrays.asList(
                new SfGuiElement(
                        Material.GOLDEN_PICKAXE,
                        is -> {
                            is.editMeta(im -> {
                                im.setDisplayName("Чары для инструментов");
                            });
                            is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        }
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(tools.getDynView());
                    }
                }),
                new SfGuiElement(
                        Material.GOLDEN_CHESTPLATE,
                        is -> {
                            is.editMeta(im -> {
                                im.setDisplayName("Чары для брони");
                            });
                            is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        }
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(armor.getDynView());
                    }
                }),
                new SfGuiElement(
                        Material.GOLDEN_SWORD,
                        is -> {
                            is.editMeta(im -> {
                                im.setDisplayName("Чары для мечей");
                            });
                            is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        }
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(swords.getDynView());
                    }
                }),
                new SfGuiElement(
                        Material.BOW,
                        "Чары для луков"
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(bow.getDynView());
                    }
                }),
                new SfGuiElement(
                        Material.CROSSBOW,
                        "Чары для арбалетов"
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(crossbow.getDynView());
                    }
                }),
                new SfGuiElement(
                        Material.TRIDENT,
                        is -> {
                            is.editMeta(im -> {
                                im.setDisplayName("Чары для трезубцев");
                            });
                            is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        }
                ).setOnClick(e -> {
                    if (e.getClick() == ClickType.LEFT) {
                        setView(trident.getDynView());
                    }
                })
        );
        for (IEnchGuiSection section : sections) drawSection(section);
        {
            final var builder = new EnchGuiBuilder(getView(), false);
            builder.drawBackground();
            drawMain(builder);
            drawInput(builder);
        }
    }

    public void openInventory() {
        human.openInventory(getInventory());
    }

    private void drawExit(EnchGuiBuilder builder) {
        builder.view.set(8, 0, exit);
    }
    private void drawSection(IEnchGuiSection section) {
        {
            final var builder = new EnchGuiBuilder(section.getView(), false);
            builder.drawBackground();
            drawExit(builder);
            section.draw(builder);
            drawInput(builder);
        }
        {
            final var builder = new EnchGuiBuilder(section.getDynView(), true);
            builder.drawBackground();
            drawExit(builder);
            section.draw(builder);
            drawInput(builder);
            drawOutput(builder);
        }
    }

    @Override
    public void close(InventoryCloseEvent e) {
        clearInput(e.getPlayer(), e.getView().getBottomInventory());
    }

    private void clearInput(HumanEntity human, Inventory inv) {
        final var item = input.readItem();
        if(item == null || item.getType() == Material.AIR) return;
        input.setItem(null);
        final var idx = inv.firstEmpty();
        if(idx == -1) {
            final var loc = human.getEyeLocation();
            final var dir = human.getLocation().getDirection().normalize();
            loc.getWorld().dropItem(loc, item, it -> {
                it.setVelocity(dir.multiply(0.2));
            });
        } else {
            inv.setItem(idx, item);
        }
    }

    public @Nullable IEnchGuiSection getSection(ItemStack item) {
        for (IEnchGuiSection section : sections) {
            if (section.isAllowed(item)) return section;
        }
        return null;
    }

    protected boolean inputChanged(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            setView(main);
            return true;
        }
        final var section = getSection(item);
        if(section == null) return false;
        setView(section.getDynView());
        output.setItem(new ItemStack(Material.AIR));
        return true;
    }

    private void drawInput(EnchGuiBuilder builder) {
        builder.view.set(1, 1, this.input);
    }
    private void drawOutput(EnchGuiBuilder builder) {
        builder.view.set(3, 1, this.output);
    }
    private void drawMain(EnchGuiBuilder builder) {
        for (SfGuiElement el : sectionEls) {
            builder.addCategory(el);
        }
    }

    public void setOutput(ItemStack item) {
        output.setItem(item);
    }

    public ItemStack getInput() {
        return input.readItem();
    }

}
