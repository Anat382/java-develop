package ru.otus.java.basic.lesson12;

import ru.otus.java.basic.lesson2.User;

/*
Реализуйте классы Тарелка (максимальное количество еды, текущее количество еды) и Кот (имя, аппетит). Количество еды измеряем в условных единицах.
При создании тарелки указывается ее объем и она полностью заполняется едой
В тарелке должен быть метод, позволяющий добавить еду в тарелку. После добавления в тарелке не может оказаться еды больше максимума
В тарелке должен быть boolean метод уменьшения количества еды, при этом после такого уменьшения, в тарелке не может оказаться
отрицательное количество еды (если удалось уменьшить еду так, чтобы в тарелке осталось >= 0 кусков еды, то возвращаем true, в противном случае - false).
Каждому коту нужно добавить поле сытость (когда создаем котов, они голодны). Если коту удалось покушать (хватило еды), сытость = true.
Считаем, что если коту мало еды в тарелке, то он её просто не трогает, то есть не может быть наполовину сыт (это сделано для упрощения логики программы).
Создать массив котов и тарелку с едой, попросить всех котов покушать из этой тарелки и потом вывести информацию о сытости котов в консоль.
 */
public class MainApplication {
    public static void main(String[] args) {

        Cat cat = new Cat("Barsik", 50);
        Plate plate = new Plate(100, 100);

        plate.info();
        cat.eat(plate);
        cat.getInfo();

        plate.info();
        plate.increaseFood(50);
        cat.getInfo();

        plate.info();
        cat.eat(plate);
        cat.getInfo();

        plate.info();
        cat.eat(plate);
        cat.getInfo();

        plate.info();
        cat.eat(plate);
        cat.getInfo();

        plate.info();


        String[][] cats = {
                {"Barsik", "50"},
                {"Leon", "70"},
                {"Sharick", "40"},
                {"Vaska", "30"},
                {"Ryzik", "35"},
                {"Malish", "10"},
        };
        printCats(cats);

    }

    public static void printCats(String[][] array) {
        System.out.println("\n\n\nДом котов:");
        Plate plate = new Plate(200, 200);
        for (int lev1 = 0; lev1 < array.length; lev1++) {
            Cat cats = new Cat(array[lev1][0], Integer.parseInt(array[lev1][1]));

            cats.eat(plate);
            cats.getInfo();
            plate.info();
            System.out.println("\n");

        }
    }
}
