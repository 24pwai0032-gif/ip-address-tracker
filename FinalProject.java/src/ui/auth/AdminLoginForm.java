package ui.auth;

import main.WelcomeScreen;
import services.AuthService;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AdminLoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginForm() {
        setTitle("👑 Admin Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ✅ Back button (fixed for new UIUtils)
        JButton backBtn = UIUtils.createBackButton("⬅ Back to Welcome", e -> {
            dispose();
            new WelcomeScreen();
        });
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(backBtn, gbc);

        // Title
        JLabel title = UIUtils.createTitleLabel("Admin Login");
        gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(title, gbc);

        // Username
        usernameField = new JTextField();
        usernameField.setFont(UIUtils.BODY_FONT);
        usernameField.setToolTipText("Enter admin username");
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(usernameField, gbc);

        // Password
        passwordField = new JPasswordField();
        passwordField.setFont(UIUtils.BODY_FONT);
        passwordField.setToolTipText("Enter admin password");
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(passwordField, gbc);

        // Show password
        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setBackground(UIUtils.BACKGROUND_COLOR);
        showPass.addActionListener(e -> passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '•'));
        gbc.gridy = 4; gbc.gridwidth = 2;
        mainPanel.add(showPass, gbc);

        // Login button
        JButton loginBtn = UIUtils.createButton("Login", UIUtils.PRIMARY_COLOR);
        loginBtn.addActionListener(e -> performLogin());
        gbc.gridy = 5; gbc.gridwidth = 2;
        mainPanel.add(loginBtn, gbc);

        // Press Enter to login
        passwordField.addActionListener(e -> performLogin());

        add(mainPanel);
        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (AuthService.adminLogin(username, password)) {
            dispose();
            new ui.admin.AdminDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
