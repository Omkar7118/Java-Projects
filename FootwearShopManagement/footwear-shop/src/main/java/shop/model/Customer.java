package shop.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Customer() {}

    public Customer(int id, String name, String phone, String email, String address) {
        this.id      = id;
        this.name    = name;
        this.phone   = phone;
        this.email   = email;
        this.address = address;
    }

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getName()             { return name; }
    public void setName(String n)       { this.name = n; }

    public String getPhone()            { return phone; }
    public void setPhone(String p)      { this.phone = p; }

    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }

    public String getAddress()          { return address; }
    public void setAddress(String a)    { this.address = a; }

    @Override
    public String toString() {
        return name + (phone != null ? " – " + phone : "");
    }
}
