package ru.jekarus.skyfortress.v3.distribution;

public interface Distribution {

    Type getType();

    State getState();

    void serverStopping();

    enum Type {

        CAPTAIN

    }

    enum State {

        STARTUP,
        LOAD_CONFIG,
        ERROR_CONFIG,
        DISTRIBUTION,
        ENDED

    }

}
