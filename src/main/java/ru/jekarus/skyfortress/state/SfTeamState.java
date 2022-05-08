package ru.jekarus.skyfortress.state;

public class SfTeamState {

    public int health = 100;

    public double experience = 0.0;

    public int getLevel() {
        final var level = (int) (32 + Math.pow(2, 14) / -(experience + 380) + 11.5);
        return Math.min(32, level);
    }

}
