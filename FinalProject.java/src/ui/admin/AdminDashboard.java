package ui.admin;

import db.DatabaseConnection;
import services.ExportManager;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTable logTable, alertsTable;
    private DefaultTableModel logModel, alertModel;

    public AdminDashboard() {
        setTitle("👨‍💼 Admin Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Top navigation bar
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(new Color(230, 230, 250));

        JButton logsBtn = UIUtils.createButton("📜 View Logs");
        JButton alertsBtn = UIUtils.createButton("🚨 View Alerts");
        JButton exportPDFBtn = UIUtils.createButton("📄 Export All PDF");
        JButton exportCSVBtn = UIUtils.createButton("📑 Export All CSV");
        JButton backBtn = UIUtils.createBackButton("⬅ Logout", e -> {
            dispose();
            new main.WelcomeScreen();
        });

        navBar.add(logsBtn);
        navBar.add(alertsBtn);
        navBar.add(exportPDFBtn);
        navBar.add(exportCSVBtn);
        navBar.add(backBtn);

        getContentPane().add(navBar, BorderLayout.NORTH);

        // Panels
        JPanel logsPanel = createLogsPanel();
        JPanel alertsPanel = createAlertsPanel();

        mainPanel.add(logsPanel, "Logs");
        mainPanel.add(alertsPanel, "Alerts");

        // Button actions
        logsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Logs"));
        alertsBtn.addActionListener(e -> {
            loadAlerts();
            cardLayout.show(mainPanel, "Alerts");
        });
        exportPDFBtn.addActionListener(e -> ExportManager.exportAllSearchLogsToPDF());
        exportCSVBtn.addActionListener(e -> ExportManager.exportAllSearchLogsToCSV());

        loadLogs();
        setVisible(true);
    }

    // ----------------- LOGS PANEL -----------------
    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIUtils.BACKGROUND_COLOR
        );

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel title = UIUtils.createTitleLabel("📜 All Search Logs");
        topPanel.add(title, BorderLayout.WEST);

        JComboBox<String> userFilter = new JComboBox<>();
        userFilter.addItem("All Users");
        loadUserFilterOptions(userFilter);
        topPanel.add(userFilter, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        logModel = new DefaultTableModel(
                new String[]{"Username", "IP Address", "City", "Country", "ISP", "Searched At"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        logTable = new JTable(logModel);
        UIUtils.styleTable(logTable);
        panel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        logTable.setRowSorter(new TableRowSorter<>(logModel));

        userFilter.addActionListener(e -> {
            String selectedUser = (String) userFilter.getSelectedItem();
            if ("All Users".equals(selectedUser)) {
                loadLogs();
            } else {
                loadLogsByUser(selectedUser);
            }
        });

        return panel;
    }

    // ----------------- ALERTS PANEL -----------------
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIUtils.BACKGROUND_COLOR
        );

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel title = UIUtils.createTitleLabel("🚨 Threat Alerts");
        topPanel.add(title, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setToolTipText("Search alerts by username or IP...");
        searchField.setPreferredSize(new Dimension(250, 30));
        topPanel.add(searchField, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        alertModel = new DefaultTableModel(
                new String[]{"Username", "IP Address", "Alert Message", "Created At"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        alertsTable = new JTable(alertModel);
        UIUtils.styleTable(alertsTable);
        panel.add(new JScrollPane(alertsTable), BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(alertModel);
        alertsTable.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        return panel;
    }

    // ----------------- LOAD METHODS -----------------
    private void loadLogs() {
        logModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, ish.ip_address, ic.city, ic.country, ic.isp, ish.searched_at " +
                            "FROM ip_search_history ish " +
                            "JOIN users u ON ish.user_id = u.id " +
                            "JOIN ip_cache ic ON ish.ip_address = ic.ip_address " +
                            "ORDER BY ish.searched_at DESC"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logModel.addRow(new Object[]{
                        rs.getString("username"),
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

    private void loadLogsByUser(String username) {
        logModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, ish.ip_address, ic.city, ic.country, ic.isp, ish.searched_at " +
                            "FROM ip_search_history ish " +
                            "JOIN users u ON ish.user_id = u.id " +
                            "JOIN ip_cache ic ON ish.ip_address = ic.ip_address " +
                            "WHERE u.username = ? " +
                            "ORDER BY ish.searched_at DESC"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logModel.addRow(new Object[]{
                        rs.getString("username"),
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

    private void loadAlerts() {
        alertModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, a.ip_address, a.alert_message, a.created_at " +
                            "FROM alerts a JOIN users u ON a.user_id = u.id " +
                            "ORDER BY a.created_at DESC"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                alertModel.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("ip_address"),
                        rs.getString("alert_message"),
                        rs.getString("created_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserFilterOptions(JComboBox<String> comboBox) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT DISTINCT username FROM users ORDER BY username"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
