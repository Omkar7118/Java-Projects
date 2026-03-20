package shop.ui.panels;

import shop.dao.BillDAO;
import shop.dao.ProductDAO;
import shop.util.AppSession;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 245, 249));
        buildUI();
    }

    private void buildUI() {
        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(241, 245, 249));
        header.setBorder(BorderFactory.createEmptyBorder(28, 32, 8, 32));
        JLabel title = new JLabel("Welcome back, " + AppSession.getInstance().getCurrentUser().getFullName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(15, 23, 42));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ── Stat cards ──
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        double todayRevenue   = BillDAO.getInstance().getTotalRevenue(today, today);
        int    todayItems     = BillDAO.getInstance().getTotalItemsSold(today, today);
        int    todayTxns      = BillDAO.getInstance().getTotalTransactions(today, today);
        int    totalProducts  = ProductDAO.getInstance().findAll().size();

        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 20));
        cards.setBackground(new Color(241, 245, 249));
        cards.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));

        cards.add(statCard("💰 Today's Revenue", String.format("$%.2f", todayRevenue), new Color(16, 185, 129)));
        cards.add(statCard("📦 Items Sold Today", String.valueOf(todayItems), new Color(59, 130, 246)));
        cards.add(statCard("🧾 Transactions Today", String.valueOf(todayTxns), new Color(139, 92, 246)));
        cards.add(statCard("🏷️  Total Products", String.valueOf(totalProducts), new Color(245, 158, 11)));

        add(cards, BorderLayout.CENTER);

        // ── footer hint ──
        JPanel footer = new JPanel();
        footer.setBackground(new Color(241, 245, 249));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 32, 24, 32));
        JLabel hint = new JLabel("Use the sidebar to navigate between modules.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        hint.setForeground(new Color(100, 116, 139));
        footer.add(hint);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel statCard(String label, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(100, 116, 139));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(new Color(15, 23, 42));

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }
}
