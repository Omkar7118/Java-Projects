package b2;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import extra.bank.Notification;
import extra.bank.Transaction;
import extra.bank.TransactionType;

public class BankAccount 
{	
		
	    int accountNumber; 
	    String accountHolderName; 
	    String accountType; 
	    double currentBalance; 
	    LocalDate accountOpeningDate;
	    String accountStatus; 
	    Scanner sc=new Scanner(System.in);
	    Notification n1=new Notification();
	    
	    //constructors
	    public BankAccount() 
		{
			this.accountNumber = 0;
			this.accountHolderName = "Not Given";
			this.accountType = "Not Given";
			this.currentBalance = 0.0;
			this.accountOpeningDate =  LocalDate.now();
			this.accountStatus = "Not Given";
		}
	    public BankAccount(int accountNumber, String accountHolderName, String accountType, double currentBalance,
				LocalDate accountOpeningDate, String accountStatus) 
		{
			this.accountNumber = accountNumber;
			this.accountHolderName = accountHolderName;
			this.accountType = accountType;
			this.currentBalance = currentBalance;
			this.accountOpeningDate = accountOpeningDate;
			this.accountStatus = accountStatus;
		}
		//setters and getters
		public int getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(int accountNumber) {
			this.accountNumber = accountNumber;
		}
		public String getAccountHolderName() {
			return accountHolderName;
		}
		public void setAccountHolderName(String accountHolderName) {
			this.accountHolderName = accountHolderName;
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public double getCurrentBalance() {
			return currentBalance;
		}
		public void setCurrentBalance(double currentBalance) {
			this.currentBalance = currentBalance;
		}
		public LocalDate getAccountOpeningDate() {
			return accountOpeningDate;
		}
		public void setAccountOpeningDate(LocalDate accountOpeningDate) {
			this.accountOpeningDate = accountOpeningDate;
		}
		public String getAccountStatus() {
			return accountStatus;
		}
		public void setAccountStatus(String accountStatus) {
			this.accountStatus = accountStatus;
		}
		//display
		public void displayAccountDetails()
		{
		        System.out.println("***************** Bank Account Details ***********************");
		        System.out.println("Account Number        : " + this.accountNumber);
		        System.out.println("Account Holder Name   : " + this.accountHolderName);
		        System.out.println("Account Type          : " + this.accountType);
		        System.out.println("Current Balance       : " + this.currentBalance);
		        System.out.println("Account Opening Date  : " + this.accountOpeningDate);
		        System.out.println("Account Status        : " + this.accountStatus);
		 
		}
		//methods
		public void updateStatus(String str)
		{
			 	setAccountStatus(str);
			 	System.out.println("***************Status Updated**************");
		}
		public void checkBalance()
		{
			System.out.println("Current Balance : " + this.currentBalance);
		}
		
		public void deposit(int tempAccountNumber)
		{ 
			System.out.println("Enter amount : ");
			double amount=sc.nextDouble();
			for(BankAccount bankAccount: accounts) {
				if(tempAccountNumber==bankAccount.getAccountNumber())
				{
					bankAccount.currentBalance+= amount;
					Transaction t1=new Transaction();
					t1.generateTrancationForDeposit(bankAccount,amount);
					System.out.println("\n"+n1.sendNotification(tempAccountNumber));

					break;
				}
				
		        
			}
		}
		
		public void withdraw(int tempAccountNumber)
		{
			System.out.println("Enter amount : ");
			double amount1=sc.nextDouble();
			for(BankAccount bankAccount: accounts) {
				if(tempAccountNumber==bankAccount.getAccountNumber())
				{
					if(bankAccount.currentBalance>=amount1){
						bankAccount.currentBalance-= amount1;
						System.out.println("Amount withdrawn : "+amount1);
						Transaction t2=new Transaction();
						t2.generateTrancationForWithdraw(bankAccount, amount1);
						System.out.println("\n"+n1.sendNotification(tempAccountNumber));
						break;
					}
					else {
						System.out.println("Insufficient balance");
						break;
					}
				}
			}
			
		 }
		
		public int verifyAccount(){
			System.out.println("Enter account number : ");
			int tempAccountNumber=sc.nextInt();
			for(BankAccount bankAccount: accounts) {
				if(tempAccountNumber==bankAccount.getAccountNumber())
				{
					System.out.println("Account Found");
					return tempAccountNumber;
				
				}
			}
			System.out.println("Account number is not Found!");
			return 0;
		}
		//hardcoded
		public static ArrayList<BankAccount> accounts = new ArrayList<>();
		static {
		    accounts.add(new SavingAccount(101, "Alice", "Saving", 500, LocalDate.now(), "Active", 1000, 3.5));
		    accounts.add(new CurrentAccount(102, "Bob", "Current", 10000, LocalDate.now(), "Active", 5000, 0));
		    accounts.add(new SalaryAccount(103, "Charlie", "Salary", 15000, LocalDate.now(), "Active", "Charlie", "TechCorp", LocalDate.now()));
		    accounts.add(new PersonalLoan(104, "David", "Loan", 20000, LocalDate.now(), "Active", "Engineer", 800000, 300000, 300000, 12.5, 24, 0, 24, "Home Renovation", true));
		    accounts.add(new BusinessLoan(105, "Eva", "Loan", 5000, LocalDate.now(), "Active", "Trader", 1000000, 700000, 700000, 11.0, 36, 0, 36, "Retail", 5));
		   
		}
		//add and remove methods
		public static void addSavingAccount(SavingAccount account) {
		    accounts.add(account);
		    System.out.println("Saving Account added for: " + account.getAccountHolderName());
		}

		public static void addCurrentAccount(CurrentAccount account) {
		    accounts.add(account);
		    System.out.println("Current Account added for: " + account.getAccountHolderName());
		}

		public static void addSalaryAccount(SalaryAccount account) {
		    accounts.add(account);
		    System.out.println("Salary Account added for: " + account.getAccountHolderName());
		}

		public static void addPersonalLoanAccount(PersonalLoan account) {
		    accounts.add(account);
		    System.out.println("Personal Loan Account added for: " + account.getAccountHolderName());
		}

		public static void addBusinessLoanAccount(BusinessLoan account) {
		    accounts.add(account);
		    System.out.println("Business Loan Account added for: " + account.getAccountHolderName());
		}
		public static void removeAccount(int accNumber)
		{
		    Iterator<BankAccount> it = accounts.iterator();
		    while (it.hasNext()) 
		    {
		        BankAccount acc = it.next();
		        if (acc.getAccountNumber() == accNumber) 
		        {
		            it.remove();
		            System.out.println("Account removed: " + accNumber);
		            return;
		        }
		    }
		    System.out.println("Account not found: " + accNumber);
		}
		
		public static ArrayList<BankAccount> getAllBankAccounts(){
			return accounts;
		}
		
}//class BankAccount ends here 


