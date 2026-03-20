package shop.ui;

import shop.dao.EmployeeDAO;
import shop.model.Employee;
import shop.util.AppSession;
import shop.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel errorLabel = new JLabel(" ");

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));  // dark slate
        buildUI();
    }

    private void buildUI() {
        // ── Centre card ──────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(new Color(30, 41, 59));
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // logo / title
        JLabel logo = new JLabel("👟  Footwear Shop", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(new Color(251, 191, 36));  // amber
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);
        card.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Management System", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(148, 163, 184));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(32));

        // username
        card.add(labelFor("Username"));
        card.add(Box.createVerticalStrut(6));
        styleInput(usernameField);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));

        // password
        card.add(labelFor("Password"));
        card.add(Box.createVerticalStrut(6));
        styleInput(passwordField);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));

        // error label
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));

        // login button
        JButton loginBtn = new JButton("LOG IN");
        loginBtn.setFocusPainted(false);
        loginBtn.setBackground(new Color(251, 191, 36));
        loginBtn.setForeground(new Color(15, 23, 42));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(this::onLogin);
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));

        JLabel hint = new JLabel("Default credentials: owner / admin123", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 116, 139));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        // wrap card in a centred layout
        JPanel centred = new JPanel(new GridBagLayout());
        centred.setBackground(new Color(15, 23, 42));
        centred.add(card);

        add(centred, BorderLayout.CENTER);

        // allow Enter key
        passwordField.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("ENTER"), "login");
        passwordField.getActionMap().put("login", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { onLogin(e); }
        });
    }

    private void onLogin(ActionEvent e) {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Username and password are required.");
            return;
        }

        Employee emp = EmployeeDAO.getInstance().findByUsername(user);
        if (emp == null || !PasswordUtil.matches(pass, emp.getPassword()) || !emp.isActive()) {
            errorLabel.setText("Invalid credentials or account is inactive.");
            return;
        }

        // ── success ──
        errorLabel.setText(" ");
        AppSession.getInstance().setCurrentUser(emp);
        mainFrame.showMainDashboard();
    }

    // ── helpers ──────────────────────────────────────────────
    private JLabel labelFor(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(new Color(203, 213, 225));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void styleInput(JComponent input) {
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setBackground(new Color(51, 65, 85));
        input.setForeground(Color.WHITE);
        input.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        input.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
