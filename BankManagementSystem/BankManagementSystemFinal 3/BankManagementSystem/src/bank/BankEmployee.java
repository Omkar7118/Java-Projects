package bank;

public class BankEmployee extends Bank {
    int employeeId;
    String employeeName;
    String password;
    Designation designation; 

    // Constructor
    
    public BankEmployee() {
        super();
        this.employeeId = 0;
        this.employeeName = "Not given";
        this.password = "Not Given";
        this.designation = null;
    }
    
    public BankEmployee(int employeeId, String employeeName, String password, Designation designation) {
        super();
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.password = password;
        this.designation = designation;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    
//    Methods
    
	public String toString() {
		return "BankEmployee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", password=" + password
				+ ", designation=" + designation + "]"+"\n";
	}
	
    
}
