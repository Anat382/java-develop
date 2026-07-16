package ru.otus.server;

public interface AuthenticatedProvider {
    void init();
    boolean authenticate(ClientHandler clientHandler, String login, String password);
    boolean register(ClientHandler clientHandler, String login, String password, String username, String role);
}
