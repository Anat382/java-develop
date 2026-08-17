package ru.otus.server.room;

import ru.otus.server.ClientHandler;
import ru.otus.server.Server;
import ru.otus.server.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager implements IRoomManager {
    private final Server server;
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public RoomManager(Server server) {
        this.server = server;
        loadRoomsFromDB();
    }

    private void loadRoomsFromDB() {
        String sql = "SELECT name, owner_id, password, created_at, last_activity FROM rooms";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                int ownerId = rs.getInt("owner_id");
                String password = rs.getString("password");
                Timestamp created = rs.getTimestamp("created_at");
                Timestamp last = rs.getTimestamp("last_activity");
                Room room = new Room(name, ownerId, password, created, last);
                loadMembers(room);
                rooms.put(name, room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMembers(Room room) {
        String sql = "SELECT u.username FROM room_members rm JOIN users u ON rm.user_id = u.id WHERE rm.room_id = (SELECT id FROM rooms WHERE name = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    room.addMember(rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getUserId(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        return -1;
    }

    @Override
    public boolean createRoom(String name, String ownerUsername, String password) {
        String countSql = "SELECT COUNT(*) FROM rooms WHERE owner_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(countSql)) {
            ps.setString(1, ownerUsername);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) >= 5) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "INSERT INTO rooms (name, owner_id, password) VALUES (?, (SELECT id FROM users WHERE username = ?), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, ownerUsername);
            ps.setString(3, password);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                Room room = new Room(name,
                        getUserId(ownerUsername),
                        password,
                        Timestamp.valueOf(LocalDateTime.now()),
                        Timestamp.valueOf(LocalDateTime.now()));
                rooms.put(name, room);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean joinRoom(String roomName, ClientHandler client, String password) {
        Room room = rooms.get(roomName);
        if (room == null) return false;
        if (room.getPassword() != null && !room.getPassword().equals(password)) return false;

        if (client.getCurrentRoom() != null && !client.getCurrentRoom().equals("общая")) {
            leaveRoom(client);
        }

        String sql = "INSERT INTO room_members (room_id, user_id) VALUES ((SELECT id FROM rooms WHERE name = ?), (SELECT id FROM users WHERE username = ?)) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomName);
            ps.setString(2, client.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        room.addMember(client.getUsername());
        client.setCurrentRoom(roomName);
        sendHistory(roomName, client);
        return true;
    }

    private void sendHistory(String roomName, ClientHandler client) {
        String sql = "SELECT username, message, sent_at FROM room_messages WHERE room_id = (SELECT id FROM rooms WHERE name = ?) ORDER BY sent_at DESC LIMIT 20";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomName);
            try (ResultSet rs = ps.executeQuery()) {
                List<String> history = new ArrayList<>();
                while (rs.next()) {
                    String msg = String.format("[%s] %s: %s",
                            rs.getTimestamp("sent_at").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                            rs.getString("username"),
                            rs.getString("message"));
                    history.add(msg);
                }
                Collections.reverse(history);
                client.sendMessage("--- История комнаты " + roomName + " ---");
                for (String line : history) {
                    client.sendMessage(line);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveRoom(ClientHandler client) {
        String roomName = client.getCurrentRoom();
        if (roomName == null || roomName.equals("общая")) return;
        Room room = rooms.get(roomName);
        if (room != null) {
            room.removeMember(client.getUsername());
            String sql = "DELETE FROM room_members WHERE room_id = (SELECT id FROM rooms WHERE name = ?) AND user_id = (SELECT id FROM users WHERE username = ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, roomName);
                ps.setString(2, client.getUsername());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        client.setCurrentRoom("общая");
    }

    @Override
    public void sendMessageToRoom(String roomName, String sender, String message) {
        if (roomName == null || roomName.equals("общая")) {
            server.broadcastMessage(sender, message);
            return;
        }
        Room room = rooms.get(roomName);
        if (room == null) {
            server.broadcastMessage(sender, message);
            return;
        }

        saveMessage(roomName, sender, message);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formatted = String.format("[%s] [%s] %s: %s", timestamp, roomName, sender, message);

        for (String member : room.getMembers()) {
            ClientHandler ch = server.getClientByUsername(member);
            if (ch != null) {
                ch.sendMessage(formatted);
            }
        }
    }

    private void saveMessage(String roomName, String sender, String message) {
        String sql = "INSERT INTO room_messages (room_id, user_id, username, message) VALUES ((SELECT id FROM rooms WHERE name = ?), (SELECT id FROM users WHERE username = ?), ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomName);
            ps.setString(2, sender);
            ps.setString(3, sender);
            ps.setString(4, message);
            ps.executeUpdate();
            String upd = "UPDATE rooms SET last_activity = CURRENT_TIMESTAMP WHERE name = ?";
            try (PreparedStatement pu = conn.prepareStatement(upd)) {
                pu.setString(1, roomName);
                pu.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRoomListForClient() {
        if (rooms.isEmpty()) return "Нет созданных комнат.";
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms.values()) {
            sb.append("- ").append(room.getName())
                    .append(" (участников: ").append(room.getMembersCount()).append(")");
            if (room.getPassword() != null) sb.append(" [защищена паролем]");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getRoomInfo(String roomName) {
        Room room = rooms.get(roomName);
        if (room == null) return "Комната не найдена.";
        String ownerName;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE id = ?")) {
            ps.setInt(1, room.getOwnerId());
            try (ResultSet rs = ps.executeQuery()) {
                ownerName = rs.next() ? rs.getString("username") : "неизвестно";
            }
        } catch (SQLException e) {
            ownerName = "неизвестно";
        }

        return "Название: " + room.getName() +
                "\nВладелец: " + ownerName +
                "\nСоздана: " + room.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) +
                "\nПоследняя активность: " + room.getLastActivity().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) +
                "\nУчастников: " + room.getMembersCount() +
                "\nЗащищена паролем: " + (room.getPassword() != null ? "Да" : "Нет");
    }

    @Override
    public void cleanupOldRooms() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        for (Room room : rooms.values()) {
            if (room.getLastActivity().toLocalDateTime().isBefore(weekAgo)) {
                String sql = "DELETE FROM rooms WHERE name = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, room.getName());
                    ps.executeUpdate();
                    rooms.remove(room.getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateNickname(String oldNick, String newNick) {
        for (Room room : rooms.values()) {
            if (room.getMembers().contains(oldNick)) {
                room.removeMember(oldNick);
                room.addMember(newNick);
            }
        }
        String sql = "UPDATE room_members SET user_id = (SELECT id FROM users WHERE username = ?) WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newNick);
            ps.setString(2, oldNick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class Room {
        private final String name;
        private final int ownerId;
        private final String password;
        private final Timestamp createdAt;
        private Timestamp lastActivity;
        private final Set<String> members = new HashSet<>();

        public Room(String name, int ownerId, String password, Timestamp createdAt, Timestamp lastActivity) {
            this.name = name;
            this.ownerId = ownerId;
            this.password = password;
            this.createdAt = createdAt;
            this.lastActivity = lastActivity;
        }

        public String getName() { return name; }
        public int getOwnerId() { return ownerId; }
        public String getPassword() { return password; }
        public Timestamp getCreatedAt() { return createdAt; }
        public Timestamp getLastActivity() { return lastActivity; }
        public Set<String> getMembers() { return members; }
        public int getMembersCount() { return members.size(); }
        public void addMember(String username) { members.add(username); }
        public void removeMember(String username) { members.remove(username); }
        public void setLastActivity(Timestamp lastActivity) { this.lastActivity = lastActivity; }
    }
}