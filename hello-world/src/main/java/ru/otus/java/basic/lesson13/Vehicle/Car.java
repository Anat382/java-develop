package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.Terrain;
import ru.otus.java.basic.lesson13.Transport;
import ru.otus.java.basic.lesson13.TypeTransport;

public class Car implements Transport {
    private TypeTransport name;
    private int fuel;
    private int fuelConsumption;
    private int distance;
    private Terrain terrain;

    public Car(TypeTransport name, int fuel, int fuelConsumption, int distance, Terrain terrain) {
        this.name = name;
        this.fuel = fuel;
        this.fuelConsumption = fuelConsumption;
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
        return !getTerrain().equals(Terrain.swamp.getName()) && !getTerrain().equals(Terrain.denseForest.getName());
    }

    @Override
    public boolean drive() {
        if (equalsTerrain()) {
            int needFuel = (int) Math.ceil((float) fuelConsumption / 100 * ((float) distance / 1000));
            if (needFuel <= fuel) {
                fuel -= needFuel;
                System.out.println("Проехал дистанцию: " + distance / 1000 + " километров, осталось топлива: " + fuel + " л.");
                return true;
            } else {
                System.out.println("У " + getName() + " не хватает топлива для преодоления дистанции: " + distance / 1000 + " километров, остаток топлива: " + fuel + " л.");
                return false;
            }
        } else {
            System.out.println(getName() + " не может передвигаться по местности " + getTerrain());
            return false;
        }
    }
}
