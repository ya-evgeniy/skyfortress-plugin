package ru.jekarus.skyfortress.v3.api.event;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import ru.jekarus.skyfortress.v3.team.SfTeam;

public interface TeamRegisterEvent extends Event {

    SfTeam getTeam();

    interface Pre extends TeamRegisterEvent, Cancellable {

    }

    interface Post extends TeamRegisterEvent {

    }

}
