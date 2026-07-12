package ru.otus.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryAuthenticatedProvider implements AuthenticatedProvider {
    private class User {
        public String login;
        public String password;
        public String username;
        public String role;

        public User(String login, String password, String username, String role) {
            this.login = login;
            this.password = password;
            this.username = username;
            this.role = role;
        }
    }

    private List<User> users;
    private Server server;

    public InMemoryAuthenticatedProvider(Server server) {
        this.server = server;
        this.users = new CopyOnWriteArrayList<>();
        users.add(new User("qwe", "qwe", "qwe", "USER"));
        users.add(new User("asd", "asd", "asd", "USER"));
        users.add(new User("zxc", "zxc", "zxc", "ADMIN"));
    }


    @Override
    public void init() {
        System.out.println("Сервис аутентификации запущен в режиме InMemory");
    }

    private String getUsernameByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.username;
            }
        }
        return null;
    }
    private String getUserRole(String login) {
        for (User user : users) {
            if (user.login.equals(login)) {
                return user.role;
            }
        }
        return null;
    }

    private boolean isLoginAlreadyExists(String login) {
        for (User user : users) {
            if (user.login.equals(login)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        String authUsername = getUsernameByLoginAndPassword(login, password);

        if (authUsername == null) {
            clientHandler.sendMessage("Неверный логин/пароль");
            return false;
        }
        if (server.isUsernameBusy(authUsername)) {
            clientHandler.sendMessage("Данная учетная запись уже используется");
            return false;
        }
        clientHandler.setUsername(authUsername);
        clientHandler.setRole(getUserRole(login));
        server.subscribe(clientHandler);
        clientHandler.sendMessage("/authok " + authUsername);

        return true;
    }

    @Override
    public boolean register(ClientHandler clientHandler, String login, String password, String username, String role) {
        if (login.length() < 3) {
            clientHandler.sendMessage("Логин должен быть 3+ символа");
            return false;
        }
        if (password.length() < 3) {
            clientHandler.sendMessage("Пароль должен быть 3+ символа");
            return false;
        }
        if (username.length() < 3) {
            clientHandler.sendMessage("Имя пользователя должна быть 3+ символа");
            return false;
        }
        if (isLoginAlreadyExists(login)) {
            clientHandler.sendMessage("Указанный логин уже занят");
            return false;
        }


        users.add(new User(login, password, username, role));
        clientHandler.setUsername(username);
        clientHandler.setRole(role);
        server.subscribe(clientHandler);
        clientHandler.sendMessage("/regok " + username);

        return true;
    }
}
