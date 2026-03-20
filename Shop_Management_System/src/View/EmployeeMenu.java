package View;

import java.util.Scanner;

import Controller.ProductServices;
import Controller.UserService;

public class EmployeeMenu {
	
	public static void employeeMenu() {
		UserService user=new UserService();
		ProductServices product=new ProductServices();
		InventoryMenu productMenu=new InventoryMenu();
		EmployeeManagementMenu employeeMenu=new EmployeeManagementMenu();
		BillingMenu billingMenu=new BillingMenu();
		Login login=new Login();
		Scanner sc=new Scanner(System.in);
		int choice=0;
		do{
		System.out.println("Owner's Menu");
	 	System.out.println("1.Inventory Management");
		System.out.println("2.Billing");
		System.out.println("0.Exit");
	 	choice=sc.nextInt();
	 	switch(choice) {
	 	case 1:
	 		productMenu.productMenu();
	 		break;
	 		
	 	case 2:
	 		billingMenu.billingMenu();
	 		break;
	 		
	 	case 0:
	 		login.login();
	 		break;
	 		
	 	default :
	 		System.out.println("Invalid Choice");
	 		break;
	 	}
		}while(choice!=0);
	 }
	
}//Class EmployeeMenu ends here
