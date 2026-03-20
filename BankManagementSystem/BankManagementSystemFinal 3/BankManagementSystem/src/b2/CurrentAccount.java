package b2;

import java.time.LocalDate;

public class CurrentAccount extends BankAccount
{
	double overdraftLimit;
    double overdraftUsed;
	
    //constructors
    public  CurrentAccount()
    {
		super();
		this.overdraftLimit = 0.0;
		this.overdraftUsed = 0.0;
	}
    public CurrentAccount(int accountNumber, String accountHolderName, String accountType, double currentBalance,
			LocalDate accountOpeningDate, String accountStatus,double overdraftLimit, double overdraftUsed) 
    {
		super( accountNumber,  accountHolderName,  accountType,  currentBalance,
				 accountOpeningDate,  accountStatus);
		this.overdraftLimit = overdraftLimit;
		this.overdraftUsed = overdraftUsed;
	}
    //setters and getters
    public double getOverdraftLimit() {
		return overdraftLimit;
	}
	public void setOverdraftLimit(double overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}
	public double getOverdraftUsed() {
		return overdraftUsed;
	}
	public void setOverdraftUsed(double overdraftUsed) {
		this.overdraftUsed = overdraftUsed;
	}
    //display
	
	public void displayAccountDetails()
	{
		super.displayAccountDetails();
		System.out.println("Overdraft Limit : " + this.overdraftLimit);
        System.out.println("Overdraft Used  : " + this.overdraftUsed);
	}
	//methods
	public void checkOverDraft() 
	 {
		 System.out.println("Overdraft Used: " + overdraftUsed);
		 System.out.println("Overdraft Remaining: " + (overdraftLimit - overdraftUsed));
	 }
	 
}//class CurrentAccount ends here
