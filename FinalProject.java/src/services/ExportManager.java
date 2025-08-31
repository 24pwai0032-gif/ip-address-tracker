package services;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;

import db.DatabaseConnection;

public class ExportManager {

    // ✅ 1. Export PDF for individual user
    public static void exportSearchHistoryToPDF(int userId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ip_address, searched_at FROM ip_search_history WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            Document doc = new Document();
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("SearchHistory.pdf"));
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            PdfWriter.getInstance(doc, new FileOutputStream(chooser.getSelectedFile()));
            doc.open();
            doc.add(new Paragraph("🔍 Your IP Search History\n\n"));

            PdfPTable table = new PdfPTable(2);
            table.addCell("IP Address");
            table.addCell("Searched At");

            while (rs.next()) {
                table.addCell(rs.getString("ip_address"));
                table.addCell(rs.getString("searched_at"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "PDF Exported Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
        }
    }

    // ✅ 2. Export CSV for individual user
    public static void exportSearchHistoryToCSV(int userId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ip_address, searched_at FROM ip_search_history WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("SearchHistory.csv"));
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            FileWriter writer = new FileWriter(chooser.getSelectedFile());
            writer.write("IP Address,Searched At\n");

            while (rs.next()) {
                writer.write(rs.getString("ip_address") + "," + rs.getString("searched_at") + "\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(null, "CSV Exported Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
        }
    }

    // ✅ 3. Admin: Export ALL search logs to PDF
    public static void exportAllSearchLogsToPDF() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, ish.ip_address, ish.searched_at " +
                            "FROM ip_search_history ish JOIN users u ON ish.user_id = u.id");

            ResultSet rs = stmt.executeQuery();

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("AllSearchLogs.pdf"));
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(chooser.getSelectedFile()));
            doc.open();
            doc.add(new Paragraph("📊 All IP Search Logs\n\n"));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Username");
            table.addCell("IP Address");
            table.addCell("Searched At");

            while (rs.next()) {
                table.addCell(rs.getString("username"));
                table.addCell(rs.getString("ip_address"));
                table.addCell(rs.getString("searched_at"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Admin PDF Exported Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Admin Export Failed: " + e.getMessage());
        }
    }

    // ✅ 4. Admin: Export ALL search logs to CSV
    public static void exportAllSearchLogsToCSV() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.username, ish.ip_address, ish.searched_at " +
                            "FROM ip_search_history ish JOIN users u ON ish.user_id = u.id");

            ResultSet rs = stmt.executeQuery();

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("AllSearchLogs.csv"));
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            FileWriter writer = new FileWriter(chooser.getSelectedFile());
            writer.write("Username,IP Address,Searched At\n");

            while (rs.next()) {
                writer.write(rs.getString("username") + "," +
                        rs.getString("ip_address") + "," +
                        rs.getString("searched_at") + "\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(null, "Admin CSV Exported Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Admin Export Failed: " + e.getMessage());
        }
    }
}
