package ru.otus.client;

public class ClientApplication {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8189;
        if (args.length >= 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Неверный порт. Используется порт по умолчанию: 8189");
            }
        }
        try {
            new Client(host, port);
        } catch (Exception e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
            e.printStackTrace();
        }
    }
}