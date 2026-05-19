package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.ActionVehicle;
import ru.otus.java.basic.lesson13.Human;

public class CrossVehicle implements ActionVehicle {
    private String name;
    private int fuel;
    private int fuelConsumption;

    public CrossVehicle(String name, int fuel, int fuelConsumption) {

        this.name = name;
        this.fuel = fuel;
        this.fuelConsumption = fuelConsumption;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean driver(Human human, int distance) {
        int needFuel = (int) Math.ceil((float) fuelConsumption / 100 * ((float) distance / 1000));
        if (needFuel <= fuel) {
            human.setDouwn();
            fuel -= needFuel;
            System.out.println(human.getName() + " проехал дистанцию: " + distance / 1000 + " километров, осталось топлива: " + fuel + " л.");
            human.standUpp();
            return true;
        } else {
            System.out.println("У " + name + " не хватает топлива для преодоления дистанции: " + distance / 1000 + " километров, остаток топлива: " + fuel + " л.");
            return false;
        }
    }
}
