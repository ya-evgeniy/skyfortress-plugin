package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.team.SfTeam;

public class LanguageValueVariables {

    private final LanguageVariables variables;
    private final SfLanguage language;
    private final String key;

    private TextColor color;

    public LanguageValueVariables(LanguageVariables variables, SfLanguage language, String key) {
        this.variables = variables;
        this.language = language;
        this.key = key;
    }

    public LanguageValueVariables color(SfTeam team) {
        return color(team.getColor());
    }

    public LanguageValueVariables color(TextColor color) {
        this.color = color;
        return this;
    }

    public LanguageVariables name(SfTeam team) {
        return this.name(team, true);
    }

    public LanguageVariables name(SfTeam team, boolean useColor) {
        SfTeamLanguage teamLanguage = this.language.teams.get(team);
        if (teamLanguage == null) {
            return this.variables;
        }
        int index = -1;
        String clonedKey = this.key.isEmpty() ? this.key : this.key + ".";
        for (String name : teamLanguage.names) {
            String key = clonedKey + "name." + ++index;
            if (useColor) {
                this.variables.appendCustom(key, Text.builder(name).color(team.getColor()).build());
            } else {
                this.variables.appendCustom(key, Text.of(name));
            }
        }
        return this.variables;
    }

    public LanguageVariables name(PlayerData player) {
        return this.string(player.getName());
    }

    public LanguageVariables name(Player player) {
        return this.string(player.getName());
    }

    public LanguageVariables string(String string) {
        Text text = appendColor(Text.builder(string)).build();
        return this.text(text);
    }

    public LanguageVariables text(Text text) {
        this.variables.appendCustom(this.key, text);
        return this.variables;
    }

    public LanguageVariables number(int number) {
        String content = String.valueOf(number);
        return this.string(content);
    }

    private Text.Builder appendColor(Text.Builder builder) {
        if (color != null) builder.color(this.color);
        return builder;
    }
}
