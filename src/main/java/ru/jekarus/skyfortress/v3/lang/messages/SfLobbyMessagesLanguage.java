package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfLobbyMessagesLanguage {

    public TextTemplate cantJoin = parse("Вход в команды отключен!");
    public TextTemplate cantLeave = parse("Выход из команды отключен!");
    public TextTemplate cantReady = parse("Готовность команды отключена");
    public TextTemplate cantUnready = parse("Не готовность команды отключена");
    public TextTemplate cantAccept = parse("Принятие игроков отлючено!");
    public TextTemplate cantDeny = parse("Отклонение игроков отключено!");

    public TextTemplate playerWaitAccepted = parse("Подожди пока тебя примут в команду");
    public TextTemplate playerWaitAcceptedByCaptain = parse("Подожди пока капитан команды {player.name} примет тебя в команду");
    public TextTemplate playerJoined = parse("Ты вошел в команду {team.name.1}");
    public TextTemplate playerLeaved = parse("Ты вышел из команды {team.name.1}");
    public TextTemplate playerAcceptedBy = parse("Тебя принял {player.name}");
    public TextTemplate playerDeniedBy = parse("Тебя отклонил {player.name}");

    public TextTemplate teammateWaitAccepted = parse("{player.name} пытается зайти в твою команду");
    public TextTemplate teammateJoined = parse("{player.name} присоединился к твоей команде");
    public TextTemplate teammateLeaved = parse("{player.name} вышел из твоей команды");
    public TextTemplate teammateYouAccepted = parse("Ты принял {target.player.name}");
    public TextTemplate teammateAcceptedBy = parse("{player.name} принял {target.player.name}");
    public TextTemplate teammateYouDenied = parse("Ты отклонил {target.player.name}");
    public TextTemplate teammateDeniedBy = parse("{player.name} отклонил {target.player.name}");

    public TextTemplate teammates_captain_wait_accepted = parse("{player.name} пытается зайти в твою команду");
    public TextTemplate teammates_captain_accepted_by = parse("Капитан команды {player.name} принял {target.player.name}");
    public TextTemplate teammates_captain_denied_by = parse("Капитан команды {player.name} отклонил {target.player.name}");
    public TextTemplate teammatesCaptainYouAreNew = parse("Поздравляем! Теперь ты капитан {team.name.2} команды!");
    public TextTemplate teammatesCaptainNew = parse("Поздравте {player.name}! Теперь он капитан команды");
    public TextTemplate teammates_captain_you_replaced = parse("Капитан команды {player.name} ушел в отставку. Ты заменил его");
    public TextTemplate teammatesCaptainReplaced = parse("Капитан команды {player.name} ушел в отставку. {captain.name} заменил его");
    public TextTemplate teammatesCaptainLeaved = parse("Капитан команды {player.name} покинул твою команду. Новым капитаном стал {captain.player.name}!");
    public TextTemplate teammatesCaptainLeavedYouNew = parse("Капитан команды {player.name} покинул твою команду. Ты новый капитан команды!");

    public TextTemplate teammatesCaptainCantReady = parse("Ты не капитан команды, чтобы установить готовность команды");
    public TextTemplate teammatesCaptainCantUnready = parse("Ты не капитан команды, чтобы установить не готовность команды");
    public TextTemplate teammatesCaptainCantAccept = parse("Ты не капитан команды, чтобы принимать игроков");
    public TextTemplate teammatesCaptainCantDeny = parse("Ты не капитан команды, чтобы отклонять игроков");

    public TextTemplate commandPlayerChangeSelfTeam = parse("Ты вступил в команду {team.name_1}", TextColors.GRAY);
    public TextTemplate commandPlayerChangePlayerTeam = parse("{player.name} добавлен в команду {team.name.1}", TextColors.GRAY);
    public TextTemplate commandPlayerYouAlreadyInTeam = parse("Ты уже в этой команде", TextColors.GRAY);
    public TextTemplate commandPlayerAlreadyInTeam = parse("{player.name} уже в этой команде", TextColors.GRAY);

    public TextTemplate commandTargetSetTeam = parse("{player.name} присоеденил тебя в команду {team.name.1}", TextColors.GRAY);

    public TextTemplate commandGlobalSetSelfTeam = parse("{player.name} установил себе команду {team.name.1}", TextColors.GRAY);
    public TextTemplate commandGlobalSetPlayerTeam = parse("{player.name} установил {target.player.name} команду {team.name.1}", TextColors.GRAY);

//    public TextTemplate command_captain = parse("", TextColors.GRAY);

}
