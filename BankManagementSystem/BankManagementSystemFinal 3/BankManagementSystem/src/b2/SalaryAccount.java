package b2;

import java.time.LocalDate;

import extra.bank.Transaction;

public class SalaryAccount extends BankAccount 
{
     String employeeName;
     String companyName;
     LocalDate salaryCreditDate;
     
     //constructors
     public SalaryAccount() 
	{
		super();
		this.employeeName = "Not Given";
		this.companyName = "Not Given" ;
		this.salaryCreditDate = LocalDate.now();
	}

     public SalaryAccount(int accountNumber, String accountHolderName, String accountType, double currentBalance,
			LocalDate accountOpeningDate, String accountStatus,String employeeName, String companyName, LocalDate salaryCreditDate)
	{
		super( accountNumber,  accountHolderName,  accountType,  currentBalance,
				 accountOpeningDate,  accountStatus);
		this.employeeName = employeeName;
		this.companyName = companyName;
		this.salaryCreditDate = salaryCreditDate;
	}
	//setters and getters
     public String getEmployeeName() {
		return employeeName;
	}

     public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

     public String getCompanyName() {
		return companyName;
	}

     public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

     public LocalDate getSalaryCreditDate() {
		return salaryCreditDate;
	}

     public void setSalaryCreditDate(LocalDate salaryCreditDate) {
		this.salaryCreditDate = salaryCreditDate;
	}
    //display
	 @Override
	 public void displayAccountDetails() 
	 {
	        super.displayAccountDetails();
	        System.out.println("Employee Name      : " + this.employeeName);
	        System.out.println("Company            : " + this.companyName);
	        System.out.println("Salary Credit Date : " + this.salaryCreditDate);
	 }
	//methods
	 public void creditSalary(BankAccount Acc)
	 {
		 System.out.print("Enter amount : ");
		 double amount=sc.nextDouble();
		 this.currentBalance += amount;
		 LocalDate CreditDate=LocalDate.now();
		 System.out.println("Salary credit date : "+CreditDate);
		 System.out.println("Salary credit amount: "+amount);
		 System.out.println("New Balance after salary credited : "+this.currentBalance);
		 Transaction t=new Transaction();
		 t.generateTrancationForDeposit(Acc, amount);
		 
	 }
}//class SalaryAccount ends here
