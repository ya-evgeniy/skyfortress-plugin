package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class SfMapLanguage {

    public Text name = Text.of("Sky Fortress").toBuilder().color(TextColors.GOLD).build();
    public List<Text> creators = Arrays.asList(
            Text.of("JekaRUS").toBuilder().color(TextColors.GRAY).build(),
            Text.of("PikaviT").toBuilder().color(TextColors.GRAY).build()
    );

}
