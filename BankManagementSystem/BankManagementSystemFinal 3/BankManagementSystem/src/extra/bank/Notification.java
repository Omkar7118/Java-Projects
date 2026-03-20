package extra.bank;

import java.util.ArrayList;

import b2.BankAccount;

public class Notification {
	int notifictaionId;
	String messageContent;
	
	
//	BankAccount b = new BankAccount();
	
	// Default Constructor 
	
	public Notification() {
		this.messageContent = null;
	}
	
	// Parameter Constructor
	
	public Notification(String messageContent) {
		this.messageContent = messageContent;
	}
	
	// Setters and Getters
	
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	// Notification 
	
	public String sendNotification(int accountNo) {
		
		ArrayList<Transaction> transactions = Transaction.getAllTransaction();
		
		
		for (Transaction transaction : transactions) {
		if(accountNo==transaction.getAccountNumber()) {
			switch(transaction.getTransactionType()) {
			
			case TransactionType.Deposit:
				 messageContent = "Dear Customer, your account " + transaction.getAccountNumber() +
                        		 "\nhas been credited with ₹" + transaction.getAmount() +
                        		 "\non " + transaction.getDateTime() +
                        		 ". \nAvailable balance: ₹" + transaction.getCurrentBalance() + ".";
				break;
			
			
			case TransactionType.Withdraw: 
				messageContent = "Dear Customer, your account " + transaction.getAccountNumber() +
               		 			 "\nhas been debited with ₹" + transaction.getAmount() +
               		 			 "\non " + transaction.getDateTime() +
               		 			 ". \nAvailable balance: ₹" + transaction.getCurrentBalance() + ".";
				break;
			
//			case "loan":
//				messageContent = "Dear Customer, your loan of ₹" + transaction.getAmount() +
//                				 "\nfor account " + transaction.getAccountNumber() +
//                				 "\nhas been approved on " + transaction.getDateTime() + ".";
//				break;
//			
//			case "emi":
//				messageContent = "Dear Customer, your EMI payment of ₹" + transaction.getAmount() +
//                				 "\nfor account " + transaction.getAccountNumber() +
//                				 "\nhas been received on " + transaction.getDateTime() +
//                				 ". \nAvailable balance: ₹" + transaction.getAmount() + ".";
				}
			}
		}
		return messageContent;
	}
	
	public void displayNotification() {
		System.out.println(messageContent);
	}

}// Notification class end here
