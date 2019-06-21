package ru.jekarus.skyfortress.v3.api.event;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.team.SfTeam;

public interface CastleRegisterEvent extends Event {

    SfCastle getTeam();

    interface Pre extends CastleRegisterEvent, Cancellable {

    }

    interface Post extends CastleRegisterEvent {

    }

}
