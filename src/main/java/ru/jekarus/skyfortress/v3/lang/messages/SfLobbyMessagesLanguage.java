package ru.jekarus.skyfortress.v3.lang.messages;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfLobbyMessagesLanguage {

    @ConfigPath("cant.join") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantJoin = parse("Вход в команды отключен!");

    @ConfigPath("cant.leave") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantLeave = parse("Выход из команды отключен!");

    @ConfigPath("cant.ready") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantReady = parse("Готовность команды отключена");

    @ConfigPath("cant.unready") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantUnready = parse("Не готовность команды отключена");

    @ConfigPath("cant.accept") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantAccept = parse("Принятие игроков отлючено!");

    @ConfigPath("cant.deny") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantDeny = parse("Отклонение игроков отключено!");

    @ConfigPath("cant.join_when_distribution") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate cantJoinWhenDistribution = parse("Вход в команды отключен, так как идет распределение игроков!");


    @ConfigPath("player.wait_accepted") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerWaitAccepted = parse("Подожди пока тебя примут в команду");

    @ConfigPath("player.wait_accepted_by_captain") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerWaitAcceptedByCaptain = parse("Подожди пока капитан команды {player.name} примет тебя в команду");

    @ConfigPath("player.joined") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerJoined = parse("Ты вошел в команду {team.name.1}");

    @ConfigPath("player.leaved") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerLeaved = parse("Ты вышел из команды {team.name.1}");

    @ConfigPath("player.accepted_by") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerAcceptedBy = parse("Тебя принял {player.name}");

    @ConfigPath("player.denied_by") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate playerDeniedBy = parse("Тебя отклонил {player.name}");


    @ConfigPath("teammate.wait_accepted") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateWaitAccepted = parse("{player.name} ожидает принятия в команду");

    @ConfigPath("teammate.joined") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateJoined = parse("{player.name} присоединился к команде");

    @ConfigPath("teammate.leaved") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateLeaved = parse("{player.name} покинул команду");

    @ConfigPath("teammate.you_accepted") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateYouAccepted = parse("Ты принял {target.player.name} в команду");

    @ConfigPath("teammate.accepted_by") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateAcceptedBy = parse("{player.name} принял {target.player.name} в команду");

    @ConfigPath("teammate.you_denied") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateYouDenied = parse("Ты отклонил {target.player.name}");

    @ConfigPath("teammate.denied_by") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateDeniedBy = parse("{player.name} отклонил {target.player.name}");


//    @ConfigPath("teammate.captain.you_are_new") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
//    public TextTemplate teammateCaptainYouAreNew = parse("{player.name} пытается зайти в твою команду");
//
//    @ConfigPath("teammate.captain.new_captain") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
//    public TextTemplate teammates_captain_accepted_by = parse("Капитан команды {player.name} принял {target.player.name}");
//
//    @ConfigPath("teammate.captain.you_replaced") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
//    public TextTemplate teammates_captain_denied_by = parse("Капитан команды {player.name} отклонил {target.player.name}");

    @ConfigPath("teammate.captain.you_are_new") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainYouAreNew = parse("Поздравляем! Теперь ты капитан {team.name.2} команды!");

    @ConfigPath("teammate.captain.new_captain") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainNew = parse("Поздравте {player.name}! Теперь он капитан команды");

    @ConfigPath("teammate.captain.you_replaced") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainYouReplaced = parse("Капитан команды {player.name} ушел в отставку. Ты заменил его");

    @ConfigPath("teammate.captain.replaced") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainReplaced = parse("Капитан команды {player.name} ушел в отставку. {captain.name} заменил его");

    @ConfigPath("teammate.captain.leaved_you_new") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammatesCaptainLeavedYouNew = parse("Капитан команды {player.name} покинул твою команду. Ты новый капитан команды!");

    @ConfigPath("teammate.captain.leaved") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainLeaved = parse("Капитан команды {player.name} покинул твою команду. Новым капитаном стал {captain.player.name}!");


    @ConfigPath("teammate.captain.cant.ready") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainCantReady = parse("Ты не капитан команды, чтобы установить готовность команды");

    @ConfigPath("teammate.captain.cant.unready") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainCantUnready = parse("Ты не капитан команды, чтобы установить не готовность команды");

    @ConfigPath("teammate.captain.cant.accept") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainCantAccept = parse("Ты не капитан команды, чтобы принимать игроков");

    @ConfigPath("teammate.captain.cant.deny") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate teammateCaptainCantDeny = parse("Ты не капитан команды, чтобы отклонять игроков");


    @ConfigPath("command.player.change_self_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandPlayerChangeSelfTeam = parse("Ты вступил в команду {team.name_1}");

    @ConfigPath("command.player.change_player_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandPlayerChangePlayerTeam = parse("{player.name} добавлен в команду {team.name.1}");

    @ConfigPath("command.player.you_already_in_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandPlayerYouAlreadyInTeam = parse("Ты уже в этой команде");

    @ConfigPath("command.player.already_in_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandPlayerAlreadyInTeam = parse("{player.name} уже в этой команде");


    @ConfigPath("command.target.set_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandTargetSetTeam = parse("{player.name} присоеденил тебя в команду {team.name.1}");


    @ConfigPath("command.global.set_self_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandGlobalSetSelfTeam = parse("{player.name} установил себе команду {team.name.1}");

    @ConfigPath("command.global.set_player_team") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandGlobalSetPlayerTeam = parse("{player.name} установил {target.player.name} команду {team.name.1}");

//    public TextTemplate command_captain = parse("", TextColors.GRAY);

}
