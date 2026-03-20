package employee.model;

import java.io.Serializable;

public class Admin extends Employee implements Serializable 
{
	
	double allowance;
	//setters	
	public void setAllowance(double c)
	{
		this.allowance=c;
	}
	//getters
	public double getAllowance()
	{
		return this.allowance;
	}
	//display
	void display()
	{	
		super.display();
		System.out.println("Allowance is "+this.allowance);
	}
	//constructors
	Admin()
	{
		super();
		this.allowance=0;
	}
	public Admin(int a,String s,double b,double c)
	{
		super(a,s,b);
		this.allowance=c;
	}
	public double calSal() {
		return this.salary+this.allowance;
	}
}//class Admin ends here

