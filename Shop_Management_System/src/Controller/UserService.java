package Controller;

import java.util.Scanner;

import Model.User;
import Model.UserType;
import View.EmployeeMenu;
import View.Login;
import View.OwnerMenu;

public class UserService {
	Scanner sc=new Scanner(System.in);
	UserDAO userDAO=new UserDAO();
	
//	Methods
	
	public boolean verifyUser(int id,String password) {
		User user=userDAO.verifyUser(id, password);
		if(user!=null) {
			if(user.getType()==UserType.OWNER) {
				OwnerMenu.ownerMenu();
			}
			if(user.getType()==UserType.EMPLOYEE) {
				EmployeeMenu.employeeMenu();
			}
			return true;
		}
		else {
			System.out.println("Invalid User id or password");
			return false;
		}
	}
	
	public void addUser() {
		
		boolean check=false;
		int userId=0;
		do{
			System.out.println("Employee Details");
			System.out.print("User Id : ");
			userId=sc.nextInt();
			sc.nextLine();
			check=userDAO.checkUserId(userId);
		}while(check);
		
		System.out.print("Password : ");
		String password=sc.nextLine();
		
		System.out.print("Name : ");
		String name=sc.nextLine();
		
		System.out.print("Phone no. ");
		String phoneNo=sc.nextLine();
		
		System.out.print("Email : ");
		String emailId=sc.nextLine();
		
		System.out.print("Type(Owner or Employee) : ");
		String type=sc.nextLine();
		UserType userType=UserType.valueOf(type.toUpperCase());
		User user=new User(userId,password,name,phoneNo,emailId,userType);
		userDAO.addUser(user);
		System.out.println("User is added successfully");
	}

	public void removeUser() {
		System.out.print("Enter user id : ");
		int userId=sc.nextInt();
		sc.nextLine();
		userDAO.removeUser(userId);
		System.out.println("User is removed successfully");
	}

	public void updateUser() {
		int choice=0;
		System.out.print("Enter user id : ");
		int userId=sc.nextInt();
		sc.nextLine();
		User user=userDAO.verifyUserId(userId);
		System.out.println("Update Menu");
		System.out.println("1.Password");
		System.out.println("2.Name");
		System.out.println("3.Phone No.");
		System.out.println("4.Email");
		choice=sc.nextInt();
		switch(choice) {
		case 1:
			userDAO.updatePassword(user);
			break;
			
		case 2:
			userDAO.updateName(user);
			break;
			
		case 3:
			userDAO.updatePhoneNo(user);
			break;
			
		case 4:
			userDAO.updateEmail(user);
			break;
			
		default: 
			System.out.println("Invaid Choice");
		}
	}

	public void displayAll() {
		userDAO.display();
	}
	

}//Class UserService ends here
