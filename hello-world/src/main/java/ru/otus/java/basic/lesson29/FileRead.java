package ru.otus.java.basic.lesson29;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileRead {
    private FileCheck fileCheck;

    public FileRead(FileCheck fileCheck) {
        this.fileCheck = fileCheck;
    }

    public void printFile() {
        if (fileCheck.getPath() == null || fileCheck.getPath().isEmpty()) {
            System.err.println("Файл не выбран");
            return;
        }

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileCheck.getPath()), StandardCharsets.UTF_8))) {
            int n;
            while ((n = in.read()) != -1) {
                System.out.print((char) n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long countOccurrences(String sequence) throws IOException {
        if (sequence == null || sequence.isEmpty()) {
            return 0;
        }
        if (fileCheck.getPath() == null || fileCheck.getPath().isEmpty()) {
            throw new IOException("Путь к файлу не задан");
        }

        long count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileCheck.getPath()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int index = 0;
                while ((index = line.indexOf(sequence, index)) != -1) {
                    count++;
                    index += sequence.length();
                }
            }
        }
        return count;
    }
}