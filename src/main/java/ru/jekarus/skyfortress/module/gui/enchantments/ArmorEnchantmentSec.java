package ru.jekarus.skyfortress.module.gui.enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;
import ru.jekarus.skyfortress.module.gui.SfGuiElement;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorEnchantmentSec implements IEnchGuiSection {

    private final ChestGuiView view = new ChestGuiView(3);
    private final ChestGuiView dynView = new ChestGuiView(3);
    private final SfGuiElement category;
    private final List<SfGuiElement> enchs = new ArrayList<>();

    public ArmorEnchantmentSec(EnchantmentsGui gui) {
        category = new SfGuiElement(
                Material.GOLDEN_CHESTPLATE,
                is -> {
                    is.editMeta(im -> {
                        im.setDisplayName("Чары для брони");
                    });
                    is.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
        );
        for (SfEnchantment ench : SfConfig.ENCHANTMENT_ARMOR) {
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
                    item.addEnchantment(ench.ench, 2);
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
                Material.CHAINMAIL_HELMET,
                Material.IRON_HELMET,
                Material.GOLDEN_HELMET,
                Material.DIAMOND_HELMET,

                Material.CHAINMAIL_CHESTPLATE,
                Material.IRON_CHESTPLATE,
                Material.GOLDEN_CHESTPLATE,
                Material.DIAMOND_CHESTPLATE,

                Material.CHAINMAIL_LEGGINGS,
                Material.IRON_LEGGINGS,
                Material.GOLDEN_LEGGINGS,
                Material.DIAMOND_LEGGINGS,

                Material.CHAINMAIL_BOOTS,
                Material.IRON_BOOTS,
                Material.GOLDEN_BOOTS,
                Material.DIAMOND_BOOTS
        ).contains(item.getType());
    }

}