package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfLobbyMessagesLanguage {

    public TextTemplate player_join = SfTextParser.parse("Подожди пока тебя примут в команду", TextColors.GRAY);
    public TextTemplate player_joined = SfTextParser.parse("Ты вошел в команду {team.name.1}", TextColors.GRAY);
    public TextTemplate player_leave = SfTextParser.parse("Ты вышел из команды {team.name.1}", TextColors.GRAY);
    public TextTemplate player_accept = SfTextParser.parse("Тебя принял {player.name}", TextColors.GRAY);
    public TextTemplate player_deny = SfTextParser.parse("Тебя отклонил {player.name}", TextColors.GRAY);
    public TextTemplate player_cant_join = SfTextParser.parse("Вход в команды отключен!", TextColors.GRAY);
    public TextTemplate player_cant_leave = SfTextParser.parse("Выход из команды отключен", TextColors.GRAY);

    public TextTemplate teammate_join = SfTextParser.parse("{player.name} пытается зайти в твою команду", TextColors.GRAY);
    public TextTemplate teammate_joined = SfTextParser.parse("{player.name} присоеденился к твоей команде", TextColors.GRAY);
    public TextTemplate teammate_leave = SfTextParser.parse("{player.name} вышел из твоей команды", TextColors.GRAY);
    public TextTemplate teammate_accept = SfTextParser.parse("{player.name} принял {target.player.name}", TextColors.GRAY);
    public TextTemplate teammate_deny = SfTextParser.parse("{player.name} отклонил {target.player.name}", TextColors.GRAY);

    public TextTemplate command_player_set_self_team = SfTextParser.parse("Ты вступил в команду {team.name_1}", TextColors.GRAY);
    public TextTemplate command_player_set_player_team = SfTextParser.parse("{player.name} добавлен в команду {team.name.1}", TextColors.GRAY);
    public TextTemplate command_player_you_already_in_team = SfTextParser.parse("Ты уже в этой команде", TextColors.GRAY);
    public TextTemplate command_player_target_already_in_team = SfTextParser.parse("{player.name} уже в этой команде", TextColors.GRAY);

    public TextTemplate command_target_set_team = SfTextParser.parse("{player.name} присоеденил тебя в команду {team.name.1}", TextColors.GRAY);

    public TextTemplate command_global_set_self_team = SfTextParser.parse("{player.name} установил себе команду {team.name.1}", TextColors.GRAY);
    public TextTemplate command_global_set_player_team = SfTextParser.parse("{player.name} установил {target.player.name} команду {team.name.1}", TextColors.GRAY);

}
