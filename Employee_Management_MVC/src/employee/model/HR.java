package employee.model;

import java.io.Serializable;

public class HR extends Employee implements Serializable 
{
	double commission;
	//constructors
	HR()
	{
		super();
		this.commission=0;
	}
	public HR(int a,String s,double b,double c)
	{
		super(a,s,b);
		this.id=a;
		this.name=s;
		this.salary=b;
		this.commission=c;
	}
	//setters
	void setCommission(double c)
	{
		this.commission=c;
	}
	//getters
	public double getCommision()
	{
		return this.commission;
	}
	//display
	void display()
	{
		super.display();
		System.out.println("Commission is "+this.commission);
	}
	public double calSal() {
		return this.salary+this.commission;
	}

}//class HR ends here

