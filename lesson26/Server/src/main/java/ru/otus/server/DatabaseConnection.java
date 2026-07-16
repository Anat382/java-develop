package ru.otus.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5433/javadb?currentSchema=otus_java";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер PostgreSQL загружен.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Драйвер PostgreSQL не найден", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}