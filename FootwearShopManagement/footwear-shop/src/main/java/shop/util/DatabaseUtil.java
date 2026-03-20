package shop.util;

import java.sql.*;
import java.io.File;

public class DatabaseUtil {
    private static final String DB_PATH = System.getProperty("user.dir") + File.separator + "footwear_shop.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            enableForeignKeys();
            initializeTables();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    public static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                connection.setAutoCommit(true);
                enableForeignKeys();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection: " + e.getMessage(), e);
        }
        return connection;
    }

    private void enableForeignKeys() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    private void initializeTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // Users / Employees table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    full_name TEXT NOT NULL,
                    role TEXT NOT NULL DEFAULT 'EMPLOYEE',
                    phone TEXT,
                    email TEXT,
                    hire_date TEXT NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL DEFAULT (datetime('now'))
                )
            """);

            // Products / Inventory table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sku TEXT UNIQUE NOT NULL,
                    name TEXT NOT NULL,
                    brand TEXT NOT NULL,
                    category TEXT NOT NULL,
                    size TEXT NOT NULL,
                    color TEXT NOT NULL,
                    cost_price REAL NOT NULL DEFAULT 0,
                    sell_price REAL NOT NULL DEFAULT 0,
                    quantity INTEGER NOT NULL DEFAULT 0,
                    description TEXT,
                    created_at TEXT NOT NULL DEFAULT (datetime('now')),
                    updated_at TEXT NOT NULL DEFAULT (datetime('now'))
                )
            """);

            // Customers table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT,
                    email TEXT,
                    address TEXT,
                    created_at TEXT NOT NULL DEFAULT (datetime('now'))
                )
            """);

            // Bills / Transactions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bills (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bill_number TEXT UNIQUE NOT NULL,
                    customer_id INTEGER,
                    employee_id INTEGER NOT NULL,
                    transaction_type TEXT NOT NULL DEFAULT 'SALE',
                    total_amount REAL NOT NULL DEFAULT 0,
                    discount REAL NOT NULL DEFAULT 0,
                    final_amount REAL NOT NULL DEFAULT 0,
                    payment_method TEXT NOT NULL DEFAULT 'CASH',
                    bill_date TEXT NOT NULL DEFAULT (datetime('now')),
                    notes TEXT,
                    FOREIGN KEY (customer_id) REFERENCES customers(id),
                    FOREIGN KEY (employee_id) REFERENCES employees(id)
                )
            """);

            // Bill Items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bill_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bill_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL DEFAULT 1,
                    unit_price REAL NOT NULL,
                    item_total REAL NOT NULL,
                    FOREIGN KEY (bill_id) REFERENCES bills(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);

            // Exchange records
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS exchanges (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    original_bill_id INTEGER,
                    new_bill_id INTEGER,
                    customer_id INTEGER,
                    employee_id INTEGER NOT NULL,
                    old_product_id INTEGER NOT NULL,
                    new_product_id INTEGER NOT NULL,
                    old_quantity INTEGER NOT NULL,
                    new_quantity INTEGER NOT NULL,
                    price_difference REAL NOT NULL DEFAULT 0,
                    exchange_date TEXT NOT NULL DEFAULT (datetime('now')),
                    notes TEXT,
                    FOREIGN KEY (employee_id) REFERENCES employees(id),
                    FOREIGN KEY (old_product_id) REFERENCES products(id),
                    FOREIGN KEY (new_product_id) REFERENCES products(id)
                )
            """);

            // Returns records
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS returns (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    original_bill_id INTEGER,
                    return_bill_id INTEGER,
                    customer_id INTEGER,
                    employee_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL,
                    refund_amount REAL NOT NULL DEFAULT 0,
                    reason TEXT,
                    return_date TEXT NOT NULL DEFAULT (datetime('now')),
                    FOREIGN KEY (employee_id) REFERENCES employees(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);
        }

        // Seed a default owner account if none exists
        seedDefaultOwner();
    }

    private void seedDefaultOwner() throws SQLException {
        String checkSql = "SELECT COUNT(*) as cnt FROM employees WHERE role = 'OWNER'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt("cnt") == 0) {
                String insertSql = """
                    INSERT INTO employees (username, password, full_name, role, hire_date, is_active)
                    VALUES ('owner', '%s', 'Shop Owner', 'OWNER', date('now'), 1)
                """.formatted(PasswordUtil.hash("admin123"));
                try (Statement ins = connection.createStatement()) {
                    ins.execute(insertSql);
                }
            }
        }
    }
}
