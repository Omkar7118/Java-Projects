package View;

import java.util.Scanner;

import Controller.UserService;

public class EmployeeManagementMenu {

	public void employeeMenu() {
		Scanner sc=new Scanner(System.in);
		UserService userService=new UserService();
		int choice=0;
		do {
		System.out.println("Employee Menu");
		System.out.println("1.Add Employee");
		System.out.println("2.Remove Employee");
		System.out.println("3.Update Employee");
		System.out.println("4.Display All Employes");
		choice=sc.nextInt();
		switch(choice) {
			case 1:
				userService.addUser();
				break;
				
			case 2:
				userService.removeUser();
				break;
				
			case 3:
				userService.updateUser();
				break;
				
			case 4:
				userService.displayAll();
				break;
				
			default:
				System.out.println("Invalid choice");
		}
		}while(choice!=0);
	}
}
