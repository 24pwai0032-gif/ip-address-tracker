package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UIUtils - Centralized styling for the IP Address Tracker project.
 * All UI elements should use these helpers for consistent look & feel.
 */
public class UIUtils {

    // 🎨 Global color scheme
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);   // #1976D2
    public static final Color SECONDARY_COLOR = new Color(76, 175, 80); // #4CAF50
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    // 🖋 Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14); // ✅ Added for text fields, labels

    /**
     * Creates a styled JButton with hover effect (Primary Color default).
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, PRIMARY_COLOR, BUTTON_TEXT_COLOR);
        return button;
    }

    /**
     * Creates a styled JButton with custom colors.
     */
    public static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        styleButton(button, bgColor, BUTTON_TEXT_COLOR);
        return button;
    }

    /**
     * Styles an existing JButton with given colors & hover effects.
     */
    public static void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    /**
     * Creates a large title JLabel with consistent styling.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR.darker());
        return label;
    }

    /**
     * Creates a styled back button for navigation with a click action.
     */
    public static JButton createBackButton(String text, java.awt.event.ActionListener action) {
        JButton backBtn = new JButton(text);
        styleButton(backBtn, new Color(96, 125, 139), BUTTON_TEXT_COLOR); // Grayish back button
        backBtn.addActionListener(action);
        return backBtn;
    }

    /**
     * Overloaded Back Button (no action listener).
     * Useful when you want to set the action separately later.
     */
    public static JButton createBackButton(String text) {
        JButton backBtn = new JButton(text);
        styleButton(backBtn, new Color(96, 125, 139), BUTTON_TEXT_COLOR);
        return backBtn;
    }

    /**
     * Styles JTable with alternating row colors & grid lines.
     */
    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });
    }
}

