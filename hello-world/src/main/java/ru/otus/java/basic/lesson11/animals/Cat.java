package ru.otus.java.basic.lesson11.animals;

public class Cat extends Animal {
    public Cat(String name, float speedRun, int endurance) {
        this.name = name;
        this.speedRun = speedRun;
        this.endurance = endurance;
    }

    @Override
    public int swim(int distance) {
        System.out.println("Кошка не умеет плавать!");
        return -1;
    }
}
