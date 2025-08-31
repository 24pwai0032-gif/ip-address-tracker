package main;

import db.DatabaseConnection;
import ui.admin.AdminDashboard;
import ui.user.UserDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginForm() {
        setTitle("IP Tracker Login");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setBounds(100, 20, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 25);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 170, 100, 30);
        loginBtn.setBackground(Color.BLUE);
        loginBtn.setForeground(Color.WHITE);

        add(title);
        add(userLabel);
        add(passLabel);
        add(usernameField);
        add(passwordField);
        add(loginBtn);

        loginBtn.addActionListener(e -> loginUser());
        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = DatabaseConnection.getConnection()) {
            String sql = "SELECT u.id, u.username, u.password, r.name AS role " +
                    "FROM users u JOIN roles r ON u.role_id = r.id " +
                    "WHERE u.username = ? AND u.password = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + role);

                if (role.equalsIgnoreCase("admin")) {
                    new AdminDashboard();
                } else {
                    new UserDashboard(rs.getInt("id"));
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}