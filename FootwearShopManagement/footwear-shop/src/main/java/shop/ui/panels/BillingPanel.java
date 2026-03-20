package shop.ui.panels;

import shop.dao.BillDAO;
import shop.dao.CustomerDAO;
import shop.dao.ProductDAO;
import shop.model.*;
import shop.util.AppSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BillingPanel extends JPanel {
    // ── state ──
    private final List<BillItem> cartItems = new ArrayList<>();
    private Customer currentCustomer;

    // ── sell tab UI ──
    private final JTextField productSearchField = new JTextField();
    private final JTextField customerNameField  = new JTextField();
    private final JTextField customerPhoneField = new JTextField();
    private final JTextField discountField      = new JTextField("0");
    private final JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"CASH", "CARD", "UPI"});

    private final DefaultTableModel cartModel = new DefaultTableModel(
            new String[]{"Product", "SKU", "Size", "Qty", "Unit Price", "Total"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return c == 3; } // only qty editable
    };
    private final JTable cartTable = new JTable(cartModel);

    private final JLabel totalLabel   = new JLabel("$0.00");
    private final JLabel discountLabel = new JLabel("Discount: $0.00");
    private final JLabel finalLabel   = new JLabel("Final: $0.00");

    // product search results popup
    private JListPopup productPopup;

    public BillingPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 245, 249));
        buildUI();
    }

    public void refresh() {
        // reset cart on re-enter
        cartItems.clear();
        cartModel.setRowCount(0);
        updateTotals();
        productSearchField.setText("");
        customerNameField.setText("");
        customerPhoneField.setText("");
        discountField.setText("0");
    }

    // ── UI build ─────────────────────────────────────────
    private void buildUI() {
        // ── Title + tabs ──
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(241, 245, 249));
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 28, 0, 28));

        JLabel title = new JLabel("Billing");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        topBar.add(title, BorderLayout.WEST);

        JPanel tabButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        tabButtons.setBackground(new Color(241, 245, 249));
        tabButtons.add(tabBtn("💰 New Sale", true));
        tabButtons.add(tabBtn("↩️  Return", false));
        tabButtons.add(tabBtn("🔄 Exchange", false));
        topBar.add(tabButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Main content – left (form) + right (cart) ──
        JPanel mainContent = new JPanel(new BorderLayout(0, 0));
        mainContent.setBackground(new Color(241, 245, 249));
        mainContent.setBorder(BorderFactory.createEmptyBorder(16, 28, 28, 28));

        // LEFT – customer + product search
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(241, 245, 249));
        leftPanel.setPreferredSize(new Dimension(380, 0));

        // customer section
        JPanel custCard = whiteCard("Customer (optional)");
        custCard.add(fieldRow("Name", customerNameField));
        custCard.add(fieldRow("Phone", customerPhoneField));

        JButton searchCust = smallBtn("Search Customer", new Color(59, 130, 246));
        searchCust.addActionListener(e -> searchCustomer());
        JPanel custBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        custBtnRow.setBackground(Color.WHITE);
        custBtnRow.add(searchCust);

        JButton addNewCust = smallBtn("+ New Customer", new Color(16, 185, 129));
        addNewCust.addActionListener(e -> addNewCustomer());
        custBtnRow.add(addNewCust);
        custCard.add(custBtnRow);
        leftPanel.add(custCard);
        leftPanel.add(Box.createVerticalStrut(10));

        // product search section
        JPanel prodCard = whiteCard("Add Product");
        prodCard.add(fieldRow("Search product…", productSearchField));
        productSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchProducts(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchProducts(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
        leftPanel.add(prodCard);
        leftPanel.add(Box.createVerticalStrut(10));

        // payment
        JPanel payCard = whiteCard("Payment");
        JPanel payRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        payRow.setBackground(Color.WHITE);
        payRow.add(new JLabel("Method:"));
        payRow.add(paymentCombo);
        payRow.add(Box.createHorizontalStrut(16));

        JTextField discInput = discountField;
        JPanel discRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        discRow.setBackground(Color.WHITE);
        discRow.add(new JLabel("Discount $:"));
        discInput.setPreferredSize(new Dimension(80, 28));
        discRow.add(discInput);

        payCard.add(payRow);
        payCard.add(discRow);
        leftPanel.add(payCard);

        mainContent.add(leftPanel, BorderLayout.WEST);

        // RIGHT – cart table + totals + checkout
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(241, 245, 249));

        // cart table header
        JPanel cartHeader = new JPanel(new BorderLayout());
        cartHeader.setBackground(new Color(30, 41, 59));
        cartHeader.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel cartTitle = new JLabel("🛒  Shopping Cart");
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cartTitle.setForeground(Color.WHITE);
        cartHeader.add(cartTitle, BorderLayout.WEST);

        JButton removeBtn = smallBtn("Remove Selected", new Color(239, 68, 68));
        removeBtn.addActionListener(e -> removeFromCart());
        cartHeader.add(removeBtn, BorderLayout.EAST);

        rightPanel.add(cartHeader, BorderLayout.NORTH);

        // cart table
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cartTable.setRowHeight(32);
        cartTable.setGridColor(new Color(230, 235, 240));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        cartTable.getTableHeader().setBackground(new Color(51, 65, 85));
        cartTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        // qty change listener
        cartTable.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                if (row >= 0 && row < cartItems.size()) {
                    try {
                        int newQty = Integer.parseInt(cartTable.getValueAt(row, 3).toString().trim());
                        if (newQty <= 0) { removeFromCart(); return; }
                        cartItems.get(row).setQuantity(newQty);
                        cartModel.setValueAt(String.format("%.2f", cartItems.get(row).getItemTotal()), row, 5);
                        updateTotals();
                    } catch (NumberFormatException ex) { /* ignore */ }
                }
            }
        });

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // bottom totals + checkout
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 230)),
            BorderFactory.createEmptyBorder(12, 16, 16, 16)));

        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalLabel.setForeground(new Color(100, 116, 139));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        discountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        discountLabel.setForeground(new Color(239, 68, 68));
        discountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        finalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        finalLabel.setForeground(new Color(16, 185, 129));
        finalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bottomPanel.add(totalLabel);
        bottomPanel.add(Box.createVerticalStrut(4));
        bottomPanel.add(discountLabel);
        bottomPanel.add(Box.createVerticalStrut(4));
        bottomPanel.add(finalLabel);
        bottomPanel.add(Box.createVerticalStrut(10));

        JButton checkoutBtn = new JButton("✅  Checkout & Print Receipt");
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setBackground(new Color(16, 185, 129));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkoutBtn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        checkoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkoutBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        checkoutBtn.addActionListener(e -> checkout());
        bottomPanel.add(checkoutBtn);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainContent.add(rightPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }

    // ── Product search popup ─────────────────────────────
    private void searchProducts() {
        String kw = productSearchField.getText().trim();
        if (kw.length() < 2) return;
        List<Product> results = ProductDAO.getInstance().search(kw);
        if (results.isEmpty()) return;

        // show results in a simple list popup
        String[] items = results.stream()
                .map(p -> String.format("[%s] %s – %s (Size:%s, Qty:%d) $%.2f",
                        p.getSku(), p.getName(), p.getBrand(), p.getSize(), p.getQuantity(), p.getSellPrice()))
                .toArray(String[]::new);

        productPopup = new JListPopup(items, idx -> {
            Product selected = results.get(idx);
            if (selected.getQuantity() <= 0) {
                JOptionPane.showMessageDialog(BillingPanel.this, "This product is out of stock!");
                return;
            }
            addToCart(selected);
            productSearchField.setText("");
        });

        productPopup.showBelow(productSearchField);
    }

    private void addToCart(Product p) {
        // check if already in cart → increment
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProductId() == p.getId()) {
                BillItem existing = cartItems.get(i);
                existing.setQuantity(existing.getQuantity() + 1);
                cartModel.setValueAt(existing.getQuantity(), i, 3);
                cartModel.setValueAt(String.format("%.2f", existing.getItemTotal()), i, 5);
                updateTotals();
                return;
            }
        }
        BillItem item = new BillItem(p.getId(), p.getName(), p.getSku(), 1, p.getSellPrice());
        cartItems.add(item);
        cartModel.addRow(new Object[]{p.getName(), p.getSku(), p.getSize(), 1,
                String.format("%.2f", p.getSellPrice()), String.format("%.2f", item.getItemTotal())});
        updateTotals();
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) return;
        cartItems.remove(row);
        cartModel.removeRow(row);
        updateTotals();
    }

    private void updateTotals() {
        double total = cartItems.stream().mapToDouble(BillItem::getItemTotal).sum();
        double disc  = parseDiscount();
        double fin   = Math.max(0, total - disc);
        totalLabel.setText("Sub-total:  $" + String.format("%.2f", total));
        discountLabel.setText("Discount:  –$" + String.format("%.2f", disc));
        finalLabel.setText("Final Amount:  $" + String.format("%.2f", fin));
    }

    private double parseDiscount() {
        try { return Double.parseDouble(discountField.getText().trim()); }
        catch (Exception e) { return 0; }
    }

    // ── Customer helpers ─────────────────────────────────
    private void searchCustomer() {
        String phone = customerPhoneField.getText().trim();
        String name  = customerNameField.getText().trim();
        String kw    = !phone.isEmpty() ? phone : name;
        if (kw.isEmpty()) return;
        List<Customer> results = CustomerDAO.getInstance().search(kw);
        if (results.isEmpty()) { JOptionPane.showMessageDialog(this, "No customer found."); return; }
        if (results.size() == 1) {
            setCustomer(results.get(0));
        } else {
            String[] names = results.stream().map(Customer::toString).toArray(String[]::new);
            int idx = JOptionPane.showOptionDialog(this, "Select customer:", "Customers",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (idx >= 0) setCustomer(results.get(idx));
        }
    }

    private void setCustomer(Customer c) {
        currentCustomer = c;
        customerNameField.setText(c.getName());
        customerPhoneField.setText(c.getPhone() != null ? c.getPhone() : "");
    }

    private void addNewCustomer() {
        String name  = customerNameField.getText().trim();
        String phone = customerPhoneField.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter customer name first."); return; }
        Customer c = new Customer(0, name, phone, null, null);
        CustomerDAO.getInstance().create(c);
        currentCustomer = c;
        JOptionPane.showMessageDialog(this, "Customer '" + name + "' added.");
    }

    // ── Checkout ─────────────────────────────────────────
    private void checkout() {
        if (cartItems.isEmpty()) { JOptionPane.showMessageDialog(this, "Cart is empty."); return; }

        // stock validation
        for (BillItem item : cartItems) {
            Product p = ProductDAO.getInstance().findById(item.getProductId());
            if (p.getQuantity() < item.getQuantity()) {
                JOptionPane.showMessageDialog(this,
                    "Insufficient stock for '" + p.getName() + "'. Available: " + p.getQuantity());
                return;
            }
        }

        Bill bill = new Bill();
        bill.setBillNumber(BillDAO.getInstance().generateBillNumber());
        bill.setCustomerId(currentCustomer != null ? currentCustomer.getId() : 0);
        bill.setCustomerName(currentCustomer != null ? currentCustomer.getName() : "Walk-in");
        bill.setEmployeeId(AppSession.getInstance().getCurrentUser().getId());
        bill.setTransactionType("SALE");
        bill.setDiscount(parseDiscount());
        bill.setPaymentMethod(paymentCombo.getSelectedItem().toString());
        bill.setBillDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        bill.setItems(new ArrayList<>(cartItems));
        bill.recalculate();

        // persist
        BillDAO.getInstance().createBill(bill);

        // deduct inventory
        for (BillItem item : cartItems) {
            ProductDAO.getInstance().adjustQuantity(item.getProductId(), -item.getQuantity());
        }

        // show receipt
        showReceipt(bill);

        // reset
        refresh();
    }

    private void showReceipt(Bill bill) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║         👟 FOOTWEAR SHOP             ║\n");
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║ Bill No : %-27s║\n", bill.getBillNumber()));
        sb.append(String.format("║ Date    : %-27s║\n", bill.getBillDate()));
        sb.append(String.format("║ Customer: %-27s║\n", bill.getCustomerName()));
        sb.append(String.format("║ Cashier : %-27s║\n", AppSession.getInstance().getCurrentUser().getFullName()));
        sb.append("╠══════════════════════════════════════╣\n");

        for (BillItem item : bill.getItems()) {
            sb.append(String.format("║ %-18s x%-3d $%8.2f    ║\n",
                    truncate(item.getProductName(), 18), item.getQuantity(), item.getItemTotal()));
        }

        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║ Sub-total : $%-24.2f║\n", bill.getTotalAmount()));
        sb.append(String.format("║ Discount  : –$%-23.2f║\n", bill.getDiscount()));
        sb.append(String.format("║ TOTAL     : $%-24.2f║\n", bill.getFinalAmount()));
        sb.append(String.format("║ Payment   : %-27s║\n", bill.getPaymentMethod()));
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append("║       Thank you for shopping!        ║\n");
        sb.append("╚══════════════════════════════════════╝\n");

        JTextArea receiptArea = new JTextArea(sb.toString());
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receiptArea.setBackground(new Color(255, 253, 245));
        receiptArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JScrollPane sp = new JScrollPane(receiptArea);
        sp.setPreferredSize(new Dimension(420, 380));

        JOptionPane.showOptionDialog(this, sp, "Receipt – " + bill.getBillNumber(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Close"}, "Close");
    }

    // ── Return / Exchange (simplified dialogs) ──────────
    // These are wired from the tab buttons in buildUI
    public void openReturnDialog() {
        String billNo = JOptionPane.showInputDialog(this, "Enter original Bill Number to process return:");
        if (billNo == null || billNo.trim().isEmpty()) return;
        Bill original = BillDAO.getInstance().findByBillNumber(billNo.trim());
        if (original == null) { JOptionPane.showMessageDialog(this, "Bill not found."); return; }

        // let user pick which item to return
        String[] itemNames = original.getItems().stream()
                .map(i -> i.getProductName() + " (SKU: " + i.getProductSku() + ") – Qty: " + i.getQuantity())
                .toArray(String[]::new);

        int idx = JOptionPane.showOptionDialog(this, "Select item to return:", "Return – " + billNo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, itemNames, itemNames[0]);
        if (idx < 0) return;

        BillItem selectedItem = original.getItems().get(idx);
        String qtyInput = JOptionPane.showInputDialog(this, "Quantity to return (max " + selectedItem.getQuantity() + "):");
        if (qtyInput == null) return;
        try {
            int returnQty = Integer.parseInt(qtyInput.trim());
            if (returnQty <= 0 || returnQty > selectedItem.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }
            double refund = returnQty * selectedItem.getUnitPrice();

            String reason = JOptionPane.showInputDialog(this, "Reason for return (optional):");

            BillDAO.getInstance().createReturn(
                original.getId(), selectedItem.getProductId(), returnQty, refund,
                AppSession.getInstance().getCurrentUser().getId(),
                original.getCustomerId(), reason);

            JOptionPane.showMessageDialog(this,
                "Return processed!\nRefund amount: $" + String.format("%.2f", refund));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }

    public void openExchangeDialog() {
        String billNo = JOptionPane.showInputDialog(this, "Enter original Bill Number to process exchange:");
        if (billNo == null || billNo.trim().isEmpty()) return;
        Bill original = BillDAO.getInstance().findByBillNumber(billNo.trim());
        if (original == null) { JOptionPane.showMessageDialog(this, "Bill not found."); return; }

        // pick old item
        String[] itemNames = original.getItems().stream()
                .map(i -> i.getProductName() + " (SKU: " + i.getProductSku() + ")")
                .toArray(String[]::new);

        int idx2 = JOptionPane.showOptionDialog(this, "Select item to exchange:", "Exchange",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, itemNames, itemNames[0]);
        if (idx2 < 0) return;
        BillItem oldItem = original.getItems().get(idx2);

        // search new product
        String newSku = JOptionPane.showInputDialog(this, "Enter SKU of the new product:");
        if (newSku == null || newSku.trim().isEmpty()) return;
        Product newProduct = ProductDAO.getInstance().findBySku(newSku.trim());
        if (newProduct == null) { JOptionPane.showMessageDialog(this, "Product not found."); return; }
        if (newProduct.getQuantity() <= 0) { JOptionPane.showMessageDialog(this, "New product out of stock."); return; }

        double oldPrice = oldItem.getUnitPrice();
        double newPrice = newProduct.getSellPrice();
        double diff     = newPrice - oldPrice;

        String msg = String.format("Exchange Details:\n\nOld: %s @ $%.2f\nNew: %s @ $%.2f\n\nPrice difference: $%.2f %s",
                oldItem.getProductName(), oldPrice, newProduct.getName(), newPrice, Math.abs(diff),
                diff > 0 ? "(Customer pays)" : diff < 0 ? "(Refund to customer)" : "(No difference)");

        int confirm = JOptionPane.showConfirmDialog(this, msg, "Confirm Exchange", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        BillDAO.getInstance().createExchange(
            original.getId(), oldItem.getProductId(), newProduct.getId(),
            1, 1, diff,
            AppSession.getInstance().getCurrentUser().getId(),
            original.getCustomerId(), null);

        JOptionPane.showMessageDialog(this, "Exchange completed successfully!");
    }

    // ── Helpers ──────────────────────────────────────────
    private JPanel whiteCard(String heading) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(14, 16, 10, 16)));

        JLabel h = new JLabel(heading);
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setForeground(new Color(30, 41, 59));
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        card.add(h);
        return card;
    }

    private JPanel fieldRow(String label, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 116, 139));
        lbl.setPreferredSize(new Dimension(70, 28));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JButton smallBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton tabBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(active ? new Color(16, 185, 129) : new Color(220, 225, 230));
        btn.setForeground(active ? Color.WHITE : new Color(51, 65, 85));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // wire return/exchange
        if (text.contains("Return"))  btn.addActionListener(e -> openReturnDialog());
        if (text.contains("Exchange")) btn.addActionListener(e -> openExchangeDialog());

        return btn;
    }

    private static String truncate(String s, int max) {
        return s != null && s.length() > max ? s.substring(0, max) : s;
    }
}

// ── Simple list popup for product search results ───────────────────────────
class JListPopup extends JDialog {
    private final int selectedIndex;

    JListPopup(String[] items, java.util.function.IntConsumer onSelect) {
        super((JFrame) null, true);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JList<String> list = new JList<>(items);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        list.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int idx = list.locationToIndex(e.getPoint());
                if (idx >= 0) { onSelect.accept(idx); dispose(); }
            }
        });

        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(420, Math.min(items.length * 28 + 16, 200)));
        sp.setBorder(BorderFactory.createLineBorder(new Color(100, 116, 139), 1));
        add(sp);
        pack();
        selectedIndex = -1;
    }

    void showBelow(Component ref) {
        java.awt.Point p = ref.getLocationOnScreen();
        setLocation(p.x, p.y + ref.getHeight());
        setVisible(true);
    }
}
