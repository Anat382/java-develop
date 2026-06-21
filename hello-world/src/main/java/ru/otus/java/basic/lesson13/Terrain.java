package ru.otus.java.basic.lesson13;

public enum Terrain {
    denseForest("Густой лес"), plain("Равнина"), swamp("Болото");
    private String name;

    Terrain(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}