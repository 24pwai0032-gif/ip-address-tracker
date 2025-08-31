package ui.admin;

import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminLogsPanel extends JFrame {
    JTable table;
    JComboBox<String> logType;

    public AdminLogsPanel() {
        setTitle("Admin - View Logs");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Select Log:");
        logType = new JComboBox<>(new String[]{"ip_search_history", "alerts", "api_requests"});
        JButton loadBtn = new JButton("Load Logs");

        loadBtn.setBackground(Color.BLACK);
        loadBtn.setForeground(Color.WHITE);

        topPanel.add(label);
        topPanel.add(logType);
        topPanel.add(loadBtn);

        add(topPanel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> loadLogs());

        setVisible(true);
    }

    private void loadLogs() {
        String selectedTable = (String) logType.getSelectedItem();
        DefaultTableModel model = new DefaultTableModel();
        try (Connection con = DatabaseConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
            ResultSetMetaData md = rs.getMetaData();

            // Set columns
            int colCount = md.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                model.addColumn(md.getColumnName(i));
            }

            // Set rows
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}