package ru.otus.java.basic.lesson12;

public class Plate {
    private int food;
    private int maxFood;

    public Plate(int maxFood, int food) {
        this.maxFood = maxFood;
        this.food = food;
    }

    public int getFood() {
        return food;
    }

    public void increaseFood(int amount) {
        if (food + amount <= maxFood) {
            food += amount;
            System.out.println("В тарелку положили еду: " + amount + " ед.");
        } else {
            System.out.println("Тарелка нет места");
        }
    }

    public boolean decreaseFood(int amount) {
        if (food - amount >= 0) {
            food -= amount;
            return true;
        } else {
            System.out.println("В тарелке мало еды, положите еды!");
            return false;
        }
    }

    public void info() {
        System.out.println("Plate food: " + food);
    }
}
