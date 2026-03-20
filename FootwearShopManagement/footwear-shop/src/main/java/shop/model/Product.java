package shop.model;

public class Product {
    private int id;
    private String sku;
    private String name;
    private String brand;
    private String category;
    private String size;
    private String color;
    private double costPrice;
    private double sellPrice;
    private int quantity;
    private String description;

    public Product() {}

    public Product(int id, String sku, String name, String brand, String category,
                   String size, String color, double costPrice, double sellPrice,
                   int quantity, String description) {
        this.id            = id;
        this.sku           = sku;
        this.name          = name;
        this.brand         = brand;
        this.category      = category;
        this.size          = size;
        this.color         = color;
        this.costPrice     = costPrice;
        this.sellPrice     = sellPrice;
        this.quantity      = quantity;
        this.description   = description;
    }

    // ── Getters & Setters ──────────────────────────────────
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getSku()              { return sku; }
    public void setSku(String s)        { this.sku = s; }

    public String getName()             { return name; }
    public void setName(String n)       { this.name = n; }

    public String getBrand()            { return brand; }
    public void setBrand(String b)      { this.brand = b; }

    public String getCategory()         { return category; }
    public void setCategory(String c)   { this.category = c; }

    public String getSize()             { return size; }
    public void setSize(String s)       { this.size = s; }

    public String getColor()            { return color; }
    public void setColor(String c)      { this.color = c; }

    public double getCostPrice()        { return costPrice; }
    public void setCostPrice(double p)  { this.costPrice = p; }

    public double getSellPrice()        { return sellPrice; }
    public void setSellPrice(double p)  { this.sellPrice = p; }

    public int getQuantity()            { return quantity; }
    public void setQuantity(int q)      { this.quantity = q; }

    public String getDescription()      { return description; }
    public void setDescription(String d){ this.description = d; }

    @Override
    public String toString() {
        return String.format("[%s] %s – %s (Size: %s, Color: %s) Qty: %d @ $%.2f",
                sku, name, brand, size, color, quantity, sellPrice);
    }
}
