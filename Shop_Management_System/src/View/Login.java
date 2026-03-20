package View;

import java.util.Scanner;

import Controller.UserService;

public class Login {
	UserService user=new UserService();
	Scanner sc=new Scanner(System.in);
	
	public void login() {
		System.out.println("\nWelcome to login page!!");
		System.out.print("Enter Login Id : ");
		int id=sc.nextInt();
		sc.nextLine();
		System.out.print("Enter Password : ");
		String password=sc.nextLine();
		System.out.println();
		boolean found=user.verifyUser(id,password);
		if(!found) {
			login();
		}
	}
}
