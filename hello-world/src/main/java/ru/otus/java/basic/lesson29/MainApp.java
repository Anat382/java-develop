package ru.otus.java.basic.lesson29;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        // Корневой каталог проекта – текущая рабочая директория
        String dir = "." + File.separator;
        String name = "";

        FileCheck fileCheck = new FileCheck(dir, name);
        FileUtils fileUtils = new FileUtils(fileCheck);
        FileRead fileRead = new FileRead(fileCheck);
        FileWrite fileWrite = new FileWrite(fileCheck);

        // Вывод списка файлов из корневого каталога
        fileUtils.printFiles();

        // Ввод имени файла пользователем
        fileUtils.inputFile();

        // Проверка, что файл выбран (не был введён exit)
        if (fileCheck.getPath() == null) {
            System.out.println("Работа завершена пользователем.");
            return;
        }

        // Запрос искомой последовательности
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите искомую последовательность символов: ");
        String sequence = scanner.nextLine().trim();

        // Подсчёт вхождений
        try {
            long count = fileRead.countOccurrences(sequence);
            System.out.println("Количество вхождений \"" + sequence + "\" в файле: " + count);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        // Дополнительно: вывод содержимого файла (опционально)
        System.out.println("\nСодержимое файла:");
        fileRead.printFile();

        // Запись дополнительной строки (опционально, для демонстрации)
        String text = fileUtils.inputText();
        if (text != null) {
            fileWrite.writeRow(text);
            System.out.println("Строка добавлена в файл.");
            System.out.println("Обновлённое содержимое:");
            fileRead.printFile();
        }
    }
}