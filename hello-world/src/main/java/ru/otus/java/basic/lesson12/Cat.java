package ru.otus.java.basic.lesson12;

public class Cat {
    private String name;
    private int appetite;
    private boolean satiety = false;

    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
    }

    public void eat(Plate plate) {
        System.out.println("Кот " + name + " кушает");
        if (plate.decreaseFood(appetite)) {
            satiety = true;
        } else {
            satiety = false;
        }
    }

    public void getInfo() {
        if (satiety) {
            System.out.println("Кот " + name + " сытый");
        } else {
            System.out.println("Кот " + name + " голодный");
        }
    }

}
