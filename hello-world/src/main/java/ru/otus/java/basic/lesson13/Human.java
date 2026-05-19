package ru.otus.java.basic.lesson13;

public class Human {
    private String name;
    private String currentTransport = null;

    public Human(String name) {
        this.name = name;
    }

    public void getInfo() {
        System.out.println("Имя человека " + name + ", выбранный транспорт " + currentTransport);
    }

    public void setCurrentTransport(String transport) {
        currentTransport = transport;
    }

    public String getCurrentTransport() {
        return currentTransport;
    }

    public String getName() {
        return name;
    }

    public boolean setDouwn() {
        if (currentTransport != null) {
            System.out.println(name + ", сел в транспорт " + currentTransport);
            return true;
        }
        return false;
    }

    public boolean standUpp() {
        if (currentTransport != null) {
            System.out.println(name + ", вышел из транспорта " + currentTransport);
            currentTransport = null;
            return true;
        }
        return false;
    }


//    boolean set();
//    boolean standUpp();
//    boolean driver(int distance);
//    boolean getName();
//    boolean walking(int distance);
}
