package ru.jekarus.skyfortress.v3.gui;

import org.spongepowered.api.text.Text;
import ru.jekarus.jgui.gui.ChestGui;
import ru.jekarus.jgui.gui.content.GuiContent;

public class ShopGui extends ChestGui {

//    public static ShopGui INSTANCE = null;//new ShopGui("shop", Text.of("Магазин"), 3);

    public ShopGui(String uniqueId, Text title, int rows)
    {
        super(uniqueId, title, rows);
        this.setContent(new GuiContent(this.cols * this.rows));
        this.content.setGui(this);
//        this.initSlots();
    }

//    private void initSlots()
//    {
//
//        this.setSlot(1, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1),
//                    ItemStack.of(ItemTypes.IRON_INGOT, 8)
//            ))
//        );
//
//        this.setSlot(2, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.IRON_INGOT, 8),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(3, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.IRON_INGOT, 1),
//                    ItemStack.of(ItemTypes.COBBLESTONE, 10)
//            ))
//        );
//
//        this.setSlot(4, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.IRON_INGOT, 1),
//                    ItemStack.of(ItemTypes.PLANKS, 10)
//            ))
//        );
//
//        this.setSlot(5, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.BOW, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 8)
//            ))
//        );
//
//        this.setSlot(6, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.DIAMOND_AXE, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 8)
//            ))
//        );
//
//        this.setSlot(7, 0,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.ENCHANTING_TABLE, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 32)
//            ))
//        );
//
//        this.setSlot(1, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.SHEARS, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 2)
//            ))
//        );
//
//        this.setSlot(2, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.WOOL, 16),
//                    ItemStack.of(ItemTypes.IRON_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(3, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.CONCRETE, 32),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(4, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.COOKED_FISH, 8),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(5, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.ARROW, 8),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(6, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.DIAMOND_SWORD, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 8)
//            ))
//        );
//
//        this.setSlot(7, 1,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.BOOKSHELF, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(1, 2,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.FLINT_AND_STEEL, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 3)
//            ))
//        );
//
//        this.setSlot(2, 2,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.TNT, 2),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(4, 2,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.ENDER_PEARL, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 8)
//            ))
//        );
//
//        this.setSlot(5, 2,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.SPECTRAL_ARROW, 8),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//            ))
//        );
//
//        this.setSlot(6, 2,
//            new ShopSlot(new ShopItem(
//                    ItemStack.of(ItemTypes.TOTEM_OF_UNDYING, 1),
//                    ItemStack.of(ItemTypes.GOLD_INGOT, 16)
//            ))
//        );
//
//        this.setSlot(7, 2,
//                new ShopSlot(new ShopItem(
//                        ItemStack.of(ItemTypes.EXPERIENCE_BOTTLE, 16),
//                        ItemStack.of(ItemTypes.GOLD_INGOT, 1)
//                ))
//        );
//
//    }

}
