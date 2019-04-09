package ru.jekarus.skyfortress.v3.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SfLanguageCommand extends SfCommand {

    @Override
    public CommandSpec create(SkyFortressPlugin plugin)
    {
        Map<String, Locale> langs = new HashMap<>();
        for (Locale locale : plugin.getLanguages().getLanguageByLocale().keySet())
        {
            langs.put(String.format("%s_%s", locale.getLanguage(), locale.getCountry()), locale);
        }


        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("locale"), langs)
                )
                .executor((src, args) ->
                {

                    if (!(src instanceof Player))
                    {
                        return CommandResult.empty();
                    }

                    Player player = (Player) src;

                    Locale locale = args.<Locale>getOne("locale").orElse(null);

                    if (locale == null)
                    {
                        src.sendMessage(Text.of("Язык не найден"));
                        return CommandResult.empty();
                    }

                    SfLanguage language = plugin.getLanguages().getLanguageByLocale().get(locale);

                    if (language == null)
                    {
                        src.sendMessage(Text.of("Карта не переведена на этот язык"));
                        return CommandResult.empty();
                    }

                    SfPlayer sfPlayer = SfPlayers.getInstance().getOrCreatePlayer(player);
                    sfPlayer.setLocale(locale);

                    plugin.getScoreboards().setFor(sfPlayer, player);

                    return CommandResult.success();

                })
                .build();
    }

}
