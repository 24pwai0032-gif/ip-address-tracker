package ui.user;

import db.DatabaseConnection;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {

    private int userId;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public UserDashboard(int userId) {
        this.userId = userId;
        setTitle("📊 User Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(new Color(230, 230, 250));

        JLabel title = UIUtils.createTitleLabel("📊 Your IP Search History");
        JButton refreshBtn = UIUtils.createButton("🔄 Refresh");
        JButton lookupBtn = UIUtils.createButton("🔍 New Lookup");
        JButton logoutBtn = UIUtils.createBackButton("⬅ Logout", e -> {
            dispose();
            new main.WelcomeScreen();
        });

        headerPanel.add(title);
        headerPanel.add(refreshBtn);
        headerPanel.add(lookupBtn);
        headerPanel.add(logoutBtn);

        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // Table for history
        tableModel = new DefaultTableModel(
                new String[]{"IP Address", "City", "Country", "ISP", "Searched At"}, 0
        ) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        historyTable = new JTable(tableModel);
        UIUtils.styleTable(historyTable);

        getContentPane().add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // Button actions
        refreshBtn.addActionListener(e -> loadHistory());
        lookupBtn.addActionListener(e -> new IPLookupForm(userId));

        // Initial load
        loadHistory();
        setVisible(true);
    }

    private void loadHistory() {
        tableModel.setRowCount(0); // clear table
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ish.ip_address, ic.city, ic.country, ic.isp, ish.searched_at " +
                            "FROM ip_search_history ish " +
                            "JOIN ip_cache ic ON ish.ip_address = ic.ip_address " +
                            "WHERE ish.user_id = ? " +
                            "ORDER BY ish.searched_at DESC"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("ip_address"),
                        rs.getString("city"),
                        rs.getString("country"),
                        rs.getString("isp"),
                        rs.getString("searched_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
