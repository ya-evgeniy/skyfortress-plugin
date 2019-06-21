package ru.jekarus.skyfortress.v3.gui;

import lombok.val;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Shops {

    private Map<Locale, ShopGui> shopByLocale = new HashMap<>();

    public ShopGui getShop(Locale locale) {
        final val result = this.shopByLocale.get(locale);
        if (result != null) {
            return result;
        }
        for (Map.Entry<Locale, ShopGui> entry : shopByLocale.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    public void add(Locale locale, ShopGui shop) {
        this.shopByLocale.put(locale, shop);
    }
}
