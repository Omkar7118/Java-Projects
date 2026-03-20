package b2;

import java.time.LocalDate;

public abstract class LoanAccount extends BankAccount 
{
     String occupation;
     double income;
     double loanAmount;
     double outstandingAmount;
     double interestRate;
     int tenure;
     double emiPaid;
     double emiRemaining;
     
     //constructors
     public LoanAccount()
	{
		super();
		this.occupation = "Not Given";
		this.income = 0.0;
		this.loanAmount = 0.0;
		this.outstandingAmount = 0.0;
		this.interestRate = 0.0;
		this.tenure = 0;
		this.emiPaid = 0.0;
		this.emiRemaining = 0.0;
	}
     public LoanAccount(int accountNumber, String accountHolderName, String accountType, double currentBalance,
			LocalDate accountOpeningDate, String accountStatus,String occupation, double income, double loanAmount, double outstandingAmount, double interestRate,
			int tenure, double emiPaid, double emiRemaining) 
	{
		super( accountNumber,  accountHolderName,  accountType,  currentBalance,
				 accountOpeningDate,  accountStatus);
		this.occupation = occupation;
		this.income = income;
		this.loanAmount = loanAmount;
		this.outstandingAmount = outstandingAmount;
		this.interestRate = interestRate;
		this.tenure = tenure;
		this.emiPaid = emiPaid;
		this.emiRemaining = emiRemaining;
	}
	//setters and getters
     public String getOccupation() {
		return occupation;
	}
     public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
     public double getIncome() {
		return income;
	}
     public void setIncome(double income) {
		this.income = income;
	}
     public double getLoanAmount() {
		return loanAmount;
	}
     public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
     public double getOutstandingAmount() {
		return outstandingAmount;
	}
     public void setOutstandingAmount(double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public int getTenure() {
		return tenure;
	}
	public void setTenure(int tenure) {
		this.tenure = tenure;
	}
	public double getEmiPaid() {
		return emiPaid;
	}
	public void setEmiPaid(double emiPaid) {
		this.emiPaid = emiPaid;
	}
	double getEmiRemaining() {
		return emiRemaining;
	}
	public void setEmiRemaining(double emiRemaining) {
		this.emiRemaining = emiRemaining;
	}
	//display
	@Override
	public void displayAccountDetails() 
	{
		super.displayAccountDetails();
        System.out.println("Occupation			: " + this.occupation);
        System.out.println("Loan Amount			: " + this.loanAmount);
        System.out.println("Outstanding Amount	: " + this.outstandingAmount);
        System.out.println("Interest Rate		: " + this.interestRate);
        System.out.println("Tenure				: " + this.tenure + " months");
        System.out.println("EMI Paid			: " + this.emiPaid);
        System.out.println("EMI Remaining		: " + this.emiRemaining);
    }
    //methods
	abstract void checkEligibility();
	abstract void emiDetails();
	abstract void calEmi();
	abstract void payEmi();
	
}//class LoanAccount ends here
