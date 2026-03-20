package shop.ui.panels;

import shop.dao.EmployeeDAO;
import shop.model.Employee;
import shop.util.PasswordUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EmployeePanel extends JPanel {

    private final String[] COLS = {"ID", "Username", "Full Name", "Role", "Phone", "Email", "Hire Date", "Active"};
    private final DefaultTableModel tableModel = new DefaultTableModel(COLS, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 245, 249));
        buildUI();
    }

    public void refresh() { loadEmployees(EmployeeDAO.getInstance().findAllEmployees()); }

    private void buildUI() {
        // ── Top bar ──
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(241, 245, 249));
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 28, 12, 28));

        JLabel title = new JLabel("Employee Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        topBar.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBackground(new Color(241, 245, 249));
        buttons.add(actionButton("＋ Add Employee",   new Color(16, 185, 129)));
        buttons.add(actionButton("✏ Edit Employee",   new Color(59, 130, 246)));
        buttons.add(actionButton("🔑 Change Password", new Color(139, 92, 246)));
        buttons.add(actionButton("🗑 Delete",          new Color(239, 68, 68)));
        topBar.add(buttons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ──
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setGridColor(new Color(230, 235, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 41, 59));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 28, 28, 28));
        scrollPane.getViewport().setBackground(new Color(241, 245, 249));
        add(scrollPane, BorderLayout.CENTER);

        // wire buttons
        wireButtons(buttons);
    }

    private void wireButtons(JPanel toolbar) {
        for (Component c : toolbar.getComponents()) {
            if (!(c instanceof JButton btn)) continue;
            String txt = btn.getText();
            if (txt.contains("Add"))      btn.addActionListener(e -> openAddDialog());
            else if (txt.contains("Edit"))     btn.addActionListener(e -> openEditDialog());
            else if (txt.contains("Password")) btn.addActionListener(e -> changePassword());
            else if (txt.contains("Delete"))   btn.addActionListener(e -> deleteSelected());
        }
    }

    // ── Data ─────────────────────────────────────────────
    private void loadEmployees(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                emp.getId(), emp.getUsername(), emp.getFullName(), emp.getRole(),
                emp.getPhone() != null ? emp.getPhone() : "",
                emp.getEmail() != null ? emp.getEmail() : "",
                emp.getHireDate(), emp.isActive() ? "Yes" : "No"
            });
        }
    }

    // ── Dialogs ──────────────────────────────────────────
    private void openAddDialog() {
        EmployeeDialog dialog = new EmployeeDialog(null, "Add New Employee", null);
        dialog.setVisible(true);
        if (dialog.isSaved()) refresh();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select an employee to edit."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Employee emp = EmployeeDAO.getInstance().findById(id);
        EmployeeDialog dialog = new EmployeeDialog(null, "Edit Employee", emp);
        dialog.setVisible(true);
        if (dialog.isSaved()) refresh();
    }

    private void changePassword() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select an employee."); return; }
        int id = (int) tableModel.getValueAt(row, 0);

        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();
        Object[] message = {
            "New Password:", newPass,
            "Confirm Password:", confirmPass
        };
        int option = JOptionPane.showOptionDialog(this, message, "Change Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option != JOptionPane.OK_OPTION) return;

        String p1 = new String(newPass.getPassword());
        String p2 = new String(confirmPass.getPassword());
        if (p1.isEmpty()) { JOptionPane.showMessageDialog(this, "Password cannot be empty."); return; }
        if (!p1.equals(p2)) { JOptionPane.showMessageDialog(this, "Passwords do not match."); return; }

        EmployeeDAO.getInstance().updatePassword(id, PasswordUtil.hash(p1));
        JOptionPane.showMessageDialog(this, "Password changed successfully.");
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select an employee to delete."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            EmployeeDAO.getInstance().delete(id);
            refresh();
        }
    }

    // ── Helper ───────────────────────────────────────────
    private JButton actionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 12, 36));
        return btn;
    }
}

// ─── Employee Add / Edit Dialog ─────────────────────────────────────────────
class EmployeeDialog extends JDialog {
    private boolean saved = false;

    private final JTextField usernameField  = new JTextField();
    private final JTextField fullNameField  = new JTextField();
    private final JTextField phoneField     = new JTextField();
    private final JTextField emailField     = new JTextField();
    private final JTextField hireDateField  = new JTextField(LocalDate.now().toString());
    private final JPasswordField passField  = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();
    private final JCheckBox activeCheck     = new JCheckBox("Active", true);

    private final Employee existing;

    EmployeeDialog(JFrame parent, String title, Employee existing) {
        super(parent, title, true);
        this.existing = existing;
        setSize(440, 460);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        buildUI();
        if (existing != null) populateFields();
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(9, 2, 12, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));

        addRow(form, "Username *", usernameField);
        addRow(form, "Full Name *", fullNameField);
        addRow(form, "Phone", phoneField);
        addRow(form, "Email", emailField);
        addRow(form, "Hire Date", hireDateField);

        // password fields only for new
        addRow(form, "Password *", passField);
        addRow(form, "Confirm Password *", confirmField);

        // active checkbox
        JLabel activeLabel = new JLabel("Status");
        activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeLabel.setForeground(new Color(51, 65, 85));
        activeCheck.setBackground(Color.WHITE);
        activeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(activeLabel);
        form.add(activeCheck);

        // spacer
        form.add(new JLabel(""));
        form.add(new JLabel(""));

        if (existing != null) {
            usernameField.setEditable(false);
            passField.setEnabled(false);
            confirmField.setEnabled(false);
        }

        add(form, BorderLayout.CENTER);

        // buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnPanel.setBackground(new Color(245, 247, 250));

        JButton saveBtn = new JButton("Save");
        saveBtn.setFocusPainted(false);
        saveBtn.setBackground(new Color(16, 185, 129));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> onSave());

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBackground(new Color(200, 200, 200));
        cancelBtn.setForeground(new Color(30, 30, 30));
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        usernameField.setText(existing.getUsername());
        fullNameField.setText(existing.getFullName());
        phoneField.setText(existing.getPhone() != null ? existing.getPhone() : "");
        emailField.setText(existing.getEmail() != null ? existing.getEmail() : "");
        hireDateField.setText(existing.getHireDate());
        activeCheck.setSelected(existing.isActive());
    }

    private void onSave() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();

        if (username.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Full Name are required.");
            return;
        }

        if (existing == null) {
            // create
            if (EmployeeDAO.getInstance().usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken.");
                return;
            }
            String p1 = new String(passField.getPassword());
            String p2 = new String(confirmField.getPassword());
            if (p1.isEmpty()) { JOptionPane.showMessageDialog(this, "Password is required."); return; }
            if (!p1.equals(p2)) { JOptionPane.showMessageDialog(this, "Passwords do not match."); return; }

            Employee emp = new Employee();
            emp.setUsername(username);
            emp.setPassword(PasswordUtil.hash(p1));
            emp.setFullName(fullName);
            emp.setRole("EMPLOYEE");
            emp.setPhone(phoneField.getText().trim());
            emp.setEmail(emailField.getText().trim());
            emp.setHireDate(hireDateField.getText().trim());
            emp.setActive(activeCheck.isSelected());
            EmployeeDAO.getInstance().create(emp);
        } else {
            // update
            existing.setFullName(fullName);
            existing.setPhone(phoneField.getText().trim());
            existing.setEmail(emailField.getText().trim());
            existing.setHireDate(hireDateField.getText().trim());
            existing.setActive(activeCheck.isSelected());
            EmployeeDAO.getInstance().update(existing);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }

    private void addRow(JPanel form, String label, javax.swing.JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        if (field instanceof JTextField tf) {
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        } else if (field instanceof JPasswordField pf) {
            pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        }
        form.add(lbl);
        form.add(field);
    }
}
