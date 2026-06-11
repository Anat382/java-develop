package ru.otus.java.basic.lesson20;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerApplication {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту: 8189");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (socket) {
                        System.out.println("Клиент подключился");
                        byte[] buffer = new byte[8192];
                        while (true) {
                            int n = socket.getInputStream().read(buffer);
                            String message = new String(buffer, 0, n);
                            System.out.println(message);
                            CalculateUtils calculateUtils = new CalculateUtils();
                            try {
                                double result = calculateUtils.parseAndCalculate(message);
                                socket.getOutputStream().write(("echo " + result).getBytes(StandardCharsets.UTF_8));
                            } catch (IllegalArgumentException e) {
                                String errorMsg = "Ошибка при расчёте: " + e.getMessage();
                                System.err.println(errorMsg);
                                socket.getOutputStream().write((errorMsg).getBytes(StandardCharsets.UTF_8));
                            }

                            if (message.equals("/exit")) {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
