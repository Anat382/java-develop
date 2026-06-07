package ru.otus.java.basic.lesson19;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {
    private FileCheck fileCheck;

    public FileUtils(FileCheck fileCheck) {
        this.fileCheck = fileCheck;
    }

    public void inputFile() {
        Scanner scanner = new Scanner(System.in);
        String fileName;

        while (true) {
            System.out.print("Введите название файла (или 'exit' для выхода): ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Ввод отменён.");
                fileCheck.setPath(null);
                break;
            }

            if (line.isEmpty()) {
                System.out.println("Ошибка: название файла не может быть пустым.");
                continue;
            }

            if (line.contains("/") || line.contains("\\") || line.contains(":")) {
                System.out.println("Недопустимые символы в имени файла.");
                continue;
            }

            fileName = line;
            String newPath = fileCheck.getDir() + fileName;
            fileCheck.setPath(newPath);
            fileCheck.setfile(newPath);
            fileCheck.setNameFile(fileName);

            if (fileCheck.fileCheckExists()) {
                System.out.println("Выбран файл: " + fileName);
                break;
            } else {
                System.out.println("Файл не существует. Попробуйте снова.");
            }
        }
    }

    public void printFiles() {
        File dir = fileCheck.getDirFile();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                System.out.println(f.getName() + " " + (f.isFile() ? "файл" : "дир"));
            }
        } else {
            System.out.println("Папка не найдена или пуста.");
        }
    }

    public void fileCreate() throws IOException {
        File file = fileCheck.getfile();
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Не удалось создать директории: " + parentDir.getAbsolutePath());
        }
        if (!file.createNewFile()) {
            throw new IOException("Не удалось создать файл: " + file.getAbsolutePath() +
                    " (возможно, файл уже существует или недостаточно прав)");
        }
    }

    public String inputText() {
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            System.out.print("\nВведите текст для записи (или 'exit' для выхода): ");
            line = scanner.nextLine();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Ввод отменён.");
                return null;
            }

            if (line.isEmpty()) {
                System.out.println("Ошибка: строка не может быть пустой.");
            } else {
                return line;
            }
        }
    }
}