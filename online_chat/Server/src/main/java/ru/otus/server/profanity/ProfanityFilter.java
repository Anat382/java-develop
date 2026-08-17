package ru.otus.server.profanity;

import ru.otus.server.database.DatabaseConnection;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProfanityFilter implements IProfanityFilter {
    private Pattern pattern;
    private Set<String> badWords;

    public ProfanityFilter() {
        badWords = new HashSet<>();
        loadBadWords();
    }

    private void loadBadWords() {
        String sql = "SELECT word FROM bad_words";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<String> words = new ArrayList<>();
            while (rs.next()) {
                String word = rs.getString("word").trim().toLowerCase();
                if (!word.isEmpty()) {
                    words.add(word);
                    badWords.add(word);
                }
            }
            if (!words.isEmpty()) {
                String regex = words.stream()
                        .map(Pattern::quote)
                        .collect(Collectors.joining("|"));
                pattern = Pattern.compile("\\b(?:" + regex + ")\\b",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
            } else {
                pattern = null;
                System.out.println("Список плохих слов пуст, фильтр отключён.");
            }
        } catch (SQLException e) {
            System.err.println("Не удалось загрузить плохие слова: " + e.getMessage());
            pattern = null;
        }
    }

    @Override
    public String filter(String message) {
        if (message == null || pattern == null) {
            return message;
        }
        return pattern.matcher(message).replaceAll(mr -> "*".repeat(mr.group().length()));
    }

    public void reload() {
        loadBadWords();
    }

    public boolean containsBadWords(String message) {
        if (message == null || pattern == null) {
            return false;
        }
        return pattern.matcher(message).find();
    }
}