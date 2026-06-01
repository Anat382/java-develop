package ru.otus.java.basic.lesson13;

public interface Transport {
    String getName();

    int getDistance();

    String getTerrain();

    boolean equalsTerrain();

    boolean drive();
}
