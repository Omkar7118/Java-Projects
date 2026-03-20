package employee.model;

import java.io.Serializable;

public class SalesManager extends Employee implements Serializable 
{
	int target;
	double incentive;
//	Constructors
	SalesManager(){
			super();
			this.target = 0;
			this.incentive = 0;
		}
	public SalesManager(int a,String s,double b,int target, double incentive) {
		super(a,s,b);
		this.target = target;
		this.incentive = incentive;
	}
// setters	
	public void setIncentive(double c)
	{
		this.incentive=c;
	}
	public void setTarget(int d)
	{
		this.target=d;
	}
//	getters
	public double getIncentive()
	{
		return this.incentive;
	}
	public int getTarget()
	{
		return this.target;
	}
	void display()
	{
		super.display();
		System.out.println("Id is "+this.incentive);
		System.out.println("Target is "+this.target);		
	}
	public double calSal() {
		return this.salary+this.incentive;
	}

}//class SalesManager ends here

