package shop.ui;

import shop.ui.panels.*;
import shop.util.AppSession;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int W = 1280, H = 780;

    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    // sub-panels
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame() {
        super("👟 Footwear Shop – Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(W, H);
        setMinimumSize(new Dimension(W, H));
        setLocationRelativeTo(null);
        setIconImage(null);

        loginPanel = new LoginPanel(this);
        rootPanel.add(loginPanel, "login");
        add(rootPanel);

        rootLayout.show(rootPanel, "login");
    }

    /** Called by LoginPanel after successful auth */
    public void showMainDashboard() {
        if (dashboardPanel == null) {
            dashboardPanel = new DashboardPanel(this);
            rootPanel.add(dashboardPanel, "dashboard");
        }
        dashboardPanel.refreshForCurrentUser();
        rootLayout.show(rootPanel, "dashboard");
    }

    /** Called when user logs out */
    public void showLogin() {
        AppSession.getInstance().logout();
        dashboardPanel = null;  // recreate fresh next time
        rootLayout.show(rootPanel, "login");
    }
}
