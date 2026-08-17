package ru.otus.server.authentication;

import ru.otus.server.ClientHandler;
import ru.otus.server.Server;
import ru.otus.server.database.DatabaseConnection;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DatabaseAuthenticatedProvider implements AuthenticatedProvider {
    private final Server server;

    public DatabaseAuthenticatedProvider(Server server) {
        this.server = server;
    }

    @Override
    public void init() {
        System.out.println("Сервис аутентификации запущен (PostgreSQL)");
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Подключение к БД установлено.");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        String sql = "SELECT u.id, u.username, r.name AS role " +
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

                    if (server.getBanManager().isBanned(username)) {
                        clientHandler.sendMessage("Вы забанены.");
                        return false;
                    }

                    if (server.isUsernameBusy(username)) {
                        clientHandler.sendMessage("Уже авторизован с этим именем.");
                        return false;
                    }

                    clientHandler.setUsername(username);
                    clientHandler.setRole(role);
                    updateLastActivity(username);
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
        if (login.length() < 3 || password.length() < 3 || username.length() < 3) {
            clientHandler.sendMessage("Логин, пароль и ник должны быть не менее 3 символов");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String checkSql = "SELECT email, username FROM users WHERE email = ? OR username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, login);
                ps.setString(2, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("email").equals(login))
                            clientHandler.sendMessage("Логин уже занят");
                        else
                            clientHandler.sendMessage("Имя пользователя уже занято");
                        return false;
                    }
                }
            }

            int roleId;
            String roleSql = "SELECT id FROM roles WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(roleSql)) {
                ps.setString(1, role);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        clientHandler.sendMessage("Указанная роль не существует");
                        return false;
                    }
                    roleId = rs.getInt("id");
                }
            }

            String insertUser = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, username);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    clientHandler.sendMessage("Ошибка регистрации");
                    return false;
                }
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int userId = keys.getInt(1);
                        String insertRole = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                        try (PreparedStatement pr = conn.prepareStatement(insertRole)) {
                            pr.setInt(1, userId);
                            pr.setInt(2, roleId);
                            pr.executeUpdate();
                        }
                    } else {
                        clientHandler.sendMessage("Ошибка регистрации (не удалось получить ID)");
                        return false;
                    }
                }
            }
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            clientHandler.sendMessage("Ошибка сервера при регистрации");
            return false;
        }

        clientHandler.setUsername(username);
        clientHandler.setRole(role);
        clientHandler.sendMessage("/regok " + username);
        return true;
    }

    @Override
    public boolean changeNickname(String oldName, String newName) {
        String sql = "UPDATE users SET username = ? WHERE username = ? " +
                "AND NOT EXISTS (SELECT 1 FROM users WHERE username = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, oldName);
            ps.setString(3, newName);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateLastActivity(String username) {
        String sql = "UPDATE users SET last_activity = CURRENT_TIMESTAMP WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLastActivity(String username) {
        String sql = "SELECT last_activity FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("last_activity");
                    return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "неизвестно";
    }

    @Override
    public boolean rateUser(String fromUser, String toUser, int value) {
        if (fromUser.equals(toUser)) return false;
        if (value != 1 && value != -1) return false;

        String sql = "INSERT INTO ratings (from_user_id, to_user_id, value) " +
                "SELECT u1.id, u2.id, ? FROM users u1, users u2 " +
                "WHERE u1.username = ? AND u2.username = ? " +
                "ON CONFLICT (from_user_id, to_user_id) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value);
            ps.setString(2, fromUser);
            ps.setString(3, toUser);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                updateUserRating(toUser);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateUserRating(String username) {
        String sql = "UPDATE users SET rating = (SELECT COALESCE(SUM(value), 0) FROM ratings WHERE to_user_id = users.id) WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserRating(String username) {
        String sql = "SELECT rating FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rating");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}