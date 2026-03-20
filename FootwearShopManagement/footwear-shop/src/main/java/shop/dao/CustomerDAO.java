package shop.dao;

import shop.model.Customer;
import shop.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final CustomerDAO INSTANCE = new CustomerDAO();
    public static CustomerDAO getInstance() { return INSTANCE; }

    private Connection conn() { return DatabaseUtil.getInstance().getConnection(); }

    public Customer create(Customer c) {
        String sql = """
            INSERT INTO customers (name, phone, email, address)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return c;
    }

    public Customer findById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Customer> search(String keyword) {
        String sql = """
            SELECT * FROM customers
            WHERE LOWER(name) LIKE ? OR phone LIKE ?
            ORDER BY id
        """;
        String k = "%" + keyword.toLowerCase() + "%";
        List<Customer> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, k);
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers ORDER BY id";
        List<Customer> list = new ArrayList<>();
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("address")
        );
    }
}
