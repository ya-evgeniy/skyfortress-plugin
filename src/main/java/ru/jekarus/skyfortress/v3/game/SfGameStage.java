package ru.jekarus.skyfortress.v3.game;

public abstract class SfGameStage {

    public abstract void enable();

    public abstract void disable();

    public abstract SfGameStageType getType();

}
