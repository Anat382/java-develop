package ru.otus.server;


public class ServerAplication {
    static void main() {
        int port = 8189;
        new Server(port).start();
    }
}