package ru.otus.java.basic.lesson1;
import java.util.Scanner;

//import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

/*
Описание/Пошаговая инструкция выполнения домашнего задания:
        (1) Реализуйте метод greetings(), который при вызове должен отпечатать в столбец 4 слова: Hello, World, from, Java;
        (2) Реализуйте метод TemprecheTest(..), принимающий в качестве аргументов 3 int переменные a, b и c. Метод должен посчитать их сумму, и если она больше или равна 0,
    то вывести в консоль сообщение “Сумма положительная”, в противном случае - “Сумма отрицательная”;
        (3) Реализуйте метод selectColor() в теле которого задайте int переменную data с любым начальным значением.
            Если data меньше 10 включительно, то в консоль должно быть выведено сообщение “Красный”, если от 10 до 20 включительно, то “Желтый”, если больше 20 - “Зеленый”;
        (4) Реализуйте метод compareNumbers(), в теле которого объявите две int переменные a и b с любыми начальными значениями.
            Если a больше или равно b, то необходимо вывести в консоль сообщение “a >= b”, в противном случае “a < b”;
        (5) Создайте метод addOrSubtractAndPrint(int initValue, int delta, boolean increment). Если increment = true,
            то метод должен к initValue прибавить delta и отпечатать в консоль результат, в противном случае - вычесть;
Каждый метод последовательно вызовите из метода main();
(*) При запуске приложения, запросите у пользователя число от 1 до 5, и после ввода выполнения метод, соответствующий указанному номеру со случайным значением аргументов;
*/


public class Application2 {
    public static void main(String[] args){

        int numTask = myScanner();
        if (numTask == 1){
            greetings();
        }
        if (numTask == 2){
            checkSign(myRandom(10), myRandom(10), myRandom(10));
            checkSign(myRandom(-10), myRandom(-10), myRandom(-10));
        }
        if (numTask == 3){
            selectColor();
        }
        if (numTask == 4){
            compareNumbers();
        }
        if (numTask == 5){
            addOrSubtractAndPrint(myRandom(10), myRandom(5), true);
            addOrSubtractAndPrint(myRandom(10), myRandom(5), false);
        }

    }

    //Номер 1
    public static void greetings(){

        System.out.println("Hello");
        System.out.println("World");
        System.out.println("from");
        System.out.println("Java");
    }

    //Номер 2
    public static void checkSign(int a, int b, int c){

        int result = a + b + c;
        if (result >= 0){
            System.out.println("Сумма положительная");
        } else {
            System.out.println("Сумма отрицательная");
        }
    }

    //Номер 3
    public static void selectColor(){

        int data = 25;

        if (data <= 10) {
            System.out.println("Красный");
        } else if (data <= 20) {
            System.out.println("Желтый");
        } else {
            System.out.println("Зеленый");
        };

    }

    //Номер 4
    public static void compareNumbers(){

        int a = 5;
        int b = 10;

        if (a >= b ) {
            System.out.println("a >= b");
        } else {
            System.out.println("a < b");
        }

    }

    //Номер 5
    public static void addOrSubtractAndPrint(int initValue, int delta, boolean increment){

        if (increment){
            System.out.println(initValue + delta);
        } else{
            System.out.println(initValue - delta);
        }
    }

    public static int myScanner() {

        int result;
        Scanner scanner = new Scanner(System.in);
        while (true){

            System.out.println("Введите целое число от 1 до 5");
            if (scanner.hasNextInt()) {
                result = scanner.nextInt();
                if (result > 0 && result <= 5) {
                    System.out.println("Введено: " + result);
                    break;
                } else{
                    System.out.println("Введенное значение не входит в заданный диапазон от 1 до 5, повторите попытку снова");
                }
            } else{
                System.out.println("Введено не целое число повторите попытку снова");
                scanner.next();
            }

        }
        return result;
    }

    public static int myRandom(int n) {
        int rnd = (int)(Math.random() * n);
//        System.out.println(rnd);
        return rnd;
    }


}
