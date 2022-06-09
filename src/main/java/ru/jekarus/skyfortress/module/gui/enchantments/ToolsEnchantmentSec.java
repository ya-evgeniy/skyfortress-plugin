package ru.jekarus.skyfortress.module.gui.enchantments;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;
import ru.jekarus.skyfortress.module.gui.SfGuiElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ToolsEnchantmentSec implements IEnchGuiSection {

    private final ChestGuiView view = new ChestGuiView(3);
    private final ChestGuiView dynView = new ChestGuiView(3);
    private final SfGuiElement category;
    private final List<SfGuiElement> enchs = new ArrayList<>();

    public ToolsEnchantmentSec(EnchantmentsGui gui) {
        category = new SfGuiElement(
                Material.GOLDEN_PICKAXE,
                is -> {
                    is.editMeta(im -> {
                        im.setDisplayName("Чары для инструментов");
                    });
                    is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
        );
        for (SfEnchantment ench : SfConfig.ENCHANTMENT_TOOLS) {
            final var lore = new ArrayList<String>();
            for (SfEnchantment.Option option : ench.options) {
                lore.add(option.price + " эм. | " + option.requiredLevel + " ур. | " + option.enchLvl());
            }
            enchs.add(new SfGuiElement(
                    Material.ENCHANTED_BOOK,
                    ChatColor.GRAY + ench.name, lore
            ).setOnClick(e -> {
                if (e.getClick() == ClickType.LEFT) {
                    final var item = gui.getInput();
                    item.addEnchantment(ench.ench, 1);
                    gui.setOutput(item);
                }
            }));
        }
    }

    @Override public ChestGuiView getView() { return view; }
    @Override public ChestGuiView getDynView() { return dynView; }

    @Override
    public void draw(EnchGuiBuilder builder) {
        builder.setCategory(category);
        for (SfGuiElement ench : enchs) builder.addEnch(ench);
    }

    @Override
    public boolean isAllowed(ItemStack item) {
        return Arrays.asList(
                Material.WOODEN_PICKAXE,
                Material.STONE_PICKAXE,
                Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_PICKAXE,

                Material.WOODEN_AXE,
                Material.STONE_AXE,
                Material.IRON_AXE,
                Material.GOLDEN_AXE,
                Material.DIAMOND_AXE,

                Material.WOODEN_SHOVEL,
                Material.STONE_SHOVEL,
                Material.IRON_SHOVEL,
                Material.GOLDEN_SHOVEL,
                Material.DIAMOND_SHOVEL
        ).contains(item.getType());
    }

}
