package ru.otus.java.basic.lesson27;

public class MainApp {
    public static void main(String[] args) {

        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple());
        appleBox.add(new Apple());


        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());


        Box<Fruit> fruitBox = new Box<>();
        fruitBox.add(new Apple());
        fruitBox.add(new Orange());


        System.out.println("Вес appleBox: " + appleBox.weight());
        System.out.println("Вес orangeBox: " + orangeBox.weight());
        System.out.println("Вес fruitBox: " + fruitBox.weight());


        System.out.println("appleBox.compare(orangeBox): " + appleBox.compare(orangeBox));
        System.out.println("appleBox.compare(fruitBox): " + appleBox.compare(fruitBox));

        System.out.println("\nПересыпаем яблоки из appleBox в fruitBox");
        appleBox.transfer(fruitBox);
        System.out.println("После пересыпки:");
        System.out.println("appleBox size: " + appleBox.size());
        System.out.println("fruitBox size: " + fruitBox.size());
        System.out.println("Новый вес fruitBox: " + fruitBox.weight());


        System.out.println("\nПересыпаем апельсины из orangeBox в fruitBox");
        orangeBox.transfer(fruitBox);
        System.out.println("После пересыпки:");
        System.out.println("orangeBox size: " + orangeBox.size());
        System.out.println("fruitBox size: " + fruitBox.size());
        System.out.println("Новый вес fruitBox: " + fruitBox.weight());
    }
}