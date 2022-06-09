package ru.jekarus.skyfortress.module.gui.enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.jekarus.skyfortress.config.SfConfig;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;
import ru.jekarus.skyfortress.module.gui.SfGuiElement;
import ru.jekarus.skyfortress.state.SkyFortress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrossbowEnchantmentSec implements IEnchGuiSection {

    private final ChestGuiView view = new ChestGuiView(3);
    private final ChestGuiView dynView = new ChestGuiView(3);
    private final SfGuiElement category;
    private final List<SfGuiElement> enchs = new ArrayList<>();

    public CrossbowEnchantmentSec(EnchantmentsGui gui) {
        category = new SfGuiElement(
                Material.CROSSBOW,
                "Чары для арбалетов"
        );
        for (SfEnchantment ench : SfConfig.ENCHANTMENT_CROSSBOW) {
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
        return item.getType() == Material.CROSSBOW;
    }

}
