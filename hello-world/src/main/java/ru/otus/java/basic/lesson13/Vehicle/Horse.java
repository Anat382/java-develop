package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.ActionVehicle;
import ru.otus.java.basic.lesson13.Human;

public class Horse implements ActionVehicle {
    private String name;
    private int endurance;
    private int lossEndurance;

    public Horse(String name, int endurance, int lossEndurance) {
        this.name = name;
        this.endurance = endurance;
        this.lossEndurance = lossEndurance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean driver(Human human, int distance) {
        int LossEnduranceDistance = distance * lossEndurance;
        if (endurance >= LossEnduranceDistance) {
            human.setDouwn();
            endurance -= LossEnduranceDistance;
            System.out.println(name + " с " + human.getName() + " преодолела: " + distance + " метров, осталось выносливости: " + endurance + " ед.");
            human.standUpp();
            return true;
        } else if (endurance > 0 && endurance < LossEnduranceDistance) {
            int distanceRun = endurance / lossEndurance;
            endurance -= endurance;
            System.out.println(name + " с " + human.getName() + " преодолела: " + distanceRun + " метров из заданной дистанции: " + distance + " м." + ", осталось выносливости: " + endurance + " ед.");
            human.standUpp();
            return true;
        } else {
            System.out.println("У " + name + " нет выносливости для преодоления дистанции: " + distance + " метров, остаток выносливости: " + endurance + " ед.");
            return false;
        }
    }
}
