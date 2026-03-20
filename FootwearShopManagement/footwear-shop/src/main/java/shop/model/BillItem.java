package shop.model;

public class BillItem {
    private int id;
    private int billId;
    private int productId;
    private String productName;   // denormalized for display
    private String productSku;
    private int quantity;
    private double unitPrice;
    private double itemTotal;

    public BillItem() {}

    public BillItem(int productId, String productName, String productSku,
                    int quantity, double unitPrice) {
        this.productId   = productId;
        this.productName = productName;
        this.productSku  = productSku;
        this.quantity    = quantity;
        this.unitPrice   = unitPrice;
        this.itemTotal   = quantity * unitPrice;
    }

    public void recalculate() {
        this.itemTotal = quantity * unitPrice;
    }

    // ── Getters & Setters ──────────────────────────────────
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public int getBillId()                  { return billId; }
    public void setBillId(int b)            { this.billId = b; }

    public int getProductId()               { return productId; }
    public void setProductId(int p)         { this.productId = p; }

    public String getProductName()          { return productName; }
    public void setProductName(String n)    { this.productName = n; }

    public String getProductSku()           { return productSku; }
    public void setProductSku(String s)     { this.productSku = s; }

    public int getQuantity()                { return quantity; }
    public void setQuantity(int q)          { this.quantity = q; recalculate(); }

    public double getUnitPrice()            { return unitPrice; }
    public void setUnitPrice(double u)      { this.unitPrice = u; recalculate(); }

    public double getItemTotal()            { return itemTotal; }
    public void setItemTotal(double t)      { this.itemTotal = t; }
}
