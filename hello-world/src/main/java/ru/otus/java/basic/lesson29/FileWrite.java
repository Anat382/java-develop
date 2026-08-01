package ru.otus.java.basic.lesson29;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileWrite {
    private FileCheck fileCheck;

    public FileWrite(FileCheck fileCheck) {
        this.fileCheck = fileCheck;
    }

    public void writeRow(String row) {
        if (row == null || row.isEmpty()) {
            System.out.println("Нет данных для записи");
            return;
        }

        try (BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(fileCheck.getPath(), true))) {
            byte[] buffer = row.getBytes(StandardCharsets.UTF_8);
            out.write(buffer);
            out.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}