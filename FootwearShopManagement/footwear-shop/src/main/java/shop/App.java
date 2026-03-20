package shop;

import shop.ui.MainFrame;
import shop.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        // Run on the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system look-and-feel as base, then override colours
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // fallback – fine
            }

            // Initialise the database (creates tables + seeds owner)
            DatabaseUtil.getInstance();

            // Launch the main window
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
