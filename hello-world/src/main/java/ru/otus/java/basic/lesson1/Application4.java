package ru.otus.java.basic.lesson1;

/*

Описание/Пошаговая инструкция выполнения домашнего задания:
1. Реализовать метод sumOfPositiveElements(..), принимающий в качестве аргумента целочисленный двумерный массив, метод должен посчитать и вернуть сумму всех элементов массива, которые больше 0;
2. Реализовать метод, который принимает в качестве аргумента int size и печатает в консоль квадрат из символов * со сторонами соответствующей длины;
3. Реализовать метод, принимающий в качестве аргумента двумерный целочисленный массив, и зануляющий его диагональные элементы (можете выбрать любую из диагоналей, или занулить обе);
4. Реализовать метод findMax(int[][] array) который должен найти и вернуть максимальный элемент массива;
5. Реализуйте метод, который считает сумму элементов второй строки двумерного массива, если второй строки не существует, то в качестве результата необходимо вернуть -1

 */


public class Application4 {
    public static void main(String[] args) {

        printNumberTask(1);
        int[][] sumAray = {{-1, 0,1,4,10},{-10, -5, 5,5,6}};
        System.out.println(sumOfPositiveElements(sumAray));
        printNumberTask(0);


        printNumberTask(2);
        printSquare(10);
        printNumberTask(0);

        printNumberTask(3);
        int[][] matrixAray = new int[10][10];
        printMatrix(matrixAray);
        printNumberTask(0);

        printNumberTask(4);
        int[][] maxAray = {{1, 5,10,44,20},{0, 5, 7,11,33}};
        System.out.println(findMax(maxAray));
        printNumberTask(0);

        printNumberTask(5);
        int[][] sumArayInd = {{1, 5,10,44,20},{0, 5, 7,11,33}};
        System.out.println(sunElementsArray(sumArayInd));
        printNumberTask(0);

        printNumberTask(5);
        int[][] sumArayIndNot = {{1},{0}};
        System.out.println(sunElementsArray(sumArayIndNot));
        printNumberTask(0);

    }

    public static void printNumberTask(int simbol){
        if (simbol == 0){
            System.out.println("\n\n");
        } else {
            System.out.println("Задача №"  + simbol + "\n");
        }

    }

    public static int sumOfPositiveElements(int[][] array){
        int result = 0;
        for(int lev1 = 0; lev1 < array.length; lev1++){
            for(int lev2 = 0; lev2 < array[lev1].length; lev2++){
                if(array[lev1][lev2] > 0){
                    result += array[lev1][lev2];
                }
            }
        }
        return result;
    }
    
    public static void printSquare(int size){
        String[][] sqArray = new String[size][size];
        for(int lev1 = 0; lev1 < sqArray.length; lev1++){
            for(int lev2 = 0; lev2 < sqArray[lev1].length; lev2++){
                sqArray[lev1][lev2] =  "*";
                System.out.print(sqArray[lev1][lev2] + " ");

            }
            System.out.println();
        }
    }

    public static void printMatrix(int[][] sqArray){
        int position1 = 0;
        int position2 = sqArray.length-1;
        for(int y = 0; y < sqArray.length; y++){
            for(int x = 0; x < sqArray[y].length; x++){
                if(x == position1){
                    sqArray[y][x] =  0;
                } else if (x == position2) {
                    sqArray[y][x] =  0;
                } else {
                    int randValue = (int)(Math.random() * position2 + 1);
                    sqArray[y][x] =  randValue;
                }

                System.out.print(sqArray[y][x] + " ");
            }

            System.out.println();
            position1++;
            position2--;
        }
    }

    public static int findMax(int[][] array){
        int maxValue = 0;
        for(int lev1 = 0; lev1 < array.length; lev1++) {
            for (int lev2 = 0; lev2 < array[lev1].length; lev2++) {
                if(array[lev1][lev2] > maxValue) {
                    maxValue = array[lev1][lev2];
                }
            }
        }
        return maxValue;
    }

    public static int sunElementsArray(int[][] array){
        int result = 0;
        for(int lev1 = 0; lev1 < array.length; lev1++) {
            if(array[lev1].length > 1){
                result += array[lev1][1];
            } else {
                result = -1;
                break;
            }
        }
        return result;
    }

}
