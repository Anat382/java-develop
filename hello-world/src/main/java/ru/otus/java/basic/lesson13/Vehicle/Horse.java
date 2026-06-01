package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.Terrain;
import ru.otus.java.basic.lesson13.Transport;
import ru.otus.java.basic.lesson13.TypeTransport;

public class Horse implements Transport {
    private TypeTransport name;
    private int endurance;
    private int lossEndurance;
    private int distance;
    private Terrain terrain;

    public Horse(TypeTransport name, int endurance, int lossEndurance, int distance, Terrain terrain) {
        this.name = name;
        this.endurance = endurance;
        this.lossEndurance = lossEndurance;
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
            int LossEnduranceDistance = distance * lossEndurance;
            if (endurance >= LossEnduranceDistance) {
                endurance -= LossEnduranceDistance;
                System.out.println(getName() + " преодолела: " + distance + " метров, осталось выносливости: " + endurance + " ед.");
                return true;
            } else if (endurance > 0 && endurance < LossEnduranceDistance) {
                endurance -= endurance;
                int distanceRun = endurance / lossEndurance;
                System.out.println(getName() + " преодолела: " + distanceRun + " метров из заданной дистанции: " + distance + " м." + ", осталось выносливости: " + endurance + " ед.");
                return true;
            } else {
                System.out.println("У " + getName() + " нет выносливости для преодоления дистанции: " + distance + " метров, остаток выносливости: " + endurance + " ед.");
                return false;
            }
        } else {
            System.out.println(getName() + " не может передвигаться по местности " + getTerrain());
            return false;
        }
    }
}
