package ru.jekarus.skyfortress.module.gui.enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.jekarus.skyfortress.module.gui.ChestGuiView;
import ru.jekarus.skyfortress.module.gui.SfGuiElement;

import java.util.function.Consumer;

public class EnchGuiBuilder {

    public final ChestGuiView view;
    private final boolean byeMode;
    private int count = 0;

    public EnchGuiBuilder(ChestGuiView view, boolean byeMode) {
        this.view = view;
        this.byeMode = byeMode;
    }

    protected void drawBackground() {
        if(byeMode) {
            drawByeBg();
        } else {
            drawOverviewBg();
        }
    }
    protected void drawOverviewBg() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if(x == 1 && y == 1) continue;
                view.set(x, y, new SfGuiElement(
                        Material.WHITE_STAINED_GLASS_PANE,
                        " "
                ));
            }
        }
        for (int x = 3; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                view.set(x, y, new SfGuiElement(
                        Material.GRAY_STAINED_GLASS_PANE,
                        " "
                ));
            }
        }
    }
    private void drawByeBg() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                if((x >= 1 && x <= 3) && y == 1) continue;
                view.set(x, y, new SfGuiElement(
                        Material.WHITE_STAINED_GLASS_PANE,
                        " "
                ));
            }
        }
        for (int x = 5; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                view.set(x, y, new SfGuiElement(
                        Material.GRAY_STAINED_GLASS_PANE,
                        " "
                ));
            }
        }

        view.set(2, 1, new SfGuiElement(
                Material.STRUCTURE_VOID,
                ChatColor.GRAY + "Цена"
        ));
    }

    public void drawOutput(SfGuiElement el) {
        view.set(3, 1, el);
    }

    protected SfGuiElement addEnch(SfGuiElement el) {
        int dx = count % 4;
        int dy = count / 4;
        count++;
        int x = byeMode ? 5 : 4;
        view.set(x + dx, 1 + dy, el);
        return el;
    }

    protected SfGuiElement setCategory(SfGuiElement el) {
        if(!byeMode) view.set(4, 0, el);
        return el;
    }

    protected SfGuiElement addCategory(SfGuiElement el) {
        int dx = count % 6;
        int dy = count / 6;
        count++;
        view.set(3 + dx, 1 + dy, el);
        return el;
    }

}
