package ru.otus.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner sc;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String host, int port) throws Exception {
        this.sc = new Scanner(System.in);
        this.socket = new Socket(host, port);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        System.out.print("Введите ваше имя: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Имя не может быть пустым. Завершение.");
            disconnect();
            return;
        }

        out.writeUTF("/auth " + name);

        String response = in.readUTF();
        if (!response.equals("/authok")) {
            System.out.println("Ошибка авторизации: " + response);
            disconnect();
            return;
        }
        System.out.println("Авторизация успешна. Добро пожаловать, " + name + "!");
        System.out.println("\"Для отправки личных сообщений используйте шаблон: /w <имя> <сообщение>\"");

        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.equals("/exitok")) {
                        break;
                    }
                    System.out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();

        try {
            while (true) {
                String message = sc.nextLine();
                out.writeUTF(message);
                if (message.equals("/exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        sc.close();
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}