package ru.otus.java.basic.lesson19;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileRead {
    private FileCheck fileCheck;

    public FileRead(FileCheck fileCheck) {
        this.fileCheck = fileCheck;
    }
    public void printFile() {

        try(BufferedReader in =  new BufferedReader(
                new InputStreamReader(new FileInputStream(fileCheck.getPath()), StandardCharsets.UTF_8))) {
            if (!fileCheck.getPath().isEmpty()) {
                int n = in.read();
                while (n != -1) {
                    System.out.print((char) n);
                    n = in.read();
                }
            } else {
                System.err.println("Файл не выбран");
            }


        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
