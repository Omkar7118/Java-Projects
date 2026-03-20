package View;

import java.util.Scanner;

import Controller.ProductDAO;
import Controller.ReportServices;
import Model.Product;

public class BillingMenu {

	Scanner sc=new Scanner(System.in);
	ProductDAO productDAO=new ProductDAO();
	ReportServices reportService=new ReportServices();
	int choice=0;
	
	public void billingMenu() {
		do {
		System.out.println("\nBilling Menu");
		System.out.println("1.Add report");
		System.out.println("2.Remove report");
		choice=sc.nextInt();
		switch(choice) {
		
		case 1:
			reportService.addReport();
			break;
			
		case 2:
			reportService.removeProduct();
			break;
			
		default:
			System.out.println("Invalid choice");
			break;
		}
		}while(choice!=0);
	}
}
