package ru.otus.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5433/javadb?currentSchema=javadb";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер PostgreSQL успешно загружен.");
        } catch (ClassNotFoundException e) {
            System.err.println("Не удалось загрузить драйвер PostgreSQL!");
            e.printStackTrace();
            throw new RuntimeException("Драйвер PostgreSQL не найден в classpath", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    public static void releaseConnection(Connection connection) {
        closeQuietly(connection);
    }
}