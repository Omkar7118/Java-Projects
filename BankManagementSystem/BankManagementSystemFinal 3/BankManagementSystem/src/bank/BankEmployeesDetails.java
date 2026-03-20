package bank;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BankEmployeesDetails {
	Scanner sc=new Scanner(System.in);
    static ArrayList<BankEmployee> bankEmployees = new ArrayList<BankEmployee>();
   
    static {
    	bankEmployees.add(new BankEmployee(201, "Neha Patil", "neha@123", Designation.BRANCH_MANAGER));
    	bankEmployees.add(new BankEmployee(202, "Ramesh Joshi", "ramesh@456", Designation.CUSTOMER_SERVICE_EXECUTIVE));
    	bankEmployees.add(new BankEmployee(203, "Priya Singh", "priya@789", Designation.CUSTOMER_SERVICE_EXECUTIVE));
    	bankEmployees.add(new BankEmployee(204, "Sanjay Kulkarni", "sanjay@321", Designation.CUSTOMER_SERVICE_EXECUTIVE));
    }
    
//    Methods
    
    boolean verifyUser(int id, String password) {
        HomePage homepage = new HomePage();
        for (BankEmployee employee : bankEmployees) {
            if (employee.getEmployeeId() == id && employee.getPassword().equals(password)) {
                System.out.println("\nLogin successful. Welcome " + employee.getEmployeeName() + "!");
                homepage.MainMenu(employee.getDesignation());
                return true; // login worked
            }
        }
        return false; // login failed
    }
    
    void addEmployee() {
        int employeeId; 
        String employeeName;
        String password;
        Designation designation;

        System.out.println("Enter employee id:");
        employeeId = sc.nextInt();
        sc.nextLine(); // consume leftover newline

        System.out.println("Enter employee name:");
        employeeName = sc.nextLine();

        System.out.println("Enter employee password:");
        password = sc.nextLine();

        System.out.println("Enter employee designation:");
        System.out.println("Options: BRANCH_MANAGER, CUSTOMER_SERVICE_EXECUTIVE");

        String input = sc.nextLine().trim().toUpperCase();

        try {
            designation = Designation.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid designation. Please enter one of the listed options.");
            return; // exit method if invalid input
        }

        BankEmployee newEmployee = new BankEmployee();
        newEmployee.setEmployeeId(employeeId);
        newEmployee.setEmployeeName(employeeName);
        newEmployee.setPassword(password);
        newEmployee.setDesignation(designation);

        bankEmployees.add(newEmployee);
        Bank bank=new Bank();
        bank.addCountEmployee();
        System.out.println("Employee added successfully.");
    }

    
    void removeEmployee() {
        System.out.println("Enter employee ID: ");
        int id = sc.nextInt();

        Iterator<BankEmployee> it = bankEmployees.iterator();
        boolean removed = false;

        while(it.hasNext()) {
            BankEmployee employee = it.next();
            if(employee.getEmployeeId() == id) {
                it.remove(); 
                removed = true;
                Bank bank=new Bank();
                bank.removeCountEmployee();
                System.out.println("Employee removed successfully: " + employee.getEmployeeName());
                break;
            }
        }

        if(!removed) {
            System.out.println("Employee with ID " + id + " not found.");
        }
    }

    void displayAllEmployees() {
    	System.out.println(bankEmployees);
    }
}
