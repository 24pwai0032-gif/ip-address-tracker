package ui.admin;

import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminAlertsPanel extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public AdminAlertsPanel() {
        setTitle("🚨 Admin Alerts");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 250, 255));
        setLayout(null);

        JLabel title = new JLabel("🚨 Security Alerts", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(250, 20, 300, 30);
        add(title);

        // Table
        model = new DefaultTableModel(new String[]{"Username", "IP Address", "Message", "Time"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 80, 700, 250);
        add(scrollPane);

        // Refresh Button
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBounds(300, 350, 180, 40);
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> loadAlerts());
        add(refreshBtn);

        loadAlerts();
        setVisible(true);
    }

    public void loadAlerts() {
        model.setRowCount(0); // clear table

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, a.ip_address, a.alert_message, a.created_at " +
                            "FROM alerts a JOIN users u ON a.user_id = u.id " +
                            "ORDER BY a.created_at DESC"
            );

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("ip_address"),
                        rs.getString("alert_message"),
                        rs.getString("created_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading alerts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
