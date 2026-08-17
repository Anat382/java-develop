package ru.otus.server.ban;

import ru.otus.server.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BanManager implements IBanManager {
    @Override
    public boolean isBanned(String username) {
        String sql = "SELECT ban_until FROM bans b " +
                "JOIN users u ON b.user_id = u.id " +
                "WHERE u.username = ? AND (b.ban_until IS NULL OR b.ban_until > CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isBannedByLogin(String login) {
        String findUserSql = "SELECT username FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(findUserSql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return isBanned(rs.getString("username"));
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void banUser(String username, String adminName, int minutes) {
        String sql = "INSERT INTO bans (user_id, admin_id, reason, ban_until) " +
                "SELECT u.id, a.id, ?, ? FROM users u, users a " +
                "WHERE u.username = ? AND a.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "Забанен администратором " + adminName);
            if (minutes > 0) {
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(minutes)));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }
            ps.setString(3, username);
            ps.setString(4, adminName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean unbanUser(String username) {
        String sql = "DELETE FROM bans b " +
                "USING users u " +
                "WHERE b.user_id = u.id AND u.username = ? " +
                "AND (b.ban_until IS NULL OR b.ban_until > CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getBanInfo(String username) {
        String sql = "SELECT b.reason, b.ban_until, a.username AS admin " +
                "FROM bans b " +
                "JOIN users u ON b.user_id = u.id " +
                "LEFT JOIN users a ON b.admin_id = a.id " +
                "WHERE u.username = ? AND (b.ban_until IS NULL OR b.ban_until > CURRENT_TIMESTAMP) " +
                "ORDER BY b.created_at DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String reason = rs.getString("reason");
                    Timestamp until = rs.getTimestamp("ban_until");
                    String admin = rs.getString("admin");
                    String untilStr = (until == null) ? "перманентно" :
                            until.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                    return "Причина: " + reason + ", до: " + untilStr + ", администратор: " + admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}