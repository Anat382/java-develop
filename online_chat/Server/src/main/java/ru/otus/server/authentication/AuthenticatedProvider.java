package ru.otus.server.authentication;

import ru.otus.server.ClientHandler;

public interface AuthenticatedProvider {
    void init();
    boolean authenticate(ClientHandler clientHandler, String login, String password);
    boolean register(ClientHandler clientHandler, String login, String password,
                     String username, String role);
    boolean changeNickname(String oldName, String newName);
    void updateLastActivity(String username);
    String getLastActivity(String username);
    boolean rateUser(String fromUser, String toUser, int value);
    int getUserRating(String username);
}