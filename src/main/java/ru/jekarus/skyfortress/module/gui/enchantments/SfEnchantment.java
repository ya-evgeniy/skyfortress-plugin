package ru.jekarus.skyfortress.module.gui.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class SfEnchantment {

    public static class Option {

        public final int price;
        public final int requiredLevel;
        public final int enchLevel;

        public Option(int price, int requiredLevel, int enchLevel) {
            this.price = price;
            this.requiredLevel = requiredLevel;
            this.enchLevel = enchLevel;
        }

        public String enchLvl() {
            switch (enchLevel) {
                case 0: return "I";
                case 1: return "II";
                case 2: return "III";
                case 3: return "IV";
                case 4: return "V";
                case 5: return "VI";
                case 6: return "VII";
                case 7: return "VIII";
                case 8: return "IX";
                case 9: return "X";
            }
            return "" + enchLevel;
        }
    }

    public final String name;
    public final Enchantment ench;
    private final Enchantment[] incompatible;
    private final Material[] whitelist;
    public final List<Option> options = new ArrayList<>();

    public SfEnchantment(String name, Enchantment ench, int[] mapping, Enchantment[] incompatible, Material[] whitelist) {
        this.name = name;
        this.ench = ench;
        this.incompatible = incompatible;
        this.whitelist = whitelist;
        assert mapping.length % 2 == 0;
        for (int i = 0; i < mapping.length / 2; i++) {
            int price = mapping[i * 2];
            int requiredLevel = mapping[i * 2 + 1];
            if(price == 0) continue;
            options.add(new Option(price, requiredLevel, i));
        }
    }
    public SfEnchantment(String name, Enchantment ench, int[] mapping, Enchantment[] incompatible) {
        this(name, ench, mapping, incompatible, new Material[0]);
    }
    public SfEnchantment(String name, Enchantment ench, int[] mapping, Material[] whitelist) {
        this(name, ench, mapping, new Enchantment[0], whitelist);
    }
    public SfEnchantment(String name, Enchantment ench, int[] mapping) {
        this(name, ench, mapping, new Enchantment[0], new Material[0]);
    }


}
