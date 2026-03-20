package shop.ui.panels;

import shop.dao.BillDAO;
import shop.model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class ReportPanel extends JPanel {

    // date selectors
    private final JTextField startDateField = new JTextField();
    private final JTextField endDateField   = new JTextField();

    // KPI labels
    private final JLabel revenueLabel      = new JLabel("$0.00");
    private final JLabel itemsSoldLabel     = new JLabel("0");
    private final JLabel transactionsLabel  = new JLabel("0");
    private final JLabel avgOrderLabel      = new JLabel("$0.00");

    // sales table
    private final DefaultTableModel salesModel = new DefaultTableModel(
            new String[]{"Bill No", "Date", "Customer", "Items", "Sub-Total", "Discount", "Final", "Payment"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable salesTable = new JTable(salesModel);

    // top-products table
    private final DefaultTableModel topModel = new DefaultTableModel(
            new String[]{"Rank", "Product", "Brand", "SKU", "Price", "Qty Sold", "Revenue"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable topTable = new JTable(topModel);

    // bar-chart panel
    private BarChartPanel barChart;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 245, 249));
        buildUI();
    }

    public void refresh() { applyDateRange(); }

    // ── Build UI ─────────────────────────────────────────
    private void buildUI() {
        // ── Title ──
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(241, 245, 249));
        titleBar.setBorder(BorderFactory.createEmptyBorder(20, 28, 8, 28));
        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        titleBar.add(title, BorderLayout.WEST);
        add(titleBar, BorderLayout.NORTH);

        // ── Scrollable body ──
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(241, 245, 249));
        body.setBorder(BorderFactory.createEmptyBorder(0, 28, 28, 28));

        // date selector row
        body.add(buildDateSelector());
        body.add(Box.createVerticalStrut(14));

        // KPI cards
        body.add(buildKPICards());
        body.add(Box.createVerticalStrut(18));

        // top-selling products section
        JLabel topTitle = new JLabel("🏆  Top Selling Products");
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topTitle.setForeground(new Color(15, 23, 42));
        topTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(topTitle);
        body.add(Box.createVerticalStrut(8));

        // top products table
        topTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topTable.setRowHeight(32);
        topTable.setGridColor(new Color(230, 235, 240));
        topTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        topTable.getTableHeader().setBackground(new Color(30, 41, 59));
        topTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane topScroll = new JScrollPane(topTable);
        topScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        topScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        topScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(topScroll);
        body.add(Box.createVerticalStrut(18));

        // bar chart
        barChart = new BarChartPanel();
        barChart.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        barChart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        barChart.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel chartTitle = new JLabel("📈  Top Products – Units Sold");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(new Color(15, 23, 42));
        chartTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(chartTitle);
        body.add(Box.createVerticalStrut(6));
        body.add(barChart);
        body.add(Box.createVerticalStrut(18));

        // all sales list
        JLabel salesTitle = new JLabel("🧾  Sales List");
        salesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        salesTitle.setForeground(new Color(15, 23, 42));
        salesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(salesTitle);
        body.add(Box.createVerticalStrut(8));

        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        salesTable.setRowHeight(32);
        salesTable.setGridColor(new Color(230, 235, 240));
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        salesTable.getTableHeader().setBackground(new Color(30, 41, 59));
        salesTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane salesScroll = new JScrollPane(salesTable);
        salesScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 240));
        salesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        salesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(salesScroll);

        // wrap in scroll
        JScrollPane outerScroll = new JScrollPane(body);
        outerScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        outerScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outerScroll.setBorder(BorderFactory.createEmptyBorder());
        outerScroll.getViewport().setBackground(new Color(241, 245, 249));
        add(outerScroll, BorderLayout.CENTER);
    }

    // ── Date Selector ────────────────────────────────────
    private JPanel buildDateSelector() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        row.add(boldLabel("Period:"));

        String[] periods = {"Today", "This Week", "This Month", "This Year", "Custom"};
        for (String p : periods) {
            JButton btn = periodButton(p);
            row.add(btn);
        }

        row.add(Box.createHorizontalStrut(16));
        row.add(boldLabel("From:"));
        startDateField.setPreferredSize(new Dimension(110, 28));
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        row.add(startDateField);

        row.add(boldLabel("To:"));
        endDateField.setPreferredSize(new Dimension(110, 28));
        endDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        row.add(endDateField);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setFocusPainted(false);
        applyBtn.setBackground(new Color(16, 185, 129));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        applyBtn.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        applyBtn.setBorderPainted(false);
        applyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        applyBtn.addActionListener(e -> applyDateRange());
        row.add(applyBtn);

        return row;
    }

    private JButton periodButton(String label) {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(240, 242, 245));
        btn.setForeground(new Color(30, 41, 59));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            LocalDate today = LocalDate.now();
            String fmt = "yyyy-MM-dd";
            DateTimeFormatter f = DateTimeFormatter.ofPattern(fmt);

            switch (label) {
                case "Today" -> {
                    startDateField.setText(today.format(f));
                    endDateField.setText(today.format(f));
                }
                case "This Week" -> {
                    startDateField.setText(today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(f));
                    endDateField.setText(today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(f));
                }
                case "This Month" -> {
                    startDateField.setText(today.withDayOfMonth(1).format(f));
                    endDateField.setText(today.with(TemporalAdjusters.lastDayOfMonth()).format(f));
                }
                case "This Year" -> {
                    startDateField.setText(today.withDayOfYear(1).format(f));
                    endDateField.setText(LocalDate.of(today.getYear(), 12, 31).format(f));
                }
                case "Custom" -> {
                    // leave fields editable, user presses Apply
                }
            }
            if (!"Custom".equals(label)) applyDateRange();
        });
        return btn;
    }

    // ── KPI Cards ────────────────────────────────────────
    private JPanel buildKPICards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(new Color(241, 245, 249));
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        cards.add(kpiCard("💰 Total Revenue", revenueLabel, new Color(16, 185, 129)));
        cards.add(kpiCard("📦 Items Sold", itemsSoldLabel, new Color(59, 130, 246)));
        cards.add(kpiCard("🧾 Transactions", transactionsLabel, new Color(139, 92, 246)));
        cards.add(kpiCard("📊 Avg Order Value", avgOrderLabel, new Color(245, 158, 11)));
        return cards;
    }

    private JPanel kpiCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(15, 23, 42));

        card.add(lbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ── Apply / Load data ────────────────────────────────
    private void applyDateRange() {
        String start = startDateField.getText().trim();
        String end   = endDateField.getText().trim();
        if (start.isEmpty() || end.isEmpty()) {
            // default to today
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            start = today; end = today;
            startDateField.setText(start);
            endDateField.setText(end);
        }

        BillDAO dao = BillDAO.getInstance();

        // KPIs
        double revenue     = dao.getTotalRevenue(start, end);
        int    itemsSold   = dao.getTotalItemsSold(start, end);
        int    txns        = dao.getTotalTransactions(start, end);
        double avgOrder    = txns > 0 ? revenue / txns : 0;

        revenueLabel.setText(String.format("$%.2f", revenue));
        itemsSoldLabel.setText(String.valueOf(itemsSold));
        transactionsLabel.setText(String.valueOf(txns));
        avgOrderLabel.setText(String.format("$%.2f", avgOrder));

        // top-selling products (top 10)
        List<Object[]> topProducts = dao.getTopSellingProducts(start, end, 10);
        topModel.setRowCount(0);
        for (int i = 0; i < topProducts.size(); i++) {
            Object[] row = topProducts.get(i);
            topModel.addRow(new Object[]{
                i + 1,
                row[1], row[2], row[3],
                String.format("$%.2f", row[4]),
                row[5],
                String.format("$%.2f", row[6])
            });
        }

        // update bar chart
        barChart.setData(topProducts);
        barChart.repaint();

        // sales list
        List<Bill> sales = dao.findSalesByDateRange(start, end);
        salesModel.setRowCount(0);
        for (Bill b : sales) {
            salesModel.addRow(new Object[]{
                b.getBillNumber(),
                b.getBillDate(),
                b.getCustomerName() != null ? b.getCustomerName() : "Walk-in",
                b.getItems().size(),
                String.format("$%.2f", b.getTotalAmount()),
                String.format("$%.2f", b.getDiscount()),
                String.format("$%.2f", b.getFinalAmount()),
                b.getPaymentMethod()
            });
        }
    }

    // ── Helpers ──────────────────────────────────────────
    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(51, 65, 85));
        return l;
    }
}

// ─── Simple Bar Chart drawn with Graphics2D ─────────────────────────────────
class BarChartPanel extends JPanel {
    private List<Object[]> data; // each: [id, name, brand, sku, price, qty(int), revenue]

    private static final Color[] BAR_COLORS = {
        new Color(16, 185, 129), new Color(59, 130, 246), new Color(139, 92, 246),
        new Color(245, 158, 11), new Color(239, 68, 68),  new Color(20, 184, 166),
        new Color(234, 179, 8),  new Color(99, 102, 241), new Color(244, 63, 94),
        new Color(34, 197, 94)
    };

    public BarChartPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 20, 30, 60)));
    }

    public void setData(List<Object[]> data) { this.data = data; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.setColor(new Color(148, 163, 184));
            g2.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = 40;
        int chartWidth  = getWidth()  - pad - 140;  // leave room for labels on left
        int chartHeight = getHeight() - pad - 30;
        int x0 = 140, y0 = pad;

        int maxQty = 1;
        for (Object[] row : data) maxQty = Math.max(maxQty, (int) row[5]);

        int barGap  = 6;
        int barW    = (chartWidth - (data.size() - 1) * barGap) / data.size();
        if (barW < 8) barW = 8;

        for (int i = 0; i < data.size(); i++) {
            Object[] row = data.get(i);
            String name  = (String) row[1];
            int qty      = (int) row[5];

            int barH = (int) ((double) qty / maxQty * (chartHeight - 40));
            int bx   = x0 + i * (barW + barGap);
            int by   = y0 + chartHeight - barH - 20;

            // bar
            g2.setColor(BAR_COLORS[i % BAR_COLORS.length]);
            g2.fill(new RoundRectangle2D.Double(bx, by, barW, barH, 6, 6));

            // value label on top
            g2.setColor(new Color(30, 41, 59));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String valStr = String.valueOf(qty);
            int valW = g2.getFontMetrics().stringWidth(valStr);
            g2.drawString(valStr, bx + (barW - valW) / 2, by - 6);

            // product name below (rotated or truncated)
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(new Color(100, 116, 139));
            String truncName = name.length() > 12 ? name.substring(0, 11) + "…" : name;
            int lblW = g2.getFontMetrics().stringWidth(truncName);
            g2.drawString(truncName, bx + (barW - lblW) / 2, y0 + chartHeight + 10);
        }

        // y-axis label
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(new Color(100, 116, 139));
        // rotated "Units Sold"
        java.awt.Graphics2D g3 = (Graphics2D) g2.create();
        g3.rotate(-Math.PI / 2, 14, chartHeight / 2);
        g3.drawString("Units Sold", 14 - chartHeight / 2 + 10, chartHeight / 2);
        g3.dispose();
    }
}
