package ui.user;

import db.DatabaseConnection;
import org.json.JSONObject;
import services.ExportManager;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class IPLookupForm extends JFrame {

    private JTextField ipField;
    private JTextArea resultArea;
    private int userId;
    private JProgressBar progressBar;

    public IPLookupForm(int userId) {
        this.userId = userId;
        setTitle("🌍 IP Lookup");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // 🔹 Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(230, 230, 250));

        JButton backBtn = UIUtils.createBackButton("⬅ Dashboard", e -> {
            dispose();
            new UserDashboard(userId);
        });

        JLabel title = UIUtils.createTitleLabel("🔍 IP Address Lookup");

        topPanel.add(backBtn);
        topPanel.add(title);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // 🔹 Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        ipField = new JTextField(20);
        ipField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ipField.setToolTipText("Enter a valid IPv4 address");

        JButton searchBtn = UIUtils.createButton("Search");
        searchBtn.addActionListener(e -> performLookup());

        inputPanel.add(ipField);
        inputPanel.add(searchBtn);
        getContentPane().add(inputPanel, BorderLayout.CENTER);

        // 🔹 Progress Bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        // 🔹 Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 🔹 Export Buttons
        JPanel exportPanel = new JPanel(new FlowLayout());
        JButton exportPDF = UIUtils.createButton("📄 Export PDF");
        exportPDF.addActionListener(e -> ExportManager.exportSearchHistoryToPDF(userId));

        JButton exportCSV = UIUtils.createButton("📑 Export CSV");
        exportCSV.addActionListener(e -> ExportManager.exportSearchHistoryToCSV(userId));

        exportPanel.add(exportPDF);
        exportPanel.add(exportCSV);

        centerPanel.add(exportPanel, BorderLayout.SOUTH);
        getContentPane().add(centerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void performLookup() {
        String ip = ipField.getText().trim();
        if (!ip.matches("^(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")) {
            JOptionPane.showMessageDialog(this, "❌ Invalid IP format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            String output = "";
            @Override
            protected Void doInBackground() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                try (Connection conn = DatabaseConnection.getConnection()) {
                    PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM ip_cache WHERE ip_address = ?");
                    checkStmt.setString(1, ip);
                    ResultSet rs = checkStmt.executeQuery();

                    String city, country, isp;
                    if (rs.next()) {
                        city = rs.getString("city");
                        country = rs.getString("country");
                        isp = rs.getString("isp");
                        output = "📌 From Cache\n\nIP: " + ip + "\nCity: " + city + "\nCountry: " + country + "\nISP: " + isp;
                    } else {
                        // API Call
                        String url = "https://ipapi.co/" + ip + "/json/";
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("GET");

                        InputStream inputStream = connection.getInputStream();
                        StringBuilder response = new StringBuilder();
                        int ch;
                        while ((ch = inputStream.read()) != -1) response.append((char) ch);
                        inputStream.close();

                        JSONObject json = new JSONObject(response.toString());
                        city = json.optString("city", "N/A");
                        country = json.optString("country_name", "N/A");
                        isp = json.optString("org", "N/A");

                        output = "🌐 From API\n\nIP: " + ip + "\nCity: " + city + "\nCountry: " + country + "\nISP: " + isp;

                        PreparedStatement insertCache = conn.prepareStatement("INSERT INTO ip_cache VALUES (?, ?, ?, ?, NOW())");
                        insertCache.setString(1, ip);
                        insertCache.setString(2, city);
                        insertCache.setString(3, country);
                        insertCache.setString(4, isp);
                        insertCache.executeUpdate();
                    }

                    // Save history
                    PreparedStatement insertHistory = conn.prepareStatement(
                            "INSERT INTO ip_search_history(ip_address, user_id) VALUES (?, ?)");
                    insertHistory.setString(1, ip);
                    insertHistory.setInt(2, userId);
                    insertHistory.executeUpdate();

                    // Threat detection
                    boolean isThreat = isp.toLowerCase().contains("vpn") ||
                            isp.toLowerCase().contains("proxy") ||
                            isp.toLowerCase().contains("tor") ||
                            isp.toLowerCase().contains("anonymous") ||
                            isp.toLowerCase().contains("malicious") ||
                            isp.toLowerCase().contains("threat") ||
                            isp.toLowerCase().contains("blacklist");

                    if (isThreat) {
                        String alertMsg = "⚠️ Threat Detected!\nIP: " + ip + "\nISP: " + isp;
                        PreparedStatement insertAlert = conn.prepareStatement(
                                "INSERT INTO alerts (user_id, ip_address, alert_message) VALUES (?, ?, ?)");
                        insertAlert.setInt(1, userId);
                        insertAlert.setString(2, ip);
                        insertAlert.setString(3, alertMsg);
                        insertAlert.executeUpdate();
                        output += "\n\n⚠️ ALERT: This IP is flagged as malicious!";
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    output = "❌ Error fetching data.";
                }
                return null;
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                resultArea.setText(output);
            }
        };
        worker.execute();
    }
}
