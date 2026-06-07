package ru.otus.java.basic.lesson19;

import java.io.IOException;

public class MainApp {

    public static void main(String[] args) {
        String dir = "testDir/";
        String name = "file.txt";
        FileCheck fileCheck = new FileCheck(dir, name);
        FileUtils fileUtils = new FileUtils(fileCheck);
        FileRead fileRead = new FileRead(fileCheck);
        FileWrite fileWrite = new FileWrite(fileCheck);

        System.out.println("Ищем файл по пути: " + fileCheck.getAbsPath());
        System.out.println("Рабочая директория: " + System.getProperty("user.dir"));

        if (fileCheck.fileCheckExists()) {
            System.out.println("Файл уже существует.");
        } else {
            try {
                fileUtils.fileCreate();
                System.out.println("Файл успешно создан по пути: " + fileCheck.getAbsPath());
            } catch (IOException e) {
                System.err.println("Ошибка при создании: " + e.getMessage());
            }
        }

        System.out.println("\nСписок файлов в папке: " + dir);
        fileUtils.printFiles();

        fileUtils.inputFile();

        if (fileCheck.getPath() != null) {
            fileRead.printFile();
        } else {
            System.out.println("Работа завершена пользователем.");
            return;
        }

        String text = fileUtils.inputText();
        fileWrite.writeRow(text);
        fileRead.printFile();
    }
}