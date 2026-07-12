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

                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMessage("/exitok");
                            break;
                        }

                        // /auth login password
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
                        // /reg login password username
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
                    String message = in.readUTF();
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
//                            targetClient.disconnect();
                            server.broadcastMessage(username, " Пользователь " + targetUsername + " был отключен администратором " + username);
                            continue;
                        }

                    } else {
                        server.broadcastMessage(username, message);
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
