package main;

import utils.UIUtils;
import ui.auth.AdminLoginForm;
import ui.auth.UserLoginForm;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("IP Address Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);

        // Gradient background panel
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, UIUtils.PRIMARY_COLOR,
                        0, getHeight(), new Color(3, 169, 244)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 16, 16, 16);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = UIUtils.createTitleLabel("🌍 IP Address Tracker");
        title.setForeground(Color.WHITE); // override for dark background
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        gbc.gridy = 0;
        bg.add(title, gbc);

        // Spacer
        gbc.gridy = 1;
        bg.add(Box.createVerticalStrut(8), gbc);

        // Admin button
        JButton adminBtn = UIUtils.createButton("👑 Admin", UIUtils.PRIMARY_COLOR.darker());
        adminBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        adminBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new AdminLoginForm());
        });
        gbc.gridy = 2;
        bg.add(adminBtn, gbc);

        // User button
        JButton userBtn = UIUtils.createButton("👤 User", UIUtils.SECONDARY_COLOR);
        userBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new UserLoginForm());
        });
        gbc.gridy = 3;
        bg.add(userBtn, gbc);

        // Footer
        JLabel footer = new JLabel("Developed by Syed Hassan Tayyab", SwingConstants.CENTER);
        footer.setForeground(new Color(230, 240, 250));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 4;
        bg.add(footer, gbc);

        add(bg);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeScreen::new);
    }
}
