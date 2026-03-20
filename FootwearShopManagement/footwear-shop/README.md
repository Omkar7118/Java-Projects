# 👟 Footwear Shop Management System

A full-featured **Java Swing desktop application** for managing a footwear retail shop — billing, inventory, employee management, and analytics all in one place.

---

## 🚀 Quick Start

### Prerequisites
| Tool | Version |
|------|---------|
| JDK  | **21 or higher** (`javac` + `java` on PATH) |
| Internet | Required on **first run only** (downloads SQLite JDBC driver ~2 MB) |

### Windows
```bash
double-click  run.bat
```

### macOS / Linux / WSL
```bash
chmod +x run.sh
./run.sh
```

> The script automatically downloads the SQLite JDBC driver, compiles every `.java` file, and launches the app. A `footwear_shop.db` database file is created in the project folder on first run.

### Default Login Credentials
| Role     | Username | Password  |
|----------|----------|-----------|
| **Owner** | `owner`  | `admin123` |

---

## 📂 Project Structure

```
footwear-shop/
├── run.sh                          ← Linux / macOS build + run script
├── run.bat                         ← Windows  build + run script
├── README.md
└── src/main/java/shop/
    ├── App.java                    ← Entry point
    ├── model/                      ← POJOs / data classes
    │   ├── Employee.java
    │   ├── Product.java
    │   ├── Customer.java
    │   ├── Bill.java
    │   └── BillItem.java
    ├── dao/                        ← Data-Access Objects (SQL layer)
    │   ├── EmployeeDAO.java
    │   ├── ProductDAO.java
    │   ├── CustomerDAO.java
    │   └── BillDAO.java
    ├── util/                       ← Shared utilities
    │   ├── DatabaseUtil.java       ← SQLite connection + schema init
    │   ├── PasswordUtil.java       ← SHA-256 password hashing
    │   └── AppSession.java         ← Logged-in user singleton
    └── ui/                         ← Swing GUI
        ├── LoginPanel.java         ← Login screen
        ├── MainFrame.java          ← Root JFrame
        └── panels/
            ├── DashboardPanel.java ← Sidebar nav + content switcher
            ├── HomePanel.java      ← Welcome / today's summary cards
            ├── BillingPanel.java   ← Sell / Return / Exchange
            ├── InventoryPanel.java ← Product CRUD
            ├── EmployeePanel.java  ← Employee CRUD (owner only)
            └── ReportPanel.java    ← Analytics + bar chart
```

---

## ✅ Features by Role

### 👑 Owner
| Module | Capabilities |
|--------|--------------|
| **Billing** | Create sales, process returns & exchanges, print receipts |
| **Inventory** | Add / Edit / Delete products, restock, search |
| **Reports** | Date-range filters, KPI cards, top-selling products, bar chart, full sales list |
| **Employees** | Add / Edit / Delete employees, change passwords |

### 👤 Employee
| Module | Capabilities |
|--------|--------------|
| **Billing** | Create sales, process returns & exchanges, print receipts |
| **Inventory** | Add / Edit / Delete products, restock, search |
| **Reports** | Date-range filters, KPI cards, top-selling products, bar chart, full sales list |

> The **Employees** tab is hidden in the sidebar for the Employee role.

---

## 📊 Module Details

### 🏠 Home
Displays four live KPI cards for **today**: Revenue, Items Sold, Transactions, and Total Products in stock.

### 💰 Billing
* **New Sale** – search & add products to a cart, set customer info, apply a discount, choose payment method (Cash / Card / UPI), and checkout with an on-screen receipt.
* **Return** – enter an original bill number, pick the item & quantity, provide an optional reason; stock is automatically restored.
* **Exchange** – enter an original bill number, select the old item, search for a replacement by SKU; inventory is adjusted on both sides automatically.

### 📦 Inventory
Full CRUD table with live search across name, brand, SKU, and category.  A dedicated **Restock** button lets you bump a product's quantity without opening the edit dialog.

### 👥 Employees *(Owner only)*
Full CRUD table.  A separate **Change Password** flow keeps the password fields out of the general edit dialog for security.

### 📊 Reports
* **Period buttons** – Today / This Week / This Month / This Year / Custom date range.
* **KPI cards** – Total Revenue, Items Sold, Transactions, Average Order Value.
* **Top Selling Products** – ranked table (top 10) with units sold and revenue.
* **Bar Chart** – colour-coded horizontal bars showing units sold per top product, drawn entirely with `Graphics2D` (no external charting library).
* **Sales List** – filterable table of every sale in the selected range.

---

## 🛠 Technology Stack
| Layer | Choice |
|-------|--------|
| Language | Java 21 |
| GUI | Swing (JPanel, JTable, JDialog …) |
| Database | SQLite via **xerial/sqlite-jdbc** (embedded, zero-config) |
| Hashing | SHA-256 (`java.security.MessageDigest`) |
| Build | Plain `javac` – no Maven / Gradle required |

---

## 💡 Tips
* The database file (`footwear_shop.db`) lives next to the script.  Delete it to start fresh.
* All monetary values are stored as `REAL` (double).  For a production system consider switching to `BigDecimal`.
* To add another owner account, log in as the default owner → Employees → Add (then manually change the role in the DB, or extend the UI).
