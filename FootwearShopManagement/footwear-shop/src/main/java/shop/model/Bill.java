package shop.model;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int id;
    private String billNumber;
    private int customerId;
    private String customerName;      // denormalized for display
    private int employeeId;
    private String transactionType;   // SALE | RETURN | EXCHANGE
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private String paymentMethod;     // CASH | CARD | UPI
    private String billDate;
    private String notes;
    private List<BillItem> items;

    public Bill() {
        this.items = new ArrayList<>();
        this.transactionType = "SALE";
        this.paymentMethod   = "CASH";
    }

    // ── Getters & Setters ──────────────────────────────────
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getBillNumber()           { return billNumber; }
    public void setBillNumber(String b)     { this.billNumber = b; }

    public int getCustomerId()              { return customerId; }
    public void setCustomerId(int c)        { this.customerId = c; }

    public String getCustomerName()         { return customerName; }
    public void setCustomerName(String n)   { this.customerName = n; }

    public int getEmployeeId()              { return employeeId; }
    public void setEmployeeId(int e)        { this.employeeId = e; }

    public String getTransactionType()      { return transactionType; }
    public void setTransactionType(String t){ this.transactionType = t; }

    public double getTotalAmount()          { return totalAmount; }
    public void setTotalAmount(double t)    { this.totalAmount = t; }

    public double getDiscount()             { return discount; }
    public void setDiscount(double d)       { this.discount = d; }

    public double getFinalAmount()          { return finalAmount; }
    public void setFinalAmount(double f)    { this.finalAmount = f; }

    public String getPaymentMethod()        { return paymentMethod; }
    public void setPaymentMethod(String p)  { this.paymentMethod = p; }

    public String getBillDate()             { return billDate; }
    public void setBillDate(String d)       { this.billDate = d; }

    public String getNotes()                { return notes; }
    public void setNotes(String n)          { this.notes = n; }

    public List<BillItem> getItems()        { return items; }
    public void setItems(List<BillItem> i)  { this.items = i; }

    public void addItem(BillItem item)      { items.add(item); }

    /** Recalculate totalAmount from items, then apply discount → finalAmount */
    public void recalculate() {
        totalAmount  = items.stream().mapToDouble(BillItem::getItemTotal).sum();
        finalAmount  = totalAmount - discount;
        if (finalAmount < 0) finalAmount = 0;
    }
}
