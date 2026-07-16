package otus.test;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::closeConnection));

        List<User> allUsers = getAll();
        System.out.println("=== Все пользователи и их роли ===");
        for (User user : allUsers) {
            System.out.printf("User [%d] %s, roles: ", user.getId(), user.getEmail());
            user.getRoles().forEach(r -> System.out.print(r.getName() + " "));
            System.out.println();
        }

        System.out.println("\n=== Проверка администраторов ===");
        for (int id = 1; id <= 5; id++) {
            System.out.printf("User %d is admin? %b%n", id, isAdmin(id));
        }
    }

    public static List<User> getAll() {
        List<User> users = new ArrayList<>();
        Map<Integer, User> userMap = new LinkedHashMap<>();

        String sql = "SELECT u.id, u.email, u.password, r.id AS role_id, r.name AS role_name " +
                "FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                "LEFT JOIN roles r ON ur.role_id = r.id " +
                "ORDER BY u.id";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("id");
                User user = userMap.get(userId);
                if (user == null) {
                    user = new User(userId,
                            rs.getString("email"),
                            rs.getString("password"));
                    userMap.put(userId, user);
                }
                int roleId = rs.getInt("role_id");
                if (!rs.wasNull()) {
                    Role role = new Role(roleId, rs.getString("role_name"));
                    user.getRoles().add(role);
                }
            }
            users.addAll(userMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean isAdmin(int userId) {
        String sql = "SELECT EXISTS (" +
                "SELECT 1 FROM user_roles ur " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE ur.user_id = ? AND r.name = 'ADMIN'" +
                ")";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}