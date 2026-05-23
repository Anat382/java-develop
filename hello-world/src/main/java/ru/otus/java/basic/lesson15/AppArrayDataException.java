package ru.otus.java.basic.lesson15;

public class AppArrayDataException extends RuntimeException {
    private String typeData;

    public String getTypeData() {
        return typeData;
    }

    public AppArrayDataException(String message, String typeData) {
        super(message + typeData);
        this.typeData = typeData;
    }
}
