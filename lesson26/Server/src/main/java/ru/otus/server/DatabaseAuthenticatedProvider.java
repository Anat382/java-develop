package ru.otus.server;

import java.sql.*;

public class DatabaseAuthenticatedProvider implements AuthenticatedProvider {
    private Server server;

    public DatabaseAuthenticatedProvider(Server server) {
        this.server = server;
    }

    @Override
    public void init() {
        System.out.println("Сервис аутентификации запущен в режиме PostgreSQL");
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Соединение с БД установлено.");
        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        String sql = "SELECT u.username, r.name AS role " +
                "FROM users u " +
                "JOIN user_roles ur ON u.id = ur.user_id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.username = ? AND u.password = ? " +
                "ORDER BY CASE WHEN r.name = 'ADMIN' THEN 1 ELSE 2 END " +
                "LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String role = rs.getString("role");

                    if (server.isUsernameBusy(username)) {
                        clientHandler.sendMessage("Данная учетная запись уже используется");
                        return false;
                    }

                    clientHandler.setUsername(username);
                    clientHandler.setRole(role);
                    server.subscribe(clientHandler);
                    clientHandler.sendMessage("/authok " + username);
                    return true;
                } else {
                    clientHandler.sendMessage("Неверный логин/пароль");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clientHandler.sendMessage("Ошибка сервера при аутентификации");
            return false;
        }
    }

    @Override
    public boolean register(ClientHandler clientHandler, String login, String password,
                            String username, String role) {
        if (login.length() < 3) {
            clientHandler.sendMessage("Логин должен быть 3+ символа");
            return false;
        }
        if (password.length() < 3) {
            clientHandler.sendMessage("Пароль должен быть 3+ символа");
            return false;
        }
        if (username.length() < 3) {
            clientHandler.sendMessage("Имя пользователя должно быть 3+ символа");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String checkSql = "SELECT email, username FROM users WHERE email = ? OR username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, login);
                checkStmt.setString(2, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("email").equals(login)) {
                            clientHandler.sendMessage("Указанный логин уже занят");
                        } else {
                            clientHandler.sendMessage("Указанное имя пользователя уже занято");
                        }
                        return false;
                    }
                }
            }

            int roleId;
            String roleSql = "SELECT id FROM roles WHERE name = ?";
            try (PreparedStatement roleStmt = conn.prepareStatement(roleSql)) {
                roleStmt.setString(1, role);
                try (ResultSet rs = roleStmt.executeQuery()) {
                    if (rs.next()) {
                        roleId = rs.getInt("id");
                    } else {
                        clientHandler.sendMessage("Указанная роль не существует");
                        return false;
                    }
                }
            }

            String insertUser = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, login);
                userStmt.setString(2, password);
                userStmt.setString(3, username);
                int affected = userStmt.executeUpdate();
                if (affected == 0) {
                    clientHandler.sendMessage("Ошибка регистрации");
                    return false;
                }

                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        // 4. Вставка связи с ролью
                        String insertRole = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                        try (PreparedStatement roleInsert = conn.prepareStatement(insertRole)) {
                            roleInsert.setInt(1, userId);
                            roleInsert.setInt(2, roleId);
                            roleInsert.executeUpdate();
                        }
                    } else {
                        clientHandler.sendMessage("Ошибка регистрации (не удалось получить ID)");
                        return false;
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            try {

            } catch (Exception ex) {

            }
            e.printStackTrace();
            clientHandler.sendMessage("Ошибка сервера при регистрации");
            return false;
        }

        clientHandler.setUsername(username);
        clientHandler.setRole(role);
        server.subscribe(clientHandler);
        clientHandler.sendMessage("/regok " + username);
        return true;
    }
}