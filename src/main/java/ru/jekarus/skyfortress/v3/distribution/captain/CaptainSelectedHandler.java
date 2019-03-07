package ru.jekarus.skyfortress.v3.distribution.captain;

public interface CaptainSelectedHandler {

    void markTarget(ChoosingCaptain captain, CaptainTarget target);

    void unmarkTarget(ChoosingCaptain captain, CaptainTarget target);

    void selected(ChoosingCaptain captain, CaptainTarget target, boolean isRandom);

}
