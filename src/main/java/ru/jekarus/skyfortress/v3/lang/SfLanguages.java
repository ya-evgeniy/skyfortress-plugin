package ru.jekarus.skyfortress.v3.lang;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SfLanguages {

    private Map<Locale, SfLanguage> languages = new HashMap<>();
    private Locale def;

    public SfLanguages()
    {

    }

    public boolean has(Locale locale) {
        return this.languages.containsKey(locale);
    }

    public void add(SfLanguage language)
    {
        this.languages.put(language.locale, language);
    }

    public void remove(Locale locale)
    {
        this.languages.remove(locale);
    }

    public SfLanguage get(Locale locale)
    {
        return this.languages.get(locale);
    }

    public Locale getDef()
    {
        return this.def;
    }

    public void setDef(Locale def)
    {
        this.def = def;
    }

    public Map<Locale, SfLanguage> getMap()
    {
        return this.languages;
    }
}
