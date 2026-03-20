package shop.dao;

import shop.model.Product;
import shop.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final ProductDAO INSTANCE = new ProductDAO();
    public static ProductDAO getInstance() { return INSTANCE; }

    private Connection conn() { return DatabaseUtil.getInstance().getConnection(); }

    // ── Create ─────────────────────────────────────────────
    public void create(Product p) {
        String sql = """
            INSERT INTO products (sku, name, brand, category, size, color,
                                  cost_price, sell_price, quantity, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getSku());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getCategory());
            ps.setString(5, p.getSize());
            ps.setString(6, p.getColor());
            ps.setDouble(7, p.getCostPrice());
            ps.setDouble(8, p.getSellPrice());
            ps.setInt(9, p.getQuantity());
            ps.setString(10, p.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // ── Read ────────────────────────────────────────────────
    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Product findBySku(String sku) {
        String sql = "SELECT * FROM products WHERE sku = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, sku);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY id";
        List<Product> list = new ArrayList<>();
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    /** Search by name, brand, sku, or category (case-insensitive) */
    public List<Product> search(String keyword) {
        String sql = """
            SELECT * FROM products
            WHERE LOWER(name) LIKE ? OR LOWER(brand) LIKE ?
               OR LOWER(sku) LIKE ? OR LOWER(category) LIKE ?
            ORDER BY id
        """;
        String k = "%" + keyword.toLowerCase() + "%";
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, k); ps.setString(2, k);
            ps.setString(3, k); ps.setString(4, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    // ── Update ──────────────────────────────────────────────
    public void update(Product p) {
        String sql = """
            UPDATE products SET name=?, brand=?, category=?, size=?, color=?,
                   cost_price=?, sell_price=?, quantity=?, description=?, updated_at=datetime('now')
            WHERE id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getCategory());
            ps.setString(4, p.getSize());
            ps.setString(5, p.getColor());
            ps.setDouble(6, p.getCostPrice());
            ps.setDouble(7, p.getSellPrice());
            ps.setInt(8, p.getQuantity());
            ps.setString(9, p.getDescription());
            ps.setInt(10, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** Adjust stock quantity (can be + or -) */
    public void adjustQuantity(int productId, int delta) {
        String sql = "UPDATE products SET quantity = quantity + ?, updated_at=datetime('now') WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // ── Delete ──────────────────────────────────────────────
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean skuExists(String sku) {
        String sql = "SELECT COUNT(*) as cnt FROM products WHERE sku = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, sku);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getString("sku"),
            rs.getString("name"),
            rs.getString("brand"),
            rs.getString("category"),
            rs.getString("size"),
            rs.getString("color"),
            rs.getDouble("cost_price"),
            rs.getDouble("sell_price"),
            rs.getInt("quantity"),
            rs.getString("description")
        );
    }
}
