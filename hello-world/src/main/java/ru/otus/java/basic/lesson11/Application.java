package ru.otus.java.basic.lesson11;

/*
Описание/Пошаговая инструкция выполнения домашнего задания:
Создайте классы Cat, Dog и Horse с наследованием от класса Animal
У каждого животного есть имя, скорость бега и плавания (м/с), и выносливость (измеряется в условных единицах)
Затраты выносливости:
Все животные на 1 метр бега тратят 1 ед выносливости,
Собаки на 1 метр плавания - 2 ед.
Лошади на 1 метр плавания тратят 4 единицы
Кот плавать не умеет.
Реализуйте методы run(int distance) и swim(int distance), которые должны возвращать время, затраченное на указанное действие,
 и “понижать выносливость” животного. Если выносливости не хватает, то возвращаем время -1 и указываем что у животного появилось состояние усталости.
 При выполнении действий пишем сообщения в консоль.
Добавляем метод info(), который выводит в консоль состояние животного.
 */

import ru.otus.java.basic.lesson11.animals.Cat;
import ru.otus.java.basic.lesson11.animals.Dog;
import ru.otus.java.basic.lesson11.animals.Horse;

public class Application {

    public static void main(String[] args) {

        Cat cat = new Cat("Barsik", 13.9f, 100);
        Dog dog = new Dog("Bobik", 4.2f, 1.2f, 300, 2);
        Horse horse = new Horse("Veter", 19.7f, 1.1f, 1000, 4);

        cat.info();
        cat.run(50);
        cat.swim(10);
        cat.run(100);
        cat.run(100);
        cat.info();

        dog.info();
        dog.run(200);
        dog.swim(10);
        dog.run(50);
        dog.swim(50);
        dog.info();

        horse.info();
        horse.run(200);
        horse.swim(50);
        horse.run(400);
        horse.swim(10);
        horse.run(500);
        horse.info();


//        cat.info();
//        cat.run(50);
//        cat.run(60);
//        cat.run(50);
//        cat.info();
//
//        dog.info();
//        dog.run(200);
//        dog.run(200);
//        dog.run(100);
//        dog.info();
//
//        horse.info();
//        horse.run(200);
//        horse.run(500);
//        horse.run(400);
//        horse.info();

//        cat.info();
//        cat.swim(10);
//        cat.info();
//
//        dog.info();
//        dog.swim(25);
//        dog.swim(50);
//        dog.swim(100);
//        dog.info();
//
//        horse.info();
//        horse.swim(40);
//        horse.swim(100);
//        horse.swim(35);
//        horse.info();
    }
}
