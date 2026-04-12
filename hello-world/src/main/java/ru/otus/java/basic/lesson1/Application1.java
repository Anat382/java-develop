package ru.otus.java.basic.lesson1;


public class Application1 {
    public static void main(String[] args) {

        System.out.println("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {

            System.out.println("i = " + i);
        };

        printTest();
        TemprecheTest();
    }

    public static void printTest() {

        System.out.println("Hello world");
    }

    public static void TemprecheTest() {

        int a = 1;
        int b = 2;
        int sum = a + b;
        System.out.println("sum: " + sum);

        if (a > 10) {
            System.out.println("Значение переменной a строго больше 10");
        }
        if (a != 10) {
            System.out.println("Значение переменной a не равно 10");
        }
        if (sum >= 0) {
            System.out.println("Сумма a и b больше либо равна 0");
        }
        if (a == b) {
            System.out.println("Значение переменной a равно значению переменной b");
        }

    }

}