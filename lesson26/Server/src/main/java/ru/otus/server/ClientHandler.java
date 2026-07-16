package ru.otus.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String username;
    private String role;
    private boolean authenticated;

    public ClientHandler(Socket socket, Server server) throws Exception {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            try {
                System.out.println("Client connected... " + socket.getPort());
                // цикл аутентификации
                while (true) {
                    sendMessage("Для начала работы необходимо пройти аутентификацию \n" +
                            "'/auth login password' или пройти регистрацию \n" +
                            "'/reg login password username role' ");

                    String message;
                    try {
                        message = in.readUTF();
                    } catch (SocketException | EOFException e) {
                        // Клиент отключился
                        System.out.println("Клиент " + socket.getPort() + " разорвал соединение при аутентификации");
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMessage("/exitok");
                            break;
                        }

                        if (message.startsWith("/auth ")) {
                            String[] token = message.split(" ");
                            if (token.length != 3) {
                                sendMessage("Неверный формат команды /auth");
                                continue;
                            }
                            if (server.getAuthenticatedProvider().authenticate(this, token[1], token[2])) {
                                authenticated = true;
                                sendMessage("Роль пользователя " + role);
                                if ("ADMIN".equals(role)) {
                                    sendMessage("Для отключения пользователя используйте: /kick username");
                                }
                                break;
                            }
                        }

                        if (message.startsWith("/reg ")) {
                            String[] token = message.split(" ");
                            if (token.length != 5) {
                                sendMessage("Неверный формат команды /reg");
                                continue;
                            }
                            if (server.getAuthenticatedProvider()
                                    .register(this, token[1], token[2], token[3], token[4])) {
                                authenticated = true;
                                if ("ADMIN".equals(role)) {
                                    sendMessage("Для отключения пользователя используйте: /kick username \n ");
                                }
                                break;
                            }
                        }
                    }
                }

                // цикл работы
                while (authenticated) {
                    String message;
                    try {
                        message = in.readUTF();
                    } catch (SocketException | EOFException e) {
                        System.out.println("Клиент " + username + " разорвал соединение");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMessage("/exitok");
                            break;
                        }

                        if (message.startsWith("/kick ")) {
                            String[] token = message.split(" ");
                            if (token.length != 2) {
                                sendMessage("Неверный формат команды. Используйте: /kick username");
                                continue;
                            }
                            String targetUsername = token[1];

                            if (!"ADMIN".equals(role)) {
                                sendMessage("У вас нет прав для выполнения этой команды");
                                continue;
                            }
                            if (targetUsername.equals(username)) {
                                sendMessage("Нельзя отключить самого себя");
                                continue;
                            }

                            ClientHandler targetClient = server.getClientByUsername(targetUsername);
                            if (targetClient == null) {
                                sendMessage("Пользователь " + targetUsername + " не найден в чате");
                                continue;
                            }
                            targetClient.sendMessage("/kickok Вы были отключены администратором " + username);
                            server.broadcastMessage(username, " Пользователь " + targetUsername + " был отключен администратором " + username);
                            continue;
                        }

                    } else {
                        server.broadcastMessage(username, message);
                    }
                }
            } catch (Exception e) {
                // Любая другая ошибка
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
            System.err.println("Не удалось отправить сообщение клиенту " + username + ": " + e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void disconnect() {
        server.unsubscribe(this, true);
        try {
            if (in != null) in.close();
        } catch (Exception e) { /* ignore */ }
        try {
            if (out != null) out.close();
        } catch (Exception e) { /* ignore */ }
        try {
            if (socket != null) socket.close();
        } catch (Exception e) { /* ignore */ }
    }
}