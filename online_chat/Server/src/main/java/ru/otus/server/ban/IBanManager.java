package ru.otus.server.ban;

public interface IBanManager {
    boolean isBanned(String username);
    boolean isBannedByLogin(String login);
    void banUser(String username, String adminName, int minutes);
    boolean unbanUser(String username);
    String getBanInfo(String username);
}