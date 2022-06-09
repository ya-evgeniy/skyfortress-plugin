package ru.jekarus.skyfortress.module.gui.enchantments;

import org.bukkit.inventory.ItemStack;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;

public interface IEnchGuiSection {

    boolean isAllowed(ItemStack item);
    void draw(EnchGuiBuilder builder);

    ChestGuiView getView();

    ChestGuiView getDynView();

}
