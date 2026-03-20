package shop.dao;

import shop.model.Employee;
import shop.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static final EmployeeDAO INSTANCE = new EmployeeDAO();
    public static EmployeeDAO getInstance() { return INSTANCE; }

    private Connection conn() { return DatabaseUtil.getInstance().getConnection(); }

    // ── Login ──────────────────────────────────────────────
    public Employee findByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    // ── CRUD ───────────────────────────────────────────────
    public void create(Employee emp) {
        String sql = """
            INSERT INTO employees (username, password, full_name, role, phone, email, hire_date, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, emp.getUsername());
            ps.setString(2, emp.getPassword());
            ps.setString(3, emp.getFullName());
            ps.setString(4, emp.getRole());
            ps.setString(5, emp.getPhone());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getHireDate());
            ps.setInt(8, emp.isActive() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void update(Employee emp) {
        String sql = """
            UPDATE employees SET full_name=?, role=?, phone=?, email=?, hire_date=?, is_active=?
            WHERE id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, emp.getFullName());
            ps.setString(2, emp.getRole());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getEmail());
            ps.setString(5, emp.getHireDate());
            ps.setInt(6, emp.isActive() ? 1 : 0);
            ps.setInt(7, emp.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void updatePassword(int id, String hashedPassword) {
        String sql = "UPDATE employees SET password = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Employee findById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees ORDER BY id";
        List<Employee> list = new ArrayList<>();
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public List<Employee> findAllEmployees() {
        // excludes OWNER for display in management table
        String sql = "SELECT * FROM employees WHERE role = 'EMPLOYEE' ORDER BY id";
        List<Employee> list = new ArrayList<>();
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as cnt FROM employees WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("full_name"),
            rs.getString("role"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("hire_date"),
            rs.getInt("is_active") == 1
        );
    }
}
