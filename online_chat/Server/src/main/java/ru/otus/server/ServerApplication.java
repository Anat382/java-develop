package ru.otus.server;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 8189;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Неверный порт. Используется порт по умолчанию: 8189");
            }
        }
        new Server(port).start();
    }
}