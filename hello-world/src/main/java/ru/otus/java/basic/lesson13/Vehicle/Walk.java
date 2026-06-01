package ru.otus.java.basic.lesson13.Vehicle;

import ru.otus.java.basic.lesson13.ActionVehicle;
import ru.otus.java.basic.lesson13.Human;

public class Walk implements ActionVehicle {
    private String name;
    int endurance;

    public Walk(String name, int endurance) {
        this.name = name;
        this.endurance = endurance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean driver(Human human, int distance) {
        if (endurance >= distance) {
            human.setDouwn();
            endurance -= distance;
            System.out.println(human.getName() + " проехал дистанцию: " + distance + " метров, осталось выносливости: " + endurance + " ед.");
            return true;
        } else if (endurance > 0 && endurance < distance) {
            human.setDouwn();
            int distanceDrive = endurance;
            endurance -= distanceDrive;
            System.out.println(human.getName() + " преодолел дистанцию: " + distanceDrive + " метров, из заданных: " + distance + " метров," + " осталось выносливости: " + endurance + " ед.");
            human.standUpp();
            return true;
        } else {
            System.out.println("У человека нет выносливости для преодоления дистанции: " + distance + " метров, остаток выносливости: " + endurance + " ед.");
            return false;
        }
    }
}
