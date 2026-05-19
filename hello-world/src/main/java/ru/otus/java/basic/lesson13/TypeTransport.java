package ru.otus.java.basic.lesson13;

public enum TypeTransport {
    BikeTp("Велосипед"), CarTp("Машина"), CrossVehicleTp("Вездеход"), HorseTp("Лошадь"),
    WalkTp("Пеший ход");
    private String name;

    TypeTransport(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }
}
