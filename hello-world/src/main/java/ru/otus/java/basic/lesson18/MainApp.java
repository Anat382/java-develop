package ru.otus.java.basic.lesson18;

import java.util.Arrays;

public class MainApp {
    public static void main(String[] args) {
        PersonDataBase personDataBase = new PersonDataBase();

        personDataBase.add(new Person("Dima", Position.DEVELOPER, 1L));
        personDataBase.add(new Person("Olga", Position.DIRECTOR, 2L));
        personDataBase.add(new Person("Anna", Position.MANAGER, 3L));
        personDataBase.add(new Person("Ivan", Position.DRIVER, 4L));
        personDataBase.add(new Person("Elena", Position.ENGINEER, 5L));
        personDataBase.add(new Person("Sergey", Position.SENIOR_MANAGER, 6L));
        personDataBase.add(new Person("Maria", Position.QA, 7L));
        personDataBase.add(new Person("Petr", Position.JANITOR, 8L));
        personDataBase.add(new Person("Svetlana", Position.PLUMBER, 9L));
        personDataBase.add(new Person("Alexey", Position.BRANCH_DIRECTOR, 10L));
        personDataBase.add(new Person("Natalia", Position.JUNIOR_DEVELOPER, 11L));

        Person found = personDataBase.findById(11L);
        System.out.println("Поиск по id: " + found);

        Person dima = personDataBase.findById(1L);
        System.out.println("Сотрудник Dima менеджер? " + personDataBase.isManager(dima));

        Person olga = personDataBase.findById(2L);
        System.out.println("Сотрудник Olga менеджер? " + personDataBase.isManager(olga));

        System.out.println("Сотрудник с id 1 ме менеджер " + personDataBase.isEmployee(1L));
        System.out.println("Сотрудник с id 2 ме менеджер " + personDataBase.isEmployee(2L));

        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Исходный массив: " + Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println("После сортировки пузырьком: " + Arrays.toString(arr));

        int[] arr2 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Исходный массив: " + Arrays.toString(arr2));
        quickSortHoare(arr2, 0, arr2.length - 1);
        System.out.println("После быстрой сортировки:  " + Arrays.toString(arr2));
    }

    public static void bubbleSort(int[] array) {
        if (array == null) return;
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static void quickSortHoare(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionHoare(array, low, high);
            quickSortHoare(array, low, pivotIndex);
            quickSortHoare(array, pivotIndex + 1, high);
        }
    }

    private static int partitionHoare(int[] array, int low, int high) {
        int pivot = array[low + (high - low) / 2];
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do {
                i++;
            } while (array[i] < pivot);

            do {
                j--;
            } while (array[j] > pivot);

            if (i >= j) {
                return j;
            }
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

    }
}