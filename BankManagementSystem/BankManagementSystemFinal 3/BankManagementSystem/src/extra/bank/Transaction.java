package extra.bank;
import java.time.LocalDate;
import java.util.ArrayList;

import b2.BankAccount;

public class Transaction extends BankAccount {
    int transactionId=100;
	int accountNumber;
    String accountType;
    double amount;
    LocalDate dateTime;
    TransactionType transactionType;
    double currentBalance;
    
    
    static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
    static {
        transactions.add(new Transaction(101, "Saving", 2000, LocalDate.now().minusDays(10), TransactionType.Deposit, 7000));
        transactions.add(new Transaction(101, "Saving", 1000, LocalDate.now().minusDays(8), TransactionType.Withdraw, 6000));
        transactions.add(new Transaction(102, "Current", 5000, LocalDate.now().minusDays(7), TransactionType.Deposit, 15000));
        transactions.add(new Transaction(102, "Current", 2000, LocalDate.now().minusDays(6), TransactionType.Withdraw, 13000));
        transactions.add(new Transaction(103, "Salary", 15000, LocalDate.now().minusDays(5), TransactionType.Deposit, 30000));
        transactions.add(new Transaction(103, "Salary", 5000, LocalDate.now().minusDays(4), TransactionType.Withdraw, 25000));
        transactions.add(new Transaction(104, "Loan", 300000, LocalDate.now().minusDays(3), TransactionType.Loan, 302000));
        transactions.add(new Transaction(104, "Loan", 15000, LocalDate.now().minusDays(2), TransactionType.Loan, 287000));
        transactions.add(new Transaction(105, "Loan", 700000, LocalDate.now().minusDays(1), TransactionType.Loan, 705000));
        transactions.add(new Transaction(105, "Loan", 20000, LocalDate.now(), TransactionType.Loan, 685000));
    }

	// Default Constructor
	
	public Transaction() {
		
	}

	// Parameter Constructor
	
    public Transaction(int accountNumber, String accountType, double amount, LocalDate dateTime,
            TransactionType transactionType, double currentBalance) {
    		this.accountNumber = accountNumber;
    		this.accountType = accountType;
    		this.amount = amount;
    		this.dateTime = dateTime;
    		this.transactionType = transactionType;
    		this.currentBalance = currentBalance;
    }
	
	// Setters and Getters
	
	public int getTransactionId() {
		return transactionId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDate dateTime) {
		this.dateTime = dateTime;
	}

	
	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionDetails() {
	    String details = "===== All Transactions Details =====\n";

	    for (Transaction t : transactions ) {
	        details += "\nTransaction ID   : " + increaseTransactionId() +
	                   "\nAccount Number   : " + t.getAccountNumber()+
	                   "\nAccount Type     : " + t.getAccountType() +
	                   "\nAmount           : " + t.getAmount() +
	                   "\nDate & Time      : " + LocalDate.now() +
	                   "\nTransaction Type : " + t.transactionType +
	                   "\n----------------------------------\n";
	    }

	    return details;
	}

	public void displayTransaction() {
        System.out.println("==================================");
        System.out.println("        Transaction Summary       ");
        System.out.println("==================================");
        System.out.println(getTransactionDetails());
        System.out.println("==================================\n");
     
    }
	
	public void generateTrancationForDeposit(BankAccount b,double amount){
		transactions.add(new Transaction(b.getAccountNumber(),b.getAccountStatus(),amount,LocalDate.now(),TransactionType.Deposit,b.getCurrentBalance()));
//		displayTransaction();

	}
	
	public void generateTrancationForWithdraw(BankAccount b,double amount){
		transactions.add(new Transaction(b.getAccountNumber(),b.getAccountStatus(),amount,LocalDate.now(),TransactionType.Withdraw,b.getCurrentBalance()));
//		displayTransaction();
	}
	
	public int increaseTransactionId() {
		return transactionId++;
	}

	public static ArrayList<Transaction> getAllTransaction(){
		return transactions;
	}
	
	
}// Transaction class end here
