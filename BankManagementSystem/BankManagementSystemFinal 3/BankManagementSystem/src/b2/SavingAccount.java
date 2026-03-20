package b2;

import java.time.LocalDate;

public class SavingAccount extends BankAccount 
{	     
		double minimumBalance;
	    double interestRate;
	    
	    //constructors
	    public SavingAccount() 
		{
			super();
			this.minimumBalance = 0.0;
			this.interestRate = 0.0;
		}
	    public SavingAccount(int accountNumber, String accountHolderName, String accountType, double currentBalance,
				LocalDate accountOpeningDate, String accountStatus,double minimumBalance, double interestRate)
		{
			super( accountNumber,  accountHolderName,  accountType,  currentBalance,
					 accountOpeningDate,  accountStatus);
			this.minimumBalance = minimumBalance;
			this.interestRate = interestRate;
		}
		//setters and getters
	    public double getMinimumBalance() {
			return minimumBalance;
		}
	    public void setMinimumBalance(double minimumBalance) {
			this.minimumBalance = minimumBalance;
		}
	    public double getInterestRate() {
			return interestRate;
		}
	    public void setInterestRate(double interestRate) {
			this.interestRate = interestRate;
		}
		//display
		@Override
		public void displayAccountDetails()
		{
			super.displayAccountDetails();
			System.out.println("Minimum Balance : " + this.minimumBalance);
	        System.out.println("Interest Rate   : " + this.interestRate);
		}
		//methods 
		public void checkPenalty(int tempAccNo)
	    {
	    	if(this.currentBalance < this.minimumBalance)
	    	{
	    		System.out.println(tempAccNo+" penalty of 100rs for not maintaining minimum balance");
	    	}
	    	else
	    	{
	    		 System.out.println("No penalty.");
	    	}
	    }
//		public void calculateInterest()
//	    {
//	    	double interest = this.currentBalance * (this.interestRate / 100);
//	        System.out.println("Interest : " +interest);	
//	    }	
		public void applyInterest()
	    {
	    	double interest= this.currentBalance * (this.interestRate/ 100);
	    	System.out.println("Interest : " +interest);
	    	this.currentBalance = this.currentBalance + interest;
	    	
	    	System.out.println("New Balance after applying interest : "+this.currentBalance);
	    }
}//class SavingAccount ends here
