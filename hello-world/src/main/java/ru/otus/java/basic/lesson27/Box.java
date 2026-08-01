package ru.otus.java.basic.lesson27;
import java.util.ArrayList;
import java.util.List;

class Box<T extends Fruit> {
    private List<T> fruits = new ArrayList<>();

    public void add(T fruit) {
        fruits.add(fruit);
    }

    public double weight() {
        double total = 0;
        for (T fruit : fruits) {
            total += fruit.getWeight();
        }
        return total;
    }

    public boolean compare(Box<?> other) {
        return Double.compare(this.weight(), other.weight()) == 0;
    }

    public void transfer(Box<? super T> other) {
        if (this == other) return;
        for (T fruit : fruits) {
            other.add(fruit);
        }
        fruits.clear();
    }

    public int size() {
        return fruits.size();
    }

    @Override
    public String toString() {
        return "Box{" + fruits + "}";
    }
}