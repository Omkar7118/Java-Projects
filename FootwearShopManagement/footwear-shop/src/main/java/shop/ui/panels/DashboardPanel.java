package shop.ui.panels;

import shop.ui.MainFrame;
import shop.util.AppSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Top-level panel shown after login.  Contains:
 *   • Left sidebar  – navigation buttons (role-sensitive)
 *   • Right content – CardLayout that swaps feature panels
 */
public class DashboardPanel extends JPanel {
    private final MainFrame mainFrame;

    // content switcher
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);

    // nav buttons (keep refs so we can show/hide per role)
    private JButton btnHome, btnBilling, btnInventory, btnReports, btnEmployees, btnLogout;

    // feature sub-panels (lazy-created)
    private HomePanel        homePanel;
    private BillingPanel     billingPanel;
    private InventoryPanel   inventoryPanel;
    private ReportPanel      reportPanel;
    private EmployeePanel    employeePanel;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        buildSidebar();
        buildContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    // ── refresh after login (role may change) ──────────────
    public void refreshForCurrentUser() {
        boolean isOwner = AppSession.getInstance().isOwner();
        btnEmployees.setVisible(isOwner);

        // always land on home
        contentLayout.show(contentPanel, "home");
    }

    // ── Sidebar ─────────────────────────────────────────────
    private void buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        // logo area
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoArea.setBackground(new Color(15, 23, 42));
        logoArea.setBorder(BorderFactory.createEmptyBorder(24, 10, 20, 10));
        JLabel logo = new JLabel("👟  Footwear Shop");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        logo.setForeground(new Color(251, 191, 36));
        logoArea.add(logo);
        sidebar.add(logoArea);

        // user badge
        sidebar.add(userBadge());
        sidebar.add(Box.createVerticalStrut(12));

        // separator
        sidebar.add(separator());
        sidebar.add(Box.createVerticalStrut(12));

        // nav buttons
        btnHome       = navButton("🏠  Home",        "home");
        btnBilling    = navButton("🧾  Billing",     "billing");
        btnInventory  = navButton("📦  Inventory",   "inventory");
        btnReports    = navButton("📊  Reports",     "reports");
        btnEmployees  = navButton("👥  Employees",   "employees");

        sidebar.add(btnHome);       sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnBilling);    sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnInventory);  sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnReports);    sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnEmployees);  sidebar.add(Box.createVerticalStrut(4));

        // push logout to bottom
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(separator());
        sidebar.add(Box.createVerticalStrut(8));

        btnLogout = navButton("🚪  Logout", "logout");
        btnLogout.setBackground(new Color(30, 41, 59));
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(16));

        add(sidebar, BorderLayout.WEST);
    }

    private JPanel userBadge() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panel.setBackground(new Color(15, 23, 42));

        String name = AppSession.getInstance().getCurrentUser().getFullName();
        String role = AppSession.getInstance().getCurrentUser().getRole();

        // avatar circle
        JLabel avatar = new JLabel(String.valueOf(name.charAt(0)).toUpperCase()) {
            @Override public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(251, 191, 36));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(15, 23, 42));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(15, 23, 42));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel(role.charAt(0) + role.substring(1).toLowerCase());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(148, 163, 184));
        info.add(nameLabel);
        info.add(roleLabel);

        panel.add(avatar);
        panel.add(info);
        return panel;
    }


    private JSeparator separator() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setBackground(new Color(51, 65, 85));
        return sep;
    }

    private JButton navButton(String text, String action) {
        JButton btn = new JButton(text) {
            @Override public void paintComponent(Graphics g) {
                if (getBackground().equals(new Color(251, 191, 36))) {
                    // active highlight
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(30, 41, 59));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                super.paintComponent(g);
            }
        };
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(15, 23, 42));
        btn.setForeground(new Color(203, 213, 225));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> navigate(action));
        return btn;
    }

    // ── Content area & navigation ──────────────────────────
    private void buildContent() {
        contentPanel.setBackground(new Color(241, 245, 249));

        homePanel = new HomePanel();
        contentPanel.add(homePanel, "home");

        billingPanel = new BillingPanel();
        contentPanel.add(billingPanel, "billing");

        inventoryPanel = new InventoryPanel();
        contentPanel.add(inventoryPanel, "inventory");

        reportPanel = new ReportPanel();
        contentPanel.add(reportPanel, "reports");

        employeePanel = new EmployeePanel();
        contentPanel.add(employeePanel, "employees");
    }

    private void navigate(String target) {
        // deactivate all buttons' highlight
        for (JButton b : new JButton[]{btnHome, btnBilling, btnInventory, btnReports, btnEmployees, btnLogout}) {
            b.setBackground(new Color(15, 23, 42));
            b.setForeground(new Color(203, 213, 225));
        }

        if ("logout".equals(target)) {
            mainFrame.showLogin();
            return;
        }

        // activate chosen
        JButton active = switch (target) {
            case "home"      -> btnHome;
            case "billing"   -> btnBilling;
            case "inventory" -> btnInventory;
            case "reports"   -> btnReports;
            case "employees" -> btnEmployees;
            default          -> btnHome;
        };
        active.setBackground(new Color(30, 41, 59));
        active.setForeground(new Color(251, 191, 36));

        // refresh panels that need it
        switch (target) {
            case "inventory"  -> inventoryPanel.refresh();
            case "billing"    -> billingPanel.refresh();
            case "reports"    -> reportPanel.refresh();
            case "employees"  -> employeePanel.refresh();
        }

        contentLayout.show(contentPanel, target);
    }
}
