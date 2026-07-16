package ru.otus.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Scanner sc;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public Client(String host, int port) throws Exception {
        this.sc = new Scanner(System.in);
        this.socket = new Socket(host, port);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exitok")) {
                            break;
                        }
                        if (message.startsWith("/kickok")) {
                            System.out.println(message);
                            out.writeUTF("Соединение разорвано");
                            System.exit(0);
                        }
                        if (message.startsWith("/authok")) {
                            String username = message.split(" ")[1];
                            System.out.println("Удалось успешно подключится к чату с именем пользователя: " + username);
                        }
                        if (message.startsWith("/regok")) {
                            String username = message.split(" ")[1];
                            System.out.println("Удалось успешно зарегистрироваться и войти в чат с именем пользователя: " + username);
                        }
                    } else {
                        System.out.println(message);
                    }

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
