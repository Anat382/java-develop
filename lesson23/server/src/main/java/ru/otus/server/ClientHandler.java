package ru.otus.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String username;
    private boolean authenticated = false;

    public ClientHandler(Socket socket, Server server) throws Exception {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                System.out.println("Client connected from port " + socket.getPort());

                while (true) {
                    String message = in.readUTF();

                    if (!authenticated) {
                        if (message.startsWith("/auth ")) {
                            String name = message.substring(6).trim();
                            if (name.isEmpty()) {
                                sendMessage("Имя не может быть пустым");
                                continue;
                            }
                            if (server.isUsernameTaken(name)) {
                                sendMessage("Имя уже занято, попробуйте другое");
                                sendMessage("/authfail");
                                break;
                            } else {
                                this.username = name;
                                authenticated = true;
                                sendMessage("/authok");
                                server.broadcastMessage(username + " присоединился к чату");
                            }
                        } else {
                            sendMessage("Пожалуйста, представьтесь командой /auth <имя>");
                        }
                        continue;
                    }

                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMessage("/exitok");
                            break;
                        } else if (message.startsWith("/w ")) {
                            String[] parts = message.split(" ", 3);
                            if (parts.length < 3) {
                                sendMessage("Формат: /w <имя> <сообщение>");
                                continue;
                            }
                            String targetName = parts[1];
                            String privateMsg = parts[2];

                            ClientHandler target = server.getClientByName(targetName);
                            if (target == null) {
                                sendMessage("Пользователь " + targetName + " не найден");
                            } else {
                                target.sendMessage("Приватно от " + this.username + ": " + privateMsg);
                                sendMessage("Приватно для " + targetName + ": " + privateMsg);
                            }
                        } else {
                            sendMessage("Неизвестная команда");
                        }
                    } else {
                        server.broadcastMessage(username + ": " + message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void disconnect() {
        server.unsubscribe(this);
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