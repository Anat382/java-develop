package ru.otus.java.basic.lesson27;

abstract class Fruit {
    protected double weight;

    public Fruit(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}