package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

import java.util.HashMap;
import java.util.Map;

public class LanguageVariables {

    private Map<String, Text> variables = new HashMap<>();

    private final SfLanguage language;

    public LanguageVariables(SfLanguage language) {
        this.language = language;
    }

    public LanguageValueVariables key(String key) {
        return new LanguageValueVariables(this, language, key);
    }

    public LanguageValueVariables playerKey(String prefix) {
        return this.key(prefix + ".player.name");
    }

    public LanguageValueVariables playerKey() {
        return this.key("player.name");
    }

    public LanguageValueVariables teamKey(String prefix) {
        return this.key(prefix + ".team");
    }

    public LanguageValueVariables teamKey() {
        return this.key("team");
    }

    public LanguageVariables appendCustom(String key, Text value) {
        this.variables.put(key, value);
        return this;
    }

    public Text apply(TextTemplate template) {
        return template.apply(this.variables).build();
    }

}
