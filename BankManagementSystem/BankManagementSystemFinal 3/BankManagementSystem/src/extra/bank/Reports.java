package extra.bank;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reports {
	LocalDate dateTime;
	double totalLoanDistributed;
	double totalEmiCollected;
	double totalAmount;
	double totalActiveLoans;
    double totalDeposit = 0;
    double totalWithdraw = 0;
    double totalLoan = 0;
    double totalEmi = 0;
    int tempCount=0;
    
    Scanner sc =new Scanner(System.in);
	
	// Default Constructor
	
	public Reports() {
		this.dateTime = null;
		this.totalDeposit = 0;
		this.totalWithdraw = 0;
		this.totalLoanDistributed = 0;
		this.totalEmiCollected = 0;
		this.totalAmount = 0;
		this.totalActiveLoans = 0;
	}

	// Parameter Constructor
	
	public Reports(LocalDate dateTime, double totalDeposite, double totalwithdraw, double totalLoanDistributed,
			double totalEmiCollected, double totalAmount, double totalActiveLoans) {
		this.dateTime = dateTime;
		this.totalDeposit = totalDeposite;
		this.totalWithdraw = totalwithdraw;
		this.totalLoanDistributed = totalLoanDistributed;
		this.totalEmiCollected = totalEmiCollected;
		this.totalAmount = totalAmount;
		this.totalActiveLoans = totalActiveLoans;
	}
	
	// Getters and Setters

	public LocalDate getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDate dateTime) {
		this.dateTime = dateTime;
	}

	public double getTotalDeposite() {
		return totalDeposit;
	}

	public void setTotalDeposite(double totalDeposite) {
		this.totalDeposit = totalDeposite;
	}

	public double getTotalwithdraw() {
		return totalWithdraw;
	}

	public void setTotalwithdraw(double totalwithdraw) {
		this.totalWithdraw = totalwithdraw;
	}

	public double getTotalLoanDistributed() {
		return totalLoanDistributed;
	}

	public void setTotalLoanDistributed(double totalLoanDistributed) {
		this.totalLoanDistributed = totalLoanDistributed;
	}

	public double getTotalEmiCollected() {
		return totalEmiCollected;
	}

	public void setTotalEmiCollected(double totalEmiCollected) {
		this.totalEmiCollected = totalEmiCollected;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalActiveLoans() {
		return totalActiveLoans;
	}

	public void setTotalActiveLoans(double totalActiveLoans) {
		this.totalActiveLoans = totalActiveLoans;
	}
	
	// Calculate Reports
	


	public void calculateReports() {
	    Scanner sc = new Scanner(System.in);
	    int transactionsCount=0;

	    System.out.print("Enter date (yyyy-MM-dd): ");
	    String input = sc.next();  // Example input: 2025-10-18
	    LocalDate l1 = null;

	    try {
	        l1 = LocalDate.parse(input);  // Convert String → LocalDate
	    } catch (DateTimeParseException e) {
	        System.out.println("❌ Invalid date format! Please enter date as yyyy-MM-dd.");
	        return;  // Exit the method if date is invalid
	    }

	    this.dateTime = l1;  // set report date to user input

	    double totalDeposit = 0;
	    double totalWithdraw = 0;
	    double totalLoan = 0;
	    double totalEmi = 0;

	    ArrayList<Transaction> transactions = Transaction.getAllTransaction();

	    for (Transaction t : transactions) {
	        // Only consider transactions on the user-specified date
	        if (t.getDateTime().isEqual(l1)) {
	        	transactionsCount++;
	            TransactionType type = t.getTransactionType();
	            double amount = t.getAmount();

	            if (type == TransactionType.Deposit) {
	                totalDeposit += amount;
	            } else if (type == TransactionType.Withdraw) {
	                totalWithdraw += amount;
	            }
//	            else if (type == TransactionType.Loan) {
//	                totalLoan += amount;
//	            } else if (type == TransactionType.EMI) {
//	                totalEmi += amount;
//	            }
	        }
	    }
	    tempCount=transactionsCount;
	    this.totalDeposit = totalDeposit;
	    this.totalWithdraw = totalWithdraw;
	    this.totalLoanDistributed = totalLoan;
	    this.totalEmiCollected = totalEmi;

	    this.totalAmount = totalDeposit + totalEmi - totalWithdraw + totalLoan;

	    System.out.println("✅ Report Calculated Successfully for " + l1);
	}


	
	// Display Reports
	
	public void displayReports() {
		calculateReports();
        System.out.println("------ All Day Transaction Report ------");
        System.out.println("Date: " + dateTime);
        System.out.println("Total Deposit: " + totalDeposit);
        System.out.println("Total Withdraw: " + totalWithdraw);
        System.out.println("Total Loan Distributed: " + totalLoanDistributed);
        System.out.println("Total EMI Collected: " + totalEmiCollected);
        System.out.println("Total Active Loans: " + totalActiveLoans);
        System.out.println("Net Bank Balance: " + totalAmount);
        System.out.println("Total trasactions: " + tempCount);
        System.out.println("--------------------------");
    }
	
}// Report class end here
