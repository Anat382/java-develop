package ru.otus.client;


public class ClientApplication {
    static void main()  {
        try {
            new Client("localhost", 8189);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
