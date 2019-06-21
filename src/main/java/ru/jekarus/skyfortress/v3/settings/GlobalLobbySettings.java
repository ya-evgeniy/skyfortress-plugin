package ru.jekarus.skyfortress.v3.settings;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.Getter;
import lombok.Setter;

@ConfigPath("lobby")
public class GlobalLobbySettings {

    @OptionalValue @ConfigPath("can_join_team")
    @Getter @Setter private boolean canJoinTeam = true;

    @OptionalValue @ConfigPath("can_leave_team")
    @Getter @Setter private boolean canLeaveTeam = true;

    @OptionalValue @ConfigPath("can_set_ready")
    @Getter @Setter private boolean canSetReady = true;

    @OptionalValue @ConfigPath("can_set_unready")
    @Getter @Setter private boolean canSetUnready = true;

    @OptionalValue @ConfigPath("can_use_accept_button")
    @Getter @Setter private boolean canUseAcceptButton = true;

    @OptionalValue @ConfigPath("can_use_deny_button")
    @Getter @Setter private boolean canUseDenyButton = true;

    @OptionalValue @ConfigPath("use_lobby_captain_system")
    @Getter @Setter private boolean useLobbyCaptainSystem = true;

}