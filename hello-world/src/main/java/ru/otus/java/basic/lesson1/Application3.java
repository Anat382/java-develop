package ru.otus.java.basic.lesson1;
import java.util.Arrays;
/*
-Реализуйте метод, принимающий в качестве аргументов целое число и строку, и печатающий в консоль строку указанное количество раз
-Реализуйте метод, принимающий в качестве аргумента целочисленный массив, суммирующий все элементы, значение которых больше 5, и печатающий полученную сумму в консоль.
-Реализуйте метод, принимающий в качестве аргументов целое число и ссылку на целочисленный массив, метод должен заполниться каждую ячейку массива указанным числом.
-Реализуйте метод, принимающий в качестве аргументов целое число и ссылку на целочисленный массив, увеличивающий каждый элемент которого на указанное число.
-Реализуйте метод, принимающий в качестве аргумента целочисленный массив, и печатающий в консоль сумма элементов какой из половин массива больше.


* Домашнее задание
Цель домашнего задания: науùитþсā реúатþ простýе задаùи с исполþзованием изуùеннýх конструкøий
āзýка Java.
● Реализуйте метод, принимаĀûий на вход набор øелоùисленнýх массивов, и полуùаĀûий новýй
массив равнýй сумме входāûих;
Пример: { 1, 2, 3 }
 + { 2, 2 }
 + { 1, 1, 1, 1, 1}
 = { 4, 5, 4, 1, 1 }
● Реализуйте метод, проверāĀûий ùто естþ ƈтоùкаƉ в массиве, в которой сумма левой и правой ùасти
равнý. ƈТоùка находитсā между ÿлементамиƉ;
Пример: { 1, 1, 1, 1, 1, | 5 }, { 5, | 3, 4, -2 }, { 7, 2, 2, 2 }, { 9, 4 }
● Реализуйте метод, проверāĀûий ùто все ÿлементý массива идут в порāдке убýваниā или
возрастаниā (по вýбору полþзователā)
● Реализуйте метод, ƈперевораùиваĀûийƉ входāûий массив
Пример: { 1 2 3 4 } => { 4 3 2 1 }
 */


import java.sql.Array;
import java.util.Arrays;

public class Application3 {

    public static void main(String[] args) {
        printValue(5, "Число");
        sumArrPrint(1,2,1,5,5);
        indexAtrChange(1, new int[]{1, 2, 1, 5, 5});
        sumArrValue(1,3,9,6,1,9,2);

        processArraysSum(
            new int[]{1, 2, 3},
            new int[]{2, 2},
            new int[]{1, 1, 1, 1, 1}
        );
        chekValueSort(0, new int[]{1,5,2,9});
        chekValueSort(0, new int[]{1,2,3,5});
        chekValueSort(1, new int[]{5,3,2,1});
    }

    public static void printValue(int value, String us_text) {

        for (int i = 0; i < value; i++) {
            System.out.println(us_text);
        }

    }

    public static void sumArrPrint(int... arr) {
        int sum_arr = 0;
        for (int i = 0; i < arr.length; i++) {
            int vlue_arr = arr[i];
            if (vlue_arr >= 5) {
                sum_arr += vlue_arr;
            }
        }
        System.out.println(sum_arr);
    }

    public static void indexAtrChange(int value, int[] arrVal) {

        for (int i = 0; i < arrVal.length; i++) {
            int valArr = arrVal[i];
            arrVal[i] = valArr + value;
        }
        System.out.println(Arrays.toString(arrVal));
    }

    public static void sumArrValue(int... arr) {
        int sum_arr_part1 = 0;
        int sum_arr_part2 = 0;
        for (int i = 0; i < arr.length/2; i++) {
            int value_arr = arr[i];
            sum_arr_part1 += value_arr;
        }

        for (int i = arr.length/2; i < arr.length; i++) {
            int value_arr = arr[i];
            sum_arr_part2 += value_arr;
        }
        if (sum_arr_part1 > sum_arr_part2){
            System.out.println("Сумма первой части массива больше:" + sum_arr_part1);
        } else{
            System.out.println("Сумма второй части массива больше:" + sum_arr_part2);
        }

    }

    public static void processArraysSum(int[]... arrays) {
        int maxLength = 0;
        for (int[] arr : arrays) {
            if (arr.length > maxLength) {
                maxLength = arr.length;
            }
        }

        int[] result = new int[maxLength];

        for (int[] arr : arrays) {
            for (int i = 0; i < arr.length; i++) {
                result[i] += arr[i];
            }
        }
        System.out.println(Arrays.toString(result));
    }

    public static void chekValueSort(int flagSort, int[] array) {

        if(flagSort == 0){
            System.out.println("\nПроверка сортировки значений массива по возрастанию:" + Arrays.toString(array));
        }else{
            System.out.println("\nПроверка сортировки значений массива по убыванию:" + Arrays.toString(array));
        }
        int flagCheck = 0;
         for (int i = 0; i < array.length - 1; i++){
             int valueArr = array[i];
             if (flagSort == 0){
                 if (valueArr < array[i+1]){
                     flagCheck = 1;
                 } else{
                     flagCheck = 0;
                     break;
                 }
             } else {
                 if (valueArr > array[i+1]){
                     flagCheck = 1;
                 } else{
                     flagCheck = 0;
                     break;
                 }
             }
         }
        if(flagCheck == 1){
            System.out.println("Значения отсортированы");
        } else{
            System.out.println("Значения не отсортированы");
        }
    }
}
