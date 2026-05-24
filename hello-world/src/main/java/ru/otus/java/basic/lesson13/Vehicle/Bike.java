package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.Terrain;
import ru.otus.java.basic.lesson13.Transport;
import ru.otus.java.basic.lesson13.TypeTransport;

public class Bike implements Transport {
    private TypeTransport name;
    private int endurance;
    private int distance;
    private Terrain terrain;

    public Bike(TypeTransport name, int endurance, int distance, Terrain terrain) {
        this.name = name;
        this.endurance = endurance;
        this.distance = distance;
        this.terrain = terrain;
    }

    @Override
    public String getName() {
        return name.getType();
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public String getTerrain() {
        return terrain.getName();
    }

    @Override
    public boolean equalsTerrain() {
        return !getTerrain().equals(Terrain.swamp.getName());
    }

    @Override
    public boolean drive() {
        if (equalsTerrain()) {
            if (endurance >= distance) {
                endurance -= distance;
                System.out.println("Преодолена дистанция: " + distance / 1000 + " километров, осталось выносливости: " + endurance + " ед.");
                return true;
            } else if (endurance > 0 && endurance < distance) {
                int distanceDrive = endurance;
                endurance -= distanceDrive;
                System.out.println("Преодолена дистанция: " + distanceDrive + " метров, из заданных: " + distance / 1000 + " километров," + " осталось выносливости: " + endurance + " ед.");
                return true;
            } else {
                System.out.println("У человека нет выносливости для преодоления дистанции: " + distance / 1000 + " километров, остаток выносливости: " + endurance + " ед.");
                return false;
            }
        } else {
            System.out.println(getName() + " не может передвигаться по местности " + getTerrain());
            return false;
        }

    }
}
