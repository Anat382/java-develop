package ru.otus.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isRunning;

    public Client(String host, int port) throws Exception {
        this.scanner = new Scanner(System.in);
        this.socket = new Socket(host, port);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.isRunning = true;

        new Thread(() -> {
            try {
                while (isRunning) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exitok")) {
                            System.out.println("Сервер подтвердил отключение.");
                            isRunning = false;
                            break;
                        }
                        if (message.startsWith("/kickok")) {
                            System.out.println(message.substring(8));
                            isRunning = false;
                            break;
                        }
                        if (message.startsWith("/authok")) {
                            String username = message.split(" ")[1];
                            System.out.println("Успешная авторизация. Добро пожаловать, " + username + "!");
                        }
                        if (message.startsWith("/regok")) {
                            String username = message.split(" ")[1];
                            System.out.println("Регистрация успешна. Вы вошли как " + username);
                        }
                    } else {
                        System.out.println(message);
                    }
                }
            } catch (Exception e) {
                if (isRunning) {
                    System.err.println("Ошибка при получении сообщения: " + e.getMessage());
                }
            } finally {
                disconnect();
            }
        }).start();

        try {
            while (isRunning) {
                String message = scanner.nextLine();
                if (message == null) continue;
                out.writeUTF(message);
                if (message.equals("/exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при отправке: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        isRunning = false;
        if (scanner != null) scanner.close();
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
        System.out.println("Соединение закрыто.");
    }
}