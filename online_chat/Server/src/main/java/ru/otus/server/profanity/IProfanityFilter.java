package ru.otus.server.profanity;

public interface IProfanityFilter {
    String filter(String message);
}