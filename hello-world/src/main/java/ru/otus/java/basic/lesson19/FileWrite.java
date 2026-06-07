package ru.otus.java.basic.lesson19;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileWrite {
    private FileCheck fileCheck;

    public FileWrite(FileCheck fileCheck) {
        this.fileCheck = fileCheck;
    }

    public void writeRow(String row) {

        try (BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(fileCheck.getPath(), true)
        )) {
            if (!row.equals("exit")) {
                byte[] buffer = row.getBytes(StandardCharsets.UTF_8);
                out.write(buffer);
                out.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
            } else {
                System.out.println("Нет данных для записи");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
