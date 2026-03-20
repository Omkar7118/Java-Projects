package View;

import java.util.Scanner;

import Controller.ProductServices;
import Controller.UserService;

public class OwnerMenu {
	
	public static void ownerMenu() {
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
		System.out.println("2.Employee Management");
		System.out.println("3.Billing");
		System.out.println("0.Exit");
	 	choice=sc.nextInt();
	 	switch(choice) {
	 	case 1:
	 		productMenu.productMenu();
	 		break;
	 		
	 	case 2:
	 		employeeMenu.employeeMenu();
	 		break;
	 		
	 	case 3:
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
	
}//Class OwnerMenu ends here
