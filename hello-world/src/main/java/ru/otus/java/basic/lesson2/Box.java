package ru.otus.java.basic.lesson2;

public class Box {
    private int width;
    private int heigth;
    public String color;
    private boolean open = false;
    private boolean close = true;
    private String item;
    private boolean flagItem = false;

    public Box(int width, int heigth, String color){
        this.width = width;
        this.heigth = heigth;
        this.color = color;
    }

    public void getInfo(){
        System.out.println("\nШирина коробки: " + width);
        System.out.println("Высота коробки: " + heigth);
        System.out.println("Цвет коробки: " + color);
    }

    public void inputItem(String element){
        if (open && !flagItem){
            item = element;
            flagItem = true;
            System.out.println("В коробку положили предмет: " + item);
            closeBax();
        } else if (!open){
            System.out.println("Откройте коробку что бы положить предмет!");
        } else if (flagItem){
            System.out.println("В коробке уже есть предмет " + item + ", уберите его что бы положить новый!");
        }
    }

    public void outputItem(){
        if (open && flagItem){
            flagItem = false;
            System.out.println("Из коробку убрали предмет: " + item);
            item = "";
            closeBax();
        } else if (!open){
            System.out.println("Откройте коробку что бы убрать предмет!");
        } else if (!flagItem){
            System.out.println("В коробке пусто, положите предмет!");
        }
    }

    public void openBox(){
        open = true;
        close = false;
        System.out.println("Коробка теперь открыта!");
    }

    public void closeBax(){
        close = true;
        open = false;
        System.out.println("Коробка теперь закрыта!");
    }
}
