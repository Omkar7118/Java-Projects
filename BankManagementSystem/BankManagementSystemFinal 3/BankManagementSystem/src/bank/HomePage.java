package bank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import b2.BankAccount;
import b2.BusinessLoan;
import b2.CurrentAccount;
import b2.LoanAccount;
import b2.PersonalLoan;
import b2.SalaryAccount;
import b2.SavingAccount;
import extra.bank.Reports;
import extra.bank.Transaction;

public class HomePage {
	
	Scanner sc=new Scanner(System.in);
	BankEmployeesDetails bankEmployeeDetails;
	Reports report=new Reports();
	Transaction transcation=new Transaction();
	BankAccount bankAccount;
	Bank bank=new Bank();
	ArrayList<BankAccount>ba=bankAccount.getAllBankAccounts();
	int choice=0;
	
	void MainMenu(Designation designation) {
		int choice=0;
		do {
		if(designation==Designation.BRANCH_MANAGER) {
			System.out.println("\nWelcome to the Branch Manager Portal.");
			System.out.println("Please select an option from the menu below:");
			System.out.println("1. Add a new employee to the branch");
			System.out.println("2. Remove an existing employee");
			System.out.println("3. View detailed transaction reports");
			System.out.println("4. View transaction summary");
			System.out.println("5. Display all employees");
			System.out.println("6. Display Bank details ");
			System.out.println("7. Display all transcation details");
			System.out.println("0. Exit to main menu");
			choice=sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
				bankEmployeeDetails.addEmployee();
				break;
			
			case 2:
				bankEmployeeDetails.removeEmployee();
				break;
				
			case 3:
				report.displayReports();
				break;
				
			case 4:
				transcation.displayTransaction();
				break;
				
			case 5:
				bankEmployeeDetails.displayAllEmployees();
				break;
				
			case 6:
				bank.DEFAULT_BANK.displayBankDetails();
				break;
				
			case 7:
				System.out.println(transcation.getTransactionDetails());
				System.out.println("inside 7");
				break;
				
			case 0:
				System.out.println("Thank you for using the Branch Manager Portal. Exiting to login...");
                break;
                
            default:
            	System.out.println("⚠️ Invalid selection. Please enter a valid option from the menu.");
				}

			}
		else if(designation==Designation.CUSTOMER_SERVICE_EXECUTIVE) {
			System.out.println("\nWelcome to the Customer Service Executive Portal.");
			System.out.println("Please choose an action:");
			System.out.println("1. Create a new customer account");
			System.out.println("2. Delete an existing customer account");
			System.out.println("3. Withdraw funds from a customer account");
			System.out.println("4. Deposit funds into a customer account");
			System.out.println("5. Display all accounts");
			System.out.println("6. Enter account menu");
			System.out.println("0. Exit to main menu");
			choice=sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
			    System.out.println("\nChoose Account Type to Open:");
			    System.out.println("1. Saving Account");
			    System.out.println("2. Current Account");
			    System.out.println("3. Salary Account");
			    System.out.println("4. Personal Loan Account");
			    System.out.println("5. Business Loan Account");
			    System.out.print("Enter choice: ");
			    int accChoice = sc.nextInt();
			    sc.nextLine(); // consume newline

			    System.out.print("Enter Account Number: ");
			    int accNo = sc.nextInt();
			    sc.nextLine();

			    System.out.print("Enter Account Holder Name: ");
			    String name = sc.nextLine();

			    System.out.print("Enter Initial Balance: ");
			    double balance = sc.nextDouble();
			    sc.nextLine();

			    switch (accChoice) {
			        case 1: // Saving
			            System.out.print("Enter Minimum Balance: ");
			            double minBal = sc.nextDouble();
			            System.out.print("Enter Interest Rate: ");
			            double rate = sc.nextDouble();
			            SavingAccount sa = new SavingAccount(accNo, name, "Saving", balance, LocalDate.now(), "Active", minBal, rate);
			            BankAccount.addSavingAccount(sa);
			            break;

			        case 2: // Current
			            System.out.print("Enter Overdraft Limit: ");
			            double overdraft = sc.nextDouble();
			            CurrentAccount ca = new CurrentAccount(accNo, name, "Current", balance, LocalDate.now(), "Active", overdraft, 0);
			            BankAccount.addCurrentAccount(ca);
			            break;

			        case 3: // Salary
			            sc.nextLine();
			            System.out.print("Enter Employee Name: ");
			            String empName = sc.nextLine();
			            System.out.print("Enter Company Name: ");
			            String company = sc.nextLine();
			            SalaryAccount salAcc = new SalaryAccount(accNo, name, "Salary", balance, LocalDate.now(), "Active", empName, company, LocalDate.now());
			            BankAccount.addSalaryAccount(salAcc);
			            break;

			        case 4: // Personal Loan
			            sc.nextLine();
			            System.out.print("Enter Occupation: ");
			            String occupation = sc.nextLine();
			            System.out.print("Enter Loan Amount: ");
			            double loanAmt = sc.nextDouble();
			            System.out.print("Enter Approved Amount: ");
			            double approved = sc.nextDouble();
			            System.out.print("Enter Disbursed Amount: ");
			            double disbursed = sc.nextDouble();
			            System.out.print("Enter Interest Rate: ");
			            double loanRate = sc.nextDouble();
			            System.out.print("Enter Tenure (months): ");
			            int tenure = sc.nextInt();
			            sc.nextLine();
			            System.out.print("Enter Purpose: ");
			            String purpose = sc.nextLine();
			            PersonalLoan pl = new PersonalLoan(accNo, name, "Loan", balance, LocalDate.now(), "Active",
			                                               occupation, loanAmt, approved, disbursed, loanRate, tenure, 0, tenure, purpose, true);
			            BankAccount.addPersonalLoanAccount(pl);
			            break;

			        case 5: // Business Loan
			            sc.nextLine();
			            System.out.print("Enter Business Type: ");
			            String businessType = sc.nextLine();
			            System.out.print("Enter Loan Amount: ");
			            double bLoanAmt = sc.nextDouble();
			            System.out.print("Enter Approved Amount: ");
			            double bApproved = sc.nextDouble();
			            System.out.print("Enter Disbursed Amount: ");
			            double bDisbursed = sc.nextDouble();
			            System.out.print("Enter Interest Rate: ");
			            double bRate = sc.nextDouble();
			            System.out.print("Enter Tenure (months): ");
			            int bTenure = sc.nextInt();
			            System.out.print("Enter Partners Count: ");
			            int partners = sc.nextInt();
			            BusinessLoan bl = new BusinessLoan(accNo, name, "Loan", balance, LocalDate.now(), "Active",
			                                               businessType, bLoanAmt, bApproved, bDisbursed, bRate, bTenure, 0, bTenure, "Business", partners);
			            BankAccount.addBusinessLoanAccount(bl);
			            break;

			        default:
			            System.out.println("❌ Invalid choice. Account not created.");
			    }
			    break;
				
			
			case 2:
				bankEmployeeDetails.removeEmployee();
				break;
				
			case 3:
				int AccountNo=bankAccount.verifyAccount();
				if(AccountNo==0) {
					System.out.println("❌ Account verification failed. Unable to proceed with withdrawal.");
					break;
				}
				else {
				bankAccount.withdraw(AccountNo);
				break;
				}
				
			case 4:
				int AccountNo1=bankAccount.verifyAccount();
				if(AccountNo1==0) {
					System.out.println("❌ Account verification failed. Unable to proceed with deposit.");
					break;
				}
				else {
				bankAccount.deposit(AccountNo1);
				break;
				}
			case 5:
				bankAccount.displayAccountDetails();				
				break;
				
			case 6:
				System.out.println("Bank Menu");
				System.out.println("1.Saving Account");
				System.out.println("2.Current Account");
				System.out.println("3.Loan Account");
				System.out.println("4.Salary Account");
				int ch=sc.nextInt();
				switch(ch) {
				case 1:
					System.out.println("Saving Account Menu");
					System.out.println("1.Check Penalty");
					System.out.println("2.Apply Interest");
					int temp1=sc.nextInt();
					switch(temp1) {
					case 1:
						for(BankAccount b:ba) {
							if(b instanceof SavingAccount) {							
								SavingAccount sa=(SavingAccount)b;
								int tempNo=b.getAccountNumber();
								sa.checkPenalty(tempNo);	
							}
						}
						break;
					case 2:
						for(BankAccount b:ba) {
							if(b instanceof SavingAccount) {							
								SavingAccount sa=(SavingAccount)b;
								sa.applyInterest();	
							}
						}
						break;
					}
				case 2:
					System.out.println("Current Account Menu");
					System.out.println("1.Check Overdraft Limit");
					int temp2=sc.nextInt();
					switch(temp2) {
						case 1:
							for(BankAccount b:ba) {
								if(b instanceof CurrentAccount) {							
									CurrentAccount ca=(CurrentAccount)b;
									ca.checkOverDraft();
								}
							}
							break;
					}
				case 3:
					System.out.println("Loan Account Menu");
					System.out.println("1.Personal Loan Account");
					System.out.println("2.Business Loan Account");
					int temp3=sc.nextInt();
					switch(temp3) {
					case 1:
						for(BankAccount b:ba) {
							if(b instanceof LoanAccount) {
								LoanAccount la=(LoanAccount)b;
								if(b instanceof PersonalLoan)
								{
									PersonalLoan pl=(PersonalLoan)la;
									System.out.println("Personal Loan Account Menu");
									System.out.println("1.Check Eligibility");
									System.out.println("2.Emi Details");
									System.out.println("3.Calculate Emi");
									System.out.println("4.Pay Emi");
									int tempChoice=sc.nextInt();
									switch(tempChoice) {
									case 1:
										pl.checkEligibility();
										break;
									
									case 2:
										pl.emiDetails();
										break;
										
									case 3:
										pl.calEmi();
										break;
										
									case 4:
										pl.payEmi();
										break;
									}
								}
							}
						}
						
					case 2:
						for(BankAccount b:ba) {
							if(b instanceof LoanAccount) {
								LoanAccount la=(LoanAccount)b;
								if(b instanceof BusinessLoan)
								{
									BusinessLoan bl=(BusinessLoan)la;
									System.out.println("Business Loan Account Menu");
									System.out.println("1.Check Eligibility");
									System.out.println("2.Emi Details");
									System.out.println("3.Calculate Emi");
									System.out.println("4.Pay Emi");
									int tempChoice=sc.nextInt();
									switch(tempChoice) {
									case 1:
										bl.checkEligibility();
										break;
									
									case 2:
										bl.emiDetails();
										break;
										
									case 3:
										bl.calEmi();
										break;
										
									case 4:
										bl.payEmi();
										break;
									}
								}
							}
						}
					}
				case 4:
					System.out.println("Salary Account Menu");
					System.out.println("1.Credit Salary");
					int temp4=sc.nextInt();
					switch(temp4) {
						case 1:
						for(BankAccount b:ba) {
							if(b instanceof SalaryAccount) {
								SalaryAccount sa=(SalaryAccount)b;
								BankAccount tempAccNo=sa;
								sa.creditSalary(tempAccNo);
						}
					}
					}
					}
				
				
			case 0:
				System.out.println("Thank you for assisting our customers. Returning to main menu...");
                break;
                
            default:
            	System.out.println("⚠️ Invalid selection. Please enter a valid option from the menu.");
				}

			}
			
		}while(choice!=0);
		
	}
}
