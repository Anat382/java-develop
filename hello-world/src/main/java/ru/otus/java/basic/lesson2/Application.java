package ru.otus.java.basic.lesson2;

/*
Создайте класс Пользователь (User) с полями: фамилия, имя, отчество, год рождения, email;
Реализуйте у класса конструктор, позволяющий заполнять эти поля при создании объекта;
В классе Пользователь реализуйте метод, выводящий в консоль информацию о пользователе в виде:
ФИО: фамилия имя отчество
Год рождения: год рождения
e-mail: email
В методе main() Main класса создайте массив из 10 пользователей и заполните его объектами и с помощью цикла выведите информацию только о пользователях старше 40 лет.
Попробуйте реализовать класс по его описания: объекты класса Коробка должны иметь размеры и цвет. Коробку можно открывать и закрывать.
 Коробку можно перекрашивать. Изменить размер коробки после создания нельзя. У коробки должен быть метод, печатающий информацию о ней в консоль.
 В коробку можно складывать предмет (если в ней нет предмета), или выкидывать его оттуда (только если предмет в ней есть),
 только при условии что коробка открыта (предметом читаем просто строку). Выполнение методов должно сопровождаться выводом сообщений в консоль.

 */

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        User user1 = new User("Ponomarev", "Anatoliy", "Aleksandrovich"
                , "1990", "test@mail.ru");
        user1.getInfo();

        String[][] usersGroup = {
                {"Ivanov", "Oleg", "Petrovich", "1965", "Oleg@mail.ru"},
                {"Petrov", "Dima", "Ivanovich", "1999", "Dima@mail.ru"},
                {"Sidorova", "Olga", "Ivanovna", "2000", "Olga@mail.ru"},
                {"Smirnova", "Lena", "Petrovna", "2005", "Lena@mail.ru"},
                {"Kuznetsov", "Maxim", "Sergeevich", "1980", "Maxim@mail.ru"},
                {"Popova", "Anna", "Alexandrovna", "1975", "Anna@mail.ru"},
                {"Sokolov", "Alex", "Mikhailovich", "1965", "Alex@mail.ru"},
                {"Lebedeva", "Sveta", "Vladimirovna", "1995", "Sveta@mail.ru"},
                {"Kozlov", "Andrey", "Andreevich", "1987", "Andrey@mail.ru"},
                {"Novikov", "Pavel", "Pavlovich", "1990", "Pavel@mail.ru"}
        };
        printUser(usersGroup);

        // Создадим коробку
        Box myBox = new Box(40, 20, "Red");
        myBox.getInfo();
        myBox.color = "Yellow";
        myBox.getInfo();
        myBox.inputItem("Книга");
        myBox.openBox();
        myBox.inputItem("Книга");
        myBox.openBox();
        myBox.outputItem();
        myBox.openBox();
        myBox.outputItem();
        myBox.inputItem("Фоторамка");
        myBox.openBox();
        myBox.inputItem("Документы");
    }

    public static void printUser(String[][] array) {
        for (int lev1 = 0; lev1 < array.length; lev1++) {
            User users = new User(array[lev1][0], array[lev1][1], array[lev1][2], array[lev1][3], array[lev1][4]);
            int ageUser = users.getAge();
            if (ageUser > 40) {
                users.getInfo();
            }
        }
    }
}
