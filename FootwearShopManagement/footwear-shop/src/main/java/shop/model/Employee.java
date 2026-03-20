package shop.model;

public class Employee {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role; // OWNER or EMPLOYEE
    private String phone;
    private String email;
    private String hireDate;
    private boolean active;

    public Employee() {}

    public Employee(int id, String username, String password, String fullName,
                    String role, String phone, String email, String hireDate, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.hireDate = hireDate;
        this.active = active;
    }

    // ── Getters & Setters ──────────────────────────────────
    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getUsername()        { return username; }
    public void setUsername(String u)   { this.username = u; }

    public String getPassword()        { return password; }
    public void setPassword(String p)   { this.password = p; }

    public String getFullName()        { return fullName; }
    public void setFullName(String n)   { this.fullName = n; }

    public String getRole()            { return role; }
    public void setRole(String r)       { this.role = r; }

    public String getPhone()           { return phone; }
    public void setPhone(String p)      { this.phone = p; }

    public String getEmail()           { return email; }
    public void setEmail(String e)      { this.email = e; }

    public String getHireDate()        { return hireDate; }
    public void setHireDate(String d)   { this.hireDate = d; }

    public boolean isActive()          { return active; }
    public void setActive(boolean a)    { this.active = a; }

    @Override
    public String toString() {
        return fullName + " (" + username + ") – " + role;
    }
}
