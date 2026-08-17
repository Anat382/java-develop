package ru.otus.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private String username;
    private String role;
    private boolean authenticated;
    private long lastActivityTime;
    private String currentRoom;

    public ClientHandler(Socket socket, Server server) throws Exception {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.lastActivityTime = System.currentTimeMillis();
        this.currentRoom = "общая";

        new Thread(() -> {
            try {
                System.out.println("Клиент подключился: " + socket.getPort());
                while (true) {
                    sendMessage("Для входа: /auth login password\nДля регистрации: /reg login password username role\n" +
                            "Дополнительные команды: /help");
                    String message;
                    try {
                        message = in.readUTF();
                    } catch (SocketException | EOFException e) {
                        System.out.println("Клиент " + socket.getPort() + " разорвал соединение при аутентификации");
                        return;
                    }

                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMessage("/exitok");
                            return;
                        }
                        if (message.startsWith("/auth ")) {
                            String[] token = message.split(" ");
                            if (token.length != 3) {
                                sendMessage("Неверный формат. Используйте: /auth username password");
                                continue;
                            }
                            if (server.getBanManager().isBannedByLogin(token[1])) {
                                sendMessage("Вы забанены. Обратитесь к администратору.");
                                continue;
                            }
                            if (server.getAuthenticatedProvider().authenticate(this, token[1], token[2])) {
                                authenticated = true;
                                lastActivityTime = System.currentTimeMillis();
                                server.subscribe(this);
                                break;
                            }
                        } else if (message.startsWith("/reg ")) {
                            String[] token = message.split(" ");
                            if (token.length != 5) {
                                sendMessage("Неверный формат. Используйте: /reg login password username role");
                                continue;
                            }
                            if (server.getAuthenticatedProvider().register(this, token[1], token[2], token[3], token[4])) {
                                authenticated = true;
                                lastActivityTime = System.currentTimeMillis();
                                server.subscribe(this);
                                break;
                            }
                        } else {
                            sendMessage("Для начала авторизуйтесь или зарегистрируйтесь.");
                        }
                    }
                }

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

                    lastActivityTime = System.currentTimeMillis();
                    server.getAuthenticatedProvider().updateLastActivity(username);

                    if (!server.getSpamFilter().checkAndUpdate(username)) {
                        sendMessage("Превышен лимит сообщений. Подождите немного.");
                        continue;
                    }

                    message = server.getProfanityFilter().filter(message);

                    if (message.startsWith("/")) {
                        handleCommand(message);
                    } else {
                        server.getRoomManager().sendMessageToRoom(currentRoom, username, message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    private void handleCommand(String message) {
        String[] parts = message.split(" ");
        String cmd = parts[0];

        switch (cmd) {
            case "/exit":
                sendMessage("/exitok");
                authenticated = false;
                break;

            case "/w":
                if (parts.length < 3) {
                    sendMessage("Используйте: /w username message");
                    return;
                }
                String target = parts[1];
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < parts.length; i++) sb.append(parts[i]).append(" ");
                server.sendPrivateMessage(username, target, sb.toString().trim());
                break;

            case "/changenick":
                if (parts.length != 2) {
                    sendMessage("Используйте: /changenick newNick");
                    return;
                }
                String newNick = parts[1];
                if (server.getAuthenticatedProvider().changeNickname(username, newNick)) {
                    String oldNick = username;
                    server.getRoomManager().updateNickname(oldNick, newNick);
                    username = newNick;
                    server.broadcastMessage("admin", oldNick + " сменил ник на " + newNick);
                    sendMessage("Ник успешно изменён на " + newNick);
                } else {
                    sendMessage("Не удалось сменить ник. Возможно, такой ник уже занят.");
                }
                break;

            case "/activelist":
                server.sendActiveList(this);
                break;

            case "/ban":
                if (!"ADMIN".equals(role)) {
                    sendMessage("У вас нет прав для этой команды.");
                    return;
                }
                if (parts.length < 2) {
                    sendMessage("Используйте: /ban username [minutes]");
                    return;
                }
                String banUser = parts[1];
                int banMinutes = -1;
                if (parts.length == 3) {
                    try {
                        banMinutes = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        sendMessage("Неверный формат времени. Используйте число минут.");
                        return;
                    }
                }
                if (banUser.equals(username)) {
                    sendMessage("Нельзя забанить самого себя.");
                    return;
                }
                server.getBanManager().banUser(banUser, username, banMinutes);
                ClientHandler targetClient = server.getClientByUsername(banUser);
                if (targetClient != null) {
                    targetClient.sendMessage("/kickok Вы были забанены администратором " + username);
                    targetClient.disconnect();
                }
                sendMessage("Пользователь " + banUser + " забанен" +
                        (banMinutes > 0 ? " на " + banMinutes + " минут" : " перманентно"));
                break;

            case "/shutdown":
                if (!"ADMIN".equals(role)) {
                    sendMessage("У вас нет прав для этой команды.");
                    return;
                }
                server.shutdown();
                break;

            case "/createroom":
                if (parts.length < 2) {
                    sendMessage("Используйте: /createroom roomName [password]");
                    return;
                }
                String roomName = parts[1];
                String password = parts.length > 2 ? parts[2] : null;
                if (server.getRoomManager().createRoom(roomName, username, password)) {
                    sendMessage("Комната " + roomName + " создана.");
                    currentRoom = roomName;
                    server.getRoomManager().joinRoom(roomName, this, password);
                } else {
                    sendMessage("Не удалось создать комнату. Возможно, имя занято или достигнут лимит (макс 5 комнат).");
                }
                break;

            case "/enter":
                if (parts.length < 2) {
                    sendMessage("Используйте: /enter roomName [password]");
                    return;
                }
                String enterRoom = parts[1];
                String pass = parts.length > 2 ? parts[2] : null;
                if (server.getRoomManager().joinRoom(enterRoom, this, pass)) {
                    currentRoom = enterRoom;
                    sendMessage("Вы вошли в комнату " + enterRoom);
                } else {
                    sendMessage("Не удалось войти в комнату. Проверьте имя и пароль.");
                }
                break;

            case "/leaveroom":
                if (currentRoom == null || currentRoom.equals("общая")) {
                    sendMessage("Вы уже в общей комнате.");
                    return;
                }
                server.getRoomManager().leaveRoom(this);
                currentRoom = "общая";
                sendMessage("Вы вернулись в общую комнату.");
                break;

            case "/listrooms":
                String list = server.getRoomManager().getRoomListForClient();
                sendMessage("Список комнат:\n" + list);
                break;

            case "/roominfo":
                if (parts.length < 2) {
                    sendMessage("Используйте: /roominfo roomName");
                    return;
                }
                String info = server.getRoomManager().getRoomInfo(parts[1]);
                sendMessage(info);
                break;

            case "/like":
                if (parts.length != 2) {
                    sendMessage("Используйте: /like username");
                    return;
                }
                if (server.getAuthenticatedProvider().rateUser(username, parts[1], 1)) {
                    sendMessage("Вы поставили лайк пользователю " + parts[1]);
                } else {
                    sendMessage("Не удалось поставить лайк. Возможно, вы уже оценили этого пользователя.");
                }
                break;

            case "/dislike":
                if (parts.length != 2) {
                    sendMessage("Используйте: /dislike username");
                    return;
                }
                if (server.getAuthenticatedProvider().rateUser(username, parts[1], -1)) {
                    sendMessage("Вы поставили дизлайк пользователю " + parts[1]);
                } else {
                    sendMessage("Не удалось поставить дизлайк.");
                }
                break;

            case "/rating":
                if (parts.length != 2) {
                    sendMessage("Используйте: /rating username");
                    return;
                }
                int rating = server.getAuthenticatedProvider().getUserRating(parts[1]);
                sendMessage("Рейтинг пользователя " + parts[1] + ": " + rating);
                break;

            case "/last_activity":
                if (parts.length != 2) {
                    sendMessage("Используйте: /last_activity username");
                    return;
                }
                String activity = server.getAuthenticatedProvider().getLastActivity(parts[1]);
                sendMessage("Последняя активность " + parts[1] + ": " + activity);
                break;

            case "/sendfile":
                if (parts.length < 3) {
                    sendMessage("Используйте: /sendfile [username] filename base64data (если username не указан, отправляется в комнату)");
                    return;
                }
                String targetUser = null;
                String filename;
                int b64Start;
                if (parts.length >= 4) {
                    targetUser = parts[1];
                    filename = parts[2];
                    b64Start = 3;
                } else {
                    filename = parts[1];
                    b64Start = 2;
                }
                StringBuilder b64 = new StringBuilder();
                for (int i = b64Start; i < parts.length; i++) b64.append(parts[i]);
                try {
                    byte[] data = Base64.getDecoder().decode(b64.toString());
                    sendMessage("Файл " + filename + " получен. Размер: " + data.length + " байт.");
                    if (targetUser != null) {
                        ClientHandler targetH = server.getClientByUsername(targetUser);
                        if (targetH != null) {
                            targetH.sendMessage("[Файл от " + username + "] " + filename + " (base64): " + b64.toString());
                            sendMessage("Файл отправлен пользователю " + targetUser);
                        } else {
                            sendMessage("Пользователь " + targetUser + " не найден");
                        }
                    } else {
                        server.getRoomManager().sendMessageToRoom(currentRoom, username,
                                "[Файл] " + filename + " (base64): " + b64.toString());
                    }
                } catch (IllegalArgumentException e) {
                    sendMessage("Некорректный base64.");
                }
                break;

            case "/help":
                sendMessage("Доступные команды:\n" +
                        "/auth login password - авторизация\n" +
                        "/reg login password username role - регистрация\n" +
                        "/w username message - личное сообщение\n" +
                        "/changenick newNick - смена ника\n" +
                        "/activelist - список активных\n" +
                        "/ban username [minutes] - бан (админ)\n" +
                        "/shutdown - остановка сервера (админ)\n" +
                        "/createroom roomName [password] - создать комнату\n" +
                        "/enter roomName [password] - войти в комнату\n" +
                        "/leaveroom - покинуть комнату\n" +
                        "/listrooms - список комнат\n" +
                        "/roominfo roomName - информация о комнате\n" +
                        "/like username - лайк\n" +
                        "/dislike username - дизлайк\n" +
                        "/rating username - рейтинг\n" +
                        "/last_activity username - последняя активность\n" +
                        "/sendfile [username] filename base64data - отправить файл (если username не указан, в комнату)\n" +
                        "/exit - выход");
                break;

            default:
                sendMessage("Неизвестная команда. Введите /help для списка команд.");
                break;
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            System.err.println("Не удалось отправить сообщение клиенту " + username);
        }
    }

    public void disconnect() {
        server.unsubscribe(this, true);
        try { in.close(); } catch (Exception e) {}
        try { out.close(); } catch (Exception e) {}
        try { socket.close(); } catch (Exception e) {}
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isAuthenticated() { return authenticated; }
    public long getLastActivityTime() { return lastActivityTime; }
    public String getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(String room) { this.currentRoom = room; }
}