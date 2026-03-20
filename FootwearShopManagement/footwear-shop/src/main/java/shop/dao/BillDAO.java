package shop.dao;

import shop.model.Bill;
import shop.model.BillItem;
import shop.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final BillDAO INSTANCE = new BillDAO();
    public static BillDAO getInstance() { return INSTANCE; }

    private Connection conn() { return DatabaseUtil.getInstance().getConnection(); }

    // ── Generate next bill number ──────────────────────────
    public String generateBillNumber() {
        String sql = "SELECT MAX(id) as max_id FROM bills";
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int next = 1;
            if (rs.next() && !rs.wasNull()) next = rs.getInt("max_id") + 1;
            return String.format("BILL-%05d", next);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // ── Create a full bill with items (transactional) ─────
    public Bill createBill(Bill bill) {
        Connection c = conn();
        try {
            c.setAutoCommit(false);

            // insert bill header
            String sql = """
                INSERT INTO bills (bill_number, customer_id, employee_id, transaction_type,
                                   total_amount, discount, final_amount, payment_method, bill_date, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, bill.getBillNumber());
                ps.setInt(2, bill.getCustomerId());
                ps.setInt(3, bill.getEmployeeId());
                ps.setString(4, bill.getTransactionType());
                ps.setDouble(5, bill.getTotalAmount());
                ps.setDouble(6, bill.getDiscount());
                ps.setDouble(7, bill.getFinalAmount());
                ps.setString(8, bill.getPaymentMethod());
                ps.setString(9, bill.getBillDate());
                ps.setString(10, bill.getNotes());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) bill.setId(keys.getInt(1));
                }
            }

            // insert bill items
            String itemSql = """
                INSERT INTO bill_items (bill_id, product_id, quantity, unit_price, item_total)
                VALUES (?, ?, ?, ?, ?)
            """;
            try (PreparedStatement ps = c.prepareStatement(itemSql)) {
                for (BillItem item : bill.getItems()) {
                    ps.setInt(1, bill.getId());
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getUnitPrice());
                    ps.setDouble(5, item.getItemTotal());
                    ps.executeUpdate();
                }
            }

            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException(e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        return bill;
    }

    // ── Record a return ────────────────────────────────────
    public void createReturn(int originalBillId, int productId, int quantity,
                             double refundAmount, int employeeId, int customerId, String reason) {
        Connection c = conn();
        try {
            c.setAutoCommit(false);

            // insert return record
            String sql = """
                INSERT INTO returns (original_bill_id, customer_id, employee_id, product_id,
                                     quantity, refund_amount, reason)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, originalBillId);
                ps.setInt(2, customerId);
                ps.setInt(3, employeeId);
                ps.setInt(4, productId);
                ps.setInt(5, quantity);
                ps.setDouble(6, refundAmount);
                ps.setString(7, reason);
                ps.executeUpdate();
            }

            // add stock back
            ProductDAO.getInstance().adjustQuantity(productId, quantity);

            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException(e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ── Record an exchange ─────────────────────────────────
    public void createExchange(int originalBillId, int oldProductId, int newProductId,
                               int oldQty, int newQty, double priceDiff,
                               int employeeId, int customerId, String notes) {
        Connection c = conn();
        try {
            c.setAutoCommit(false);

            String sql = """
                INSERT INTO exchanges (original_bill_id, customer_id, employee_id,
                                       old_product_id, new_product_id, old_quantity, new_quantity,
                                       price_difference, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, originalBillId);
                ps.setInt(2, customerId);
                ps.setInt(3, employeeId);
                ps.setInt(4, oldProductId);
                ps.setInt(5, newProductId);
                ps.setInt(6, oldQty);
                ps.setInt(7, newQty);
                ps.setDouble(8, priceDiff);
                ps.setString(9, notes);
                ps.executeUpdate();
            }

            // adjust inventory: add back old, deduct new
            ProductDAO.getInstance().adjustQuantity(oldProductId,  oldQty);
            ProductDAO.getInstance().adjustQuantity(newProductId, -newQty);

            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException(e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ── Fetch bill with items ──────────────────────────────
    public Bill findById(int id) {
        String sql = "SELECT * FROM bills WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill bill = mapBillRow(rs);
                    bill.setItems(findItemsByBillId(id));
                    return bill;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Bill findByBillNumber(String billNumber) {
        String sql = "SELECT * FROM bills WHERE bill_number = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, billNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill bill = mapBillRow(rs);
                    bill.setItems(findItemsByBillId(bill.getId()));
                    return bill;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    // ── Report queries ─────────────────────────────────────
    /** Get all SALE bills within a date range */
    public List<Bill> findSalesByDateRange(String startDate, String endDate) {
        String sql = """
            SELECT b.*, c.name as customer_name FROM bills b
            LEFT JOIN customers c ON b.customer_id = c.id
            WHERE b.transaction_type = 'SALE'
              AND date(b.bill_date) BETWEEN ? AND ?
            ORDER BY b.bill_date DESC
        """;
        return queryBills(sql, startDate, endDate);
    }

    /** Total revenue in date range */
    public double getTotalRevenue(String startDate, String endDate) {
        String sql = """
            SELECT COALESCE(SUM(final_amount), 0) as total FROM bills
            WHERE transaction_type = 'SALE'
              AND date(bill_date) BETWEEN ? AND ?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** Total items sold in date range */
    public int getTotalItemsSold(String startDate, String endDate) {
        String sql = """
            SELECT COALESCE(SUM(bi.quantity), 0) as total FROM bill_items bi
            JOIN bills b ON bi.bill_id = b.id
            WHERE b.transaction_type = 'SALE'
              AND date(b.bill_date) BETWEEN ? AND ?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** Total number of bills (transactions) in date range */
    public int getTotalTransactions(String startDate, String endDate) {
        String sql = """
            SELECT COUNT(*) as cnt FROM bills
            WHERE transaction_type = 'SALE'
              AND date(bill_date) BETWEEN ? AND ?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** Most selling (top N) products in a date range */
    public List<Object[]> getTopSellingProducts(String startDate, String endDate, int limit) {
        String sql = """
            SELECT p.id, p.name, p.brand, p.sku, p.sell_price,
                   SUM(bi.quantity) as total_qty,
                   SUM(bi.item_total) as total_revenue
            FROM bill_items bi
            JOIN bills b ON bi.bill_id = b.id
            JOIN products p ON bi.product_id = p.id
            WHERE b.transaction_type = 'SALE'
              AND date(b.bill_date) BETWEEN ? AND ?
            GROUP BY p.id
            ORDER BY total_qty DESC
            LIMIT ?
        """;
        List<Object[]> results = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("sku"),
                        rs.getDouble("sell_price"),
                        rs.getInt("total_qty"),
                        rs.getDouble("total_revenue")
                    });
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return results;
    }

    /** Daily revenue breakdown for a given month (year-month as "yyyy-MM") */
    public List<Object[]> getDailyRevenueForMonth(int year, int month) {
        String sql = """
            SELECT date(bill_date) as sale_date,
                   COALESCE(SUM(final_amount), 0) as daily_total
            FROM bills
            WHERE transaction_type = 'SALE'
              AND strftime('%Y', bill_date) = ?
              AND strftime('%m', bill_date) = ?
            GROUP BY date(bill_date)
            ORDER BY sale_date
        """;
        List<Object[]> results = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, String.valueOf(year));
            ps.setString(2, String.format("%02d", month));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Object[]{rs.getString("sale_date"), rs.getDouble("daily_total")});
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return results;
    }

    // ── Helpers ─────────────────────────────────────────────
    private List<Bill> queryBills(String sql, String startDate, String endDate) {
        List<Bill> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill b = mapBillRow(rs);
                    try { b.setCustomerName(rs.getString("customer_name")); } catch (Exception ignored) {}
                    b.setItems(findItemsByBillId(b.getId()));
                    list.add(b);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private List<BillItem> findItemsByBillId(int billId) {
        String sql = """
            SELECT bi.*, p.name as product_name, p.sku as product_sku
            FROM bill_items bi
            JOIN products p ON bi.product_id = p.id
            WHERE bi.bill_id = ?
        """;
        List<BillItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setId(rs.getInt("id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductSku(rs.getString("product_sku"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setItemTotal(rs.getDouble("item_total"));
                    items.add(item);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return items;
    }

    private Bill mapBillRow(ResultSet rs) throws SQLException {
        Bill b = new Bill();
        b.setId(rs.getInt("id"));
        b.setBillNumber(rs.getString("bill_number"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setEmployeeId(rs.getInt("employee_id"));
        b.setTransactionType(rs.getString("transaction_type"));
        b.setTotalAmount(rs.getDouble("total_amount"));
        b.setDiscount(rs.getDouble("discount"));
        b.setFinalAmount(rs.getDouble("final_amount"));
        b.setPaymentMethod(rs.getString("payment_method"));
        b.setBillDate(rs.getString("bill_date"));
        b.setNotes(rs.getString("notes"));
        return b;
    }
}
