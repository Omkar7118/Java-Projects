package employee.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import employee.model.*;

public class EmployeeDAO {
    Scanner sc = new Scanner(System.in);
    ArrayList<Employee> employeeArray = new ArrayList<>();

//	 Constructor
    
    public EmployeeDAO(){
    	loadFromFile();
        if (employeeArray.isEmpty()) {
            // ----- HR Employees -----
            employeeArray.add(new HR(101, "Omkar", 50000, 5000));
            employeeArray.add(new HR(102, "Amit", 48000, 4500));
            employeeArray.add(new HR(103, "Sneha", 47000, 4000));

            // ----- Sales Managers -----
            employeeArray.add(new SalesManager(201, "Karan", 60000, 12, 8000));
            employeeArray.add(new SalesManager(202, "Meena", 58000, 15, 7500));
            employeeArray.add(new SalesManager(203, "Rohit", 62000, 10, 9000));

            // ----- Admin Employees -----
            employeeArray.add(new Admin(301, "Pragati", 70000, 10000));
            employeeArray.add(new Admin(302, "Isha", 68000, 9500));
            employeeArray.add(new Admin(303, "Pooja", 72000, 11000));
            employeeArray.add(new Admin(304, "Shubham", 75000, 12000));
        }
    }
    

//    Methods
    
    private void saveToFile() {
    	try (
    		FileOutputStream fos=new FileOutputStream("D://EmployeeData//employee.txt");
    		ObjectOutputStream oos=new ObjectOutputStream(fos);   	
    		){
    			oos.writeObject(employeeArray); 
    		}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    }

    private void loadFromFile() {
    	try (
    		FileInputStream fis=new FileInputStream("D://EmployeeData//employee.txt");
    		ObjectInputStream ois=new ObjectInputStream(fis); 	
    		){
    		employeeArray = (ArrayList<Employee>)ois.readObject();
    	}
    	catch(IOException | ClassNotFoundException e){
    		employeeArray = new ArrayList<>();
    	}
    }
    	
	public void addEmp(Employee employee) {
		
	    if (employee != null) {
	        employeeArray.add(employee);
	        saveToFile();
	        System.out.println("Employee added successfully!");
	    }

	}
	public void removeEmp(int tempId) {
		boolean done=false;
		Iterator<Employee>myit=employeeArray.iterator();
		while(myit.hasNext()) {
			Employee employee=myit.next();
			if(employee.getId()==tempId) {
				myit.remove();
				done=true;
			}
		}
		if(done) { 
			System.out.println(tempId+" Removed Successfully");
			saveToFile();
		}
		else
			System.out.println("Failed to Remove");
	}
	
	public void updateEmp(int tempId) {
		
		boolean found=false;
		System.out.println("Enter");
		System.out.println("1.Id update");
		System.out.println("2.Name update");
		System.out.println("3.Salary update");
		int choice=sc.nextInt();
		switch(choice) {
		case 1:
			System.out.println("Enter id : ");
			int empId=sc.nextInt();
			for(Employee employee:employeeArray) {
			if(employee.getId()==tempId) {
				found=true;
				employee.setId(empId);
				
				}
			}
			break;
		case 2:
			System.out.println("Enter name : ");
			String empName=sc.next();
			for(Employee employee:employeeArray) {
			if(employee.getId()==tempId) {
				employee.setName(empName);
				found=true;
				}
			}
			break;
		case 3:
			System.out.println("Enter salary : ");
			double empSalary=sc.nextDouble();
			for(Employee employee:employeeArray) {
			if(employee.getId()==tempId) {
				employee.setSalary(empSalary);
				found=true;
				}
			}
			break;
		}
		if(found) {
			saveToFile();
			System.out.println("Update successfully");
		}
		else
			System.out.println("Update failed");
		}
	
	public void searchEmp(int tempID) {

		boolean found=false;
		for(Employee employee:employeeArray) {
		if(employee.getId()==tempID) {
			found=true;
			}
		}
		if(found){
			System.out.println("Employee Found");
		}
		else {
			System.out.println("Employee not Found");
		}
	}
	
	public void sortEmp() {
		System.out.println("Sorting Menu");
		System.out.println("1.Sort on Id");
		System.out.println("2.Sort on Name");
		System.out.println("3.Sort on Salary");
		int choice=sc.nextInt();
		switch(choice) {
		case 1:
			MyEmployeeIdComparator m1=new MyEmployeeIdComparator();
			Collections.sort(employeeArray,m1);
		case 2:
			MyEmployeeNameComparator m2=new MyEmployeeNameComparator();
			Collections.sort(employeeArray,m2);
		case 3:
			MyEmployeeSalaryComparator m3=new MyEmployeeSalaryComparator();
			Collections.sort(employeeArray,m3);
		}
	}
	
	public void calSalEmp(int tempId) {

		int index=0;
		for(Employee employee:employeeArray) {
			if(employee.getId()==tempId) {
				System.out.println("Total Salary is "+employee.calSal());
			}
		}
	}
	
	public void displayEmp() {
		for(Employee employee:employeeArray) {
			if(employee instanceof HR) {
				System.out.print(employee+" ");
				HR hr=(HR)employee;
				System.out.println(hr.getCommision());
			}
			if(employee instanceof Admin) {
				System.out.print(employee+" ");
				Admin admin=(Admin)employee;
				System.out.println(admin.getAllowance());
			}
			if(employee instanceof SalesManager) {
				System.out.print(employee+" ");
				SalesManager salesManager=(SalesManager)employee;
				System.out.println(salesManager.getIncentive()+" "+salesManager.getTarget());
			}
		}
	}
}

