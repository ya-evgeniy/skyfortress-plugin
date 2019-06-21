package ru.jekarus.skyfortress.v3.serializer.itemstack;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import jekarus.hocon.config.serializer.converter.SpongeConverters;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import java.util.List;

public class ItemStackView {

    @MethodConverter(inClass = SpongeConverters.class, method = "itemType")
    public ItemType type;

    @OptionalValue
    public int quantity = 1;

    @OptionalValue
    public int durability = -1;

    @OptionalValue
    public Meta meta;

    public static class Meta {

        @OptionalValue @ConfigPath("display_name")
        public Text displayName;

        @OptionalValue @Generics(Text.class)
        public List<Text> lore;
        
    }

}
