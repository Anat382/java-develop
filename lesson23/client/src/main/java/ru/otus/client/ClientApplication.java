package ru.otus.client;

public class ClientApplication {
    public static void main(String[] args) {
        try {
            new Client("localhost", 8189);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
