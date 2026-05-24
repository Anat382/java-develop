package ru.otus.java.basic.lesson15;


public class MainApp {

    public static void main(String[] args) {
        System.out.println("Начало ");

        String[][] arrInt = {
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"}
        };

        String[][] arrSizeError = {
                {"1", "2", "3", "4"},
                {"5", "6", "7d", "8"},
        };

        String[][] arrIntError = {
                {"1", "2", "3", "4"},
                {"5", "6", "7s", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"}
        };

        Object[] arrais = {
                arrInt,
                arrSizeError,
                arrIntError
        };

        int number = 0;
        for (Object arr : arrais) {
            try {
                System.out.println("\nМассив: " + number);
                System.out.println("Сумма значений массива: " + sumElementsArray((String[][]) arr));
            } catch (AppArraySizeException | AppArrayDataException e) {
                e.printStackTrace();
            }
            number++;
        }

        System.out.println("Конец");

    }

    public static int sumElementsArray(String[][] array) {
        int result = 0;
        for (int l1 = 0; l1 < array.length; l1++) {
            if (array.length != 4 || array[l1].length != 4) {
                throw new AppArraySizeException("\nНекорректный размер массива: ", "count row: " + array.length + ", length row: " + array[l1].length);
            } else {
                for (int l2 = 0; l2 < array[l1].length; l2++) {
                    try {
                        result += Integer.parseInt(array[l1][l2]);
                    } catch (NumberFormatException e) {
                        throw new AppArrayDataException("\nНекорректный тип данных, не удалось преобразовать текст в int: ", "value: " + array[l1][l2] + ", index row: " + l1 + ", index value: " + l2);
                    }
                }
            }
        }
        return result;
    }
}

//array[l1][l2].matches("\\d+"
//