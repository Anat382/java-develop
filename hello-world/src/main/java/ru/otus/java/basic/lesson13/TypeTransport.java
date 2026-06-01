package ru.otus.java.basic.lesson13;

public enum TypeTransport {
    bike("Велосипед"), car("Машина"), crossVehicle("Вездеход"), horse("Лошадь"), none(null);
    private String name;

    TypeTransport(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }
}
