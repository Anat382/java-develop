package ru.otus.java.basic.lesson13.Location;

import ru.otus.java.basic.lesson13.ActionVehicle;
import ru.otus.java.basic.lesson13.Actions;
import ru.otus.java.basic.lesson13.Human;
import ru.otus.java.basic.lesson13.TypeTransport;

public class DenseForest implements Actions {
    private int distance;
    public Human human;
    public String name;

    public DenseForest(String name, Human human, int distance) {
        this.name = name;
        this.human = human;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public void doIt(ActionVehicle c) {
        System.out.println("\n" + human.getName() + " собирается передвигаться на транспорте " + c.getName() + " по местности  " + name);
        if (c.getName() != TypeTransport.CarTp.getType()) {
            c.driver(human, distance);
        } else {
            System.out.println(c.getName() + " не может передвигаться по местности " + name);
        }
    }
}