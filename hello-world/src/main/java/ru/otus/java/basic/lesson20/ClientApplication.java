package ru.otus.java.basic.lesson20;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (Socket socket = new Socket("localhost", 8189)) {
                byte[] buffer = new byte[8192];
                while (true) {
                    String message = scanner.nextLine();
                    if (message.isEmpty()) {
                        System.err.println("Не введено выражение " + message);
                        break;
                    }
                    socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
                    int n = socket.getInputStream().read(buffer);
                    if (n == -1) {
                        System.err.println("Ошибка в операции вычисления " + message);
                        break;
                    }
                    String response = new String(buffer, 0, n);
                    System.out.println(response);

                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}