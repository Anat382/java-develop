package ru.otus.java.basic.lesson13;

public class Human implements CurrentTransport {
    private String name;
    private TypeTransport currentTransport = null;
    private int endurance;

    public Human(String name, TypeTransport currentTransport, int endurance) {
        this.name = name;
        this.currentTransport = currentTransport;
        this.endurance = endurance;
    }

    public String getCurrentTransport() {
        return currentTransport.getType();
    }

    public void getInfo() {
        System.out.println("Имя человека " + name + ", выбранный транспорт " + getCurrentTransport());
    }


    public boolean hasCurrentTransport() {
        if (getCurrentTransport() != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void sitDouwn() {
        System.out.println(name + ", сел в транспорт " + getCurrentTransport());
    }

    public void standUp() {
        System.out.println(name + ", вышел из транспорта " + getCurrentTransport() + "\n");
    }

    public boolean move(int distance) {
        if (endurance >= distance) {
            endurance -= distance;
            System.out.println(name + " прошёл дистанцию: " + distance + " метров, осталось выносливости: " + endurance + " ед.");
            return true;
        } else if (endurance > 0 && endurance < distance) {
            int distanceDrive = endurance;
            endurance -= distanceDrive;
            System.out.println(name + " прошёл дистанцию: " + distanceDrive + " метров, из заданных: " + distance + " метров," + " осталось выносливости: " + endurance + " ед.");
            return true;
        } else {
            System.out.println("У " + name + " нет выносливости для преодоления дистанции: " + distance + " метров, остаток выносливости: " + endurance + " ед.");
            return false;
        }
    }

    @Override
    public boolean move(Transport c) {
        if (hasCurrentTransport()) {
            if (c.getName().equals(getCurrentTransport())) {
                System.out.println("\n" + name + " собирается передвигаться по местности " + c.getTerrain());
                sitDouwn();
                c.drive();
                standUp();
            }
            return true;
        } else {
            System.out.println("\n" + name + " пошёл пешком по местности " + c.getTerrain());
            return false;
        }
    }
}
