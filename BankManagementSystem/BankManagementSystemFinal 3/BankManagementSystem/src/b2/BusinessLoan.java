package b2;

import java.time.LocalDate;

public class BusinessLoan extends LoanAccount 
{
     String businessType;
     int yearsInBusiness;
     //constructors
     public BusinessLoan() 
     {
 		super();
 		this.businessType = "NOT Given";
 		this.yearsInBusiness = 0;
 	}
     public BusinessLoan(int accountNumber, String accountHolderName, String accountType, double currentBalance,
 			LocalDate accountOpeningDate, String accountStatus,String occupation, double income, double loanAmount, double outstandingAmount, double interestRate,
 			int tenure, double emiPaid, double emiRemaining,String businessType, int yearsInBusiness) 
     {
 		super(accountNumber,  accountHolderName,  accountType,  currentBalance,
				 accountOpeningDate,  accountStatus, occupation,  income,  loanAmount,  outstandingAmount,  interestRate,
				 tenure,  emiPaid,  emiRemaining);
 		this.businessType = businessType;
 		this.yearsInBusiness = yearsInBusiness;
 	}
     //setters and getters
     public String getBusinessType() {
		return businessType;
	}
     public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
     public int getYearsInBusiness() {
		return yearsInBusiness;
	}
     public void setYearsInBusiness(int yearsInBusiness) {
		this.yearsInBusiness = yearsInBusiness;
	}
    //display
	 @Override
	 public void displayAccountDetails() 
	 {
	        super.displayAccountDetails();
	        System.out.println("Business Type	  : " + this.businessType);
	        System.out.println("Years in Business : " + this.yearsInBusiness);
	 }
	//methods
	 public void checkEligibility()
	    {
	 		if(this.income > 100000 && this.yearsInBusiness > 2)
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
	 		        System.out.println("EMI (No interest) : ₹" + emi);
	 		        return;
	 		    }

	 		    double emi = (this.loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
	 		                 (Math.pow(1 + monthlyRate, months) - 1);

	 		    System.out.println("Calculated EMI for Business Loan: ₹" + emi);

	 		    
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

	 		    if (this.currentBalance>= emi) {
	 		        this.outstandingAmount -= emi;
	 		        this.emiPaid++;
	 		        this.emiRemaining--;
	 		        this.currentBalance -= emi;

	 		        System.out.println("EMI of ₹" + emi + " paid successfully.");
	 		        System.out.println("Remaining EMIs: " + this.emiRemaining);
	 		    } else {
	 		        System.out.println("Insufficient balance to pay EMI.");
	 		    }
	 
		}
}//class BusinessLoan ends here
