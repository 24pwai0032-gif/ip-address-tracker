package services;

import db.DatabaseConnection;
import java.sql.*;

public class AuthService {

    public static boolean registerUser(String username, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int roleId = role.equalsIgnoreCase("admin") ? 1 : 2;

            // Debug
            System.out.println("Trying to register user: " + username + " with role: " + role + " (role_id = " + roleId + ")");

            // Check if user already exists
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists: " + username);
                return false;
            }

            // Insert user
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, roleId);
            stmt.executeUpdate();

            System.out.println("User registered successfully: " + username);
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static int loginUser(String username, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int roleId = role.equalsIgnoreCase("admin") ? 1 : 2;

            System.out.println("Trying to log in user: " + username + " with role: " + role);

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ? AND password = ? AND role_id = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, roleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("Login successful. User ID: " + userId);
                return userId;
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ NEW — Admin login method (boolean)
    public static boolean adminLogin(String username, String password) {
        return loginUser(username, password, "admin") != -1;
    }

    // ✅ NEW — User login method (boolean)
    public static boolean userLogin(String username, String password) {
        return loginUser(username, password, "user") != -1;
    }
}
