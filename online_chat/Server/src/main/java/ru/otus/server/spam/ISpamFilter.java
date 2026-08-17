package ru.otus.server.spam;

public interface ISpamFilter {
    boolean checkAndUpdate(String username);
}