package b2;

import java.time.LocalDate;

public class PersonalLoan extends LoanAccount
{
     String purpose;
     boolean isSecured;
     
   //constructors
     public PersonalLoan() 
	{
		super();
		this.purpose = "Not Given";
		this.isSecured = false;
	}

     public PersonalLoan(int accountNumber, String accountHolderName, String accountType, double currentBalance,
			LocalDate accountOpeningDate, String accountStatus,String occupation, double income, double loanAmount, double outstandingAmount, double interestRate,
			int tenure, double emiPaid, double emiRemaining,String purpose, boolean isSecured) 
	{
		super( accountNumber,  accountHolderName,  accountType,  currentBalance,
				 accountOpeningDate,  accountStatus, occupation,  income,  loanAmount,  outstandingAmount,  interestRate,
				 tenure,  emiPaid,  emiRemaining);
		this.purpose = purpose;
		this.isSecured = isSecured;
	}
	//setters and getters
     public String getPurpose() {
		return purpose;
	}

     public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

     public boolean isSecured() {
		return isSecured;
	}

     public void setSecured(boolean isSecured) {
		this.isSecured = isSecured;
	}
	//display
	@Override
	public void displayAccountDetails() 
	{
        super.displayAccountDetails();
        System.out.println("Purpose    : " + this.purpose);
        System.out.println("Is Secured : " + this.isSecured);
    }
   //methods
	public void checkEligibility()
    {
    	if(this.income > 700000 && this.isSecured == true)
    	{
    		System.out.println("Eligible");
    	}
    	else
    	{
    		System.out.println("Not Eligible");
    	}
    }
	public void emiDetails()
	{
			System.out.println("**************************EMI Details**************************");
		 	System.out.println("Loan Amount			: " + this.loanAmount);
	        System.out.println("Outstanding Amount	: " + this.outstandingAmount);
	        System.out.println("Interest Rate		: " + this.interestRate);
	        System.out.println("Tenure				: " + this.tenure + " months");
	        System.out.println("EMI Paid			: " + this.emiPaid);
	        System.out.println("EMI Remaining		: " + this.emiRemaining);
	    
	}
	@Override
	public void calEmi()
	{
	    	double monthlyRate = this.interestRate / (12 * 100); 
		    int months = this.tenure;

		    if (monthlyRate == 0) {
		       
		        double emi = this.loanAmount / months;
		        System.out.println("EMI (No interest) : " + emi);
		        return;
		    }

		    double emi = (this.loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
		                 (Math.pow(1 + monthlyRate, months) - 1);

		    System.out.println("Calculated EMI: " + emi);

		  
		    this.emiRemaining = months;
	}
	@Override
	public void payEmi()
	{
		    double monthlyRate = this.interestRate / (12 * 100);
		    int months = this.tenure;

		    if (emiRemaining <= 0) {
		        System.out.println("All EMIs paid.");
		        return;
		    }

		    double emi = (this.loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
		                 (Math.pow(1 + monthlyRate, months) - 1);

		    if (this.currentBalance >= emi) {
		        this.currentBalance -= emi;
		        this.outstandingAmount -= emi;
		        this.emiPaid++;
		        this.emiRemaining--;

		        System.out.println("EMI of ₹" + emi + " paid successfully.");
		        System.out.println("Remaining EMIs: " + this.emiRemaining);
		        System.out.println("Updated Outstanding Amount: " + this.outstandingAmount);
		        System.out.println("Updated Balance: " + this.currentBalance);
		    } else {
		        System.out.println("Insufficient balance to pay EMI.");
		    }

	}
}//class PersonalLoan ends here
