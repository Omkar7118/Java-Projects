package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Model.Dealer;

public class DealerService {
	
	DealerDAO dealerDAO=new DealerDAO();
	Scanner sc=new Scanner(System.in);
	
	public void verifyDealer(String name) {
		boolean found=false;
		List<Dealer> dealers=dealerDAO.getAllDealers();
		for(Dealer dealer:dealers ) {
			if(dealer.getName().equalsIgnoreCase(name)) {
				found=true;
			}
		}
		if(!found) {
			addDealer(name);
		}
	}
	
	public void addDealer(String name) {
		System.out.println("Dealer doesn't exists");
		System.out.println("Adding Dealer");
		System.out.print("Enter address : ");
		String address=sc.nextLine();
		System.out.print("Enter phone no. ");
		String phoneNo=sc.nextLine();
		Dealer dealer=new Dealer(name,address,phoneNo);
		dealerDAO.addDealer(dealer);
		System.out.println("Dealer added successfully");
		
		
	}
}
