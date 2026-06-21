package ru.otus.server;

public class ServerAplication {
    public static void main(String[] args) {
        int port = 8189;
        new Server(port).start();
    }
}
