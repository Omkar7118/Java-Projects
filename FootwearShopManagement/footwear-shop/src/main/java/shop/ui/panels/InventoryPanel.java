package shop.ui.panels;

import shop.dao.ProductDAO;
import shop.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    // table
    private final String[] COLS = {"ID", "SKU", "Name", "Brand", "Category", "Size", "Color", "Cost $", "Sell $", "Qty"};
    private final DefaultTableModel tableModel = new DefaultTableModel(COLS, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    // search
    private final JTextField searchField = new JTextField();

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 245, 249));
        buildUI();
    }

    // ── refresh from DB ──────────────────────────────────
    public void refresh() { loadProducts(ProductDAO.getInstance().findAll()); }

    // ── build UI ─────────────────────────────────────────
    private void buildUI() {
        // ── Top bar ──
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setBackground(new Color(241, 245, 249));
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 28, 12, 28));

        JLabel title = new JLabel("Inventory Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        topBar.add(title, BorderLayout.WEST);

        // search + buttons
        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTools.setBackground(new Color(241, 245, 249));

        searchField.setPreferredSize(new Dimension(240, 36));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });

        rightTools.add(searchField);
        rightTools.add(actionButton("＋ Add Product", new Color(16, 185, 129)));
        rightTools.add(actionButton("✏ Edit", new Color(59, 130, 246)));
        rightTools.add(actionButton("🗑 Delete", new Color(239, 68, 68)));
        rightTools.add(actionButton("📥 Restock", new Color(139, 92, 246)));

        topBar.add(rightTools, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ──
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setGridColor(new Color(230, 235, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(15, 23, 42));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 41, 59));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // column widths
        int[] widths = {40, 90, 160, 100, 100, 55, 80, 80, 80, 60};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 28, 28, 28));
        scrollPane.getViewport().setBackground(new Color(241, 245, 249));
        add(scrollPane, BorderLayout.CENTER);

        // wire button actions
        wireButtons(rightTools);
    }

    private void wireButtons(JPanel toolbar) {
        for (Component c : toolbar.getComponents()) {
            if (!(c instanceof JButton btn)) continue;
            String txt = btn.getText();
            if (txt.contains("Add"))     btn.addActionListener(e -> openAddDialog());
            else if (txt.contains("Edit"))    btn.addActionListener(e -> openEditDialog());
            else if (txt.contains("Delete"))  btn.addActionListener(e -> deleteSelected());
            else if (txt.contains("Restock")) btn.addActionListener(e -> restockSelected());
        }
    }

    // ── Data loading ─────────────────────────────────────
    private void loadProducts(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getSku(), p.getName(), p.getBrand(), p.getCategory(),
                p.getSize(), p.getColor(),
                String.format("%.2f", p.getCostPrice()),
                String.format("%.2f", p.getSellPrice()),
                p.getQuantity()
            });
        }
    }

    private void doSearch() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) refresh();
        else loadProducts(ProductDAO.getInstance().search(kw));
    }

    // ── Dialogs ──────────────────────────────────────────
    private void openAddDialog() {
        ProductDialog dialog = new ProductDialog(null, "Add New Product", null);
        dialog.setVisible(true);
        if (dialog.isSaved()) refresh();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a product to edit."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Product p = ProductDAO.getInstance().findById(id);
        ProductDialog dialog = new ProductDialog(null, "Edit Product", p);
        dialog.setVisible(true);
        if (dialog.isSaved()) refresh();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a product to delete."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ProductDAO.getInstance().delete(id);
            refresh();
        }
    }

    private void restockSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a product to restock."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String input = JOptionPane.showInputDialog(this, "Enter quantity to add:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int qty = Integer.parseInt(input.trim());
            if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be positive."); return; }
            ProductDAO.getInstance().adjustQuantity(id, qty);
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
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
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 36));
        return btn;
    }
}

// ─── Product Add / Edit Dialog ────────────────────────────────────────────────
class ProductDialog extends JDialog {
    private boolean saved = false;

    private final JTextField skuField    = new JTextField();
    private final JTextField nameField   = new JTextField();
    private final JTextField brandField  = new JTextField();
    private final JTextField catField    = new JTextField();
    private final JTextField sizeField   = new JTextField();
    private final JTextField colorField  = new JTextField();
    private final JTextField costField   = new JTextField();
    private final JTextField sellField   = new JTextField();
    private final JTextField qtyField    = new JTextField();
    private final JTextField descField   = new JTextField();

    private final Product existing;

    ProductDialog(JFrame parent, String title, Product existing) {
        super(parent, title, true);
        this.existing = existing;
        setSize(480, 540);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        buildUI();
        if (existing != null) populateFields();
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(10, 2, 12, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));

        addRow(form, "SKU *", skuField);
        addRow(form, "Name *", nameField);
        addRow(form, "Brand *", brandField);
        addRow(form, "Category *", catField);
        addRow(form, "Size *", sizeField);
        addRow(form, "Color *", colorField);
        addRow(form, "Cost Price $", costField);
        addRow(form, "Sell Price $", sellField);
        addRow(form, "Quantity", qtyField);
        addRow(form, "Description", descField);

        if (existing != null) skuField.setEditable(false);

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
        skuField.setText(existing.getSku());
        nameField.setText(existing.getName());
        brandField.setText(existing.getBrand());
        catField.setText(existing.getCategory());
        sizeField.setText(existing.getSize());
        colorField.setText(existing.getColor());
        costField.setText(String.valueOf(existing.getCostPrice()));
        sellField.setText(String.valueOf(existing.getSellPrice()));
        qtyField.setText(String.valueOf(existing.getQuantity()));
        descField.setText(existing.getDescription());
    }

    private void onSave() {
        // validate required
        if (isBlank(nameField) || isBlank(brandField) || isBlank(catField)
                || isBlank(sizeField) || isBlank(colorField)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (*).");
            return;
        }

        String sku = skuField.getText().trim();
        if (existing == null && sku.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SKU is required.");
            return;
        }
        if (existing == null && ProductDAO.getInstance().skuExists(sku)) {
            JOptionPane.showMessageDialog(this, "SKU already exists. Please use a unique SKU.");
            return;
        }

        double cost = parseDouble(costField);
        double sell = parseDouble(sellField);
        int qty     = parseInt(qtyField);

        if (existing == null) {
            // CREATE
            Product p = new Product();
            p.setSku(sku);
            p.setName(nameField.getText().trim());
            p.setBrand(brandField.getText().trim());
            p.setCategory(catField.getText().trim());
            p.setSize(sizeField.getText().trim());
            p.setColor(colorField.getText().trim());
            p.setCostPrice(cost);
            p.setSellPrice(sell);
            p.setQuantity(qty);
            p.setDescription(descField.getText().trim());
            ProductDAO.getInstance().create(p);
        } else {
            // UPDATE
            existing.setName(nameField.getText().trim());
            existing.setBrand(brandField.getText().trim());
            existing.setCategory(catField.getText().trim());
            existing.setSize(sizeField.getText().trim());
            existing.setColor(colorField.getText().trim());
            existing.setCostPrice(cost);
            existing.setSellPrice(sell);
            existing.setQuantity(qty);
            existing.setDescription(descField.getText().trim());
            ProductDAO.getInstance().update(existing);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }

    private void addRow(JPanel form, String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        form.add(lbl);
        form.add(field);
    }

    private boolean isBlank(JTextField f) { return f.getText().trim().isEmpty(); }
    private double parseDouble(JTextField f) {
        try { return Double.parseDouble(f.getText().trim()); } catch (Exception e) { return 0; }
    }
    private int parseInt(JTextField f) {
        try { return Integer.parseInt(f.getText().trim()); } catch (Exception e) { return 0; }
    }
}
