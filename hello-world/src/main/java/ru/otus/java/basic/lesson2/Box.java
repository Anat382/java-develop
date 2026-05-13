package ru.otus.java.basic.lesson2;

public class Box {
    private int width;
    private int heigth;
    private int length;
    String color;
    private boolean isOpened = false;
    private String item;

    public Box(int width, int heigth, int length, String color) {
        this.width = width;
        this.heigth = heigth;
        this.length = length;
        this.color = color;
    }

    public void getInfo() {
        System.out.println("\nШирина коробки: " + width);
        System.out.println("Высота коробки: " + heigth);
        System.out.println("Длинна коробки: " + length);
        System.out.println("Цвет коробки: " + color);
    }

    public void inputItem(String element) {
        if (isOpened && item == null) {
            item = element;
            System.out.println("В коробку положили предмет: " + item);
            isOpened = false;
        } else if (!isOpened) {
            System.out.println("Откройте коробку что бы положить предмет!");
        } else if (item != null) {
            System.out.println("В коробке уже есть предмет " + item + ", уберите его что бы положить новый!");
        }
    }

    public void outputItem() {
        if (isOpened && item != null) {
            System.out.println("Из коробку убрали предмет: " + item);
            item = null;
            isOpened = false;
        } else if (!isOpened) {
            System.out.println("Откройте коробку что бы убрать предмет!");
        } else if (item == null) {
            System.out.println("В коробке пусто, положите предмет!");
        }
    }

    public void open() {
        isOpened = true;
        System.out.println("Коробка теперь открыта!");
    }

}
