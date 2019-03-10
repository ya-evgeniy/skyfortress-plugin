package ru.jekarus.skyfortress.v3.lang;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SfLanguages {

    @Getter private Map<Locale, SfLanguage> languageByLocale = new HashMap<>();
    @Getter @Setter private Locale def;

    public SfLanguages() {

    }

    public boolean has(Locale locale) {
        return this.languageByLocale.containsKey(locale);
    }

    public void add(SfLanguage language) {
        this.languageByLocale.put(language.locale, language);
    }

    public void remove(Locale locale) {
        this.languageByLocale.remove(locale);
    }

    public SfLanguage get(Locale locale) {
        return this.languageByLocale.get(locale);
    }

}
