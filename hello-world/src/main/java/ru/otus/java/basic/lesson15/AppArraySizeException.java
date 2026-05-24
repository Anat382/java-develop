package ru.otus.java.basic.lesson15;

public class AppArraySizeException extends RuntimeException {
    private String size;

    public String getSize() {
        return size;
    }

    public AppArraySizeException(String message, String size) {
        super(message + size);
        this.size = size;
    }

}
