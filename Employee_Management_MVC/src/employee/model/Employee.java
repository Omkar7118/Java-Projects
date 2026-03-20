package employee.model;

import java.io.Serializable;

public class Employee implements Serializable 
{
	int id;
	String name;
	double salary;
	
//	Constructors
	
	public Employee()
	{
		this.id=100;
		this.name="Unknown";
		this.salary=0;
	}
	public Employee(int a,String s,double b)
	{
		this.id=a;
		this.name=s;
		this.salary=b;
	}
	
// 	Getters and Setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
//	Methods
	
	void display()
	{
		System.out.println("\nId is "+this.id);
		System.out.println("Name is "+this.name);
		System.out.println("Salary is "+this.salary);
	}

	public double calSal() {
		 return salary;
	 }
	
	public String toString() {
		return this.id+" "+this.name+" "+this.salary;
	}
	

	
	
		
}//class Emp ends here
