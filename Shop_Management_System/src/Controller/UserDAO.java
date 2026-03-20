package Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Model.User;
import Model.UserType;

public class UserDAO {

//	Attributes
	
	private List<User>users=new ArrayList<User>();
	Scanner sc=new Scanner(System.in);
	
//	Constructors
	
	public UserDAO() {
		loadFromFile();
		if(users.isEmpty()) {	
            users.add(new User(101, "owner123", "Rohit Sharma","9876543212", "rohit@gmail.com", UserType.OWNER));
            users.add(new User(102, "emp001", "Priya Verma","95123456785", "priya@gmail.com", UserType.EMPLOYEE));
            users.add(new User(103, "emp002", "Amit Singh", "9988776655", "amit@gmail.com", UserType.EMPLOYEE));
            users.add(new User(104, "owner999", "Admin User","9500112233", "admin@gmail.com", UserType.OWNER));
            saveToFile();
		}
	}
	
	public UserDAO(List<User> users) {
		this.users = users;
	}
	
//	Setters and Getters
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
//	Methods
	
	public void saveToFile(){
		try(ObjectOutputStream oos=new ObjectOutputStream(
				new FileOutputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Users.txt"))){
			 oos.writeObject(users);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadFromFile(){
		try(ObjectInputStream ois=new ObjectInputStream(
				new FileInputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Users.txt"))){
			 users=(List<User>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void removeUser(int userId) {
		Iterator itr=users.iterator();
		while(itr.hasNext()) {
			User user=(User) itr.next();
			if(user.getUserId()==userId) {
				itr.remove();
			}
		}
	}
	
	public User verifyUser(int id,String password) {
		for(User user:users) {
			if(user.getUserId()==id&&user.getPassword().equalsIgnoreCase(password)) {
				return user;
			}
		}
		return null;
	}
	
	public User verifyUserId(int userId) {
		boolean found=false;
		for(User user:users) {
			if(user.getUserId()==userId) {
				found=false;
				return user;
			}
		}
		if(!found) {
			System.out.println("User id is not found");
		}
		return null;
	}

	public boolean checkUserId(int userId) {
		boolean found=false;
		for(User user:users) {
			if(user.getUserId()==userId) {
				found=true;
			}
		}
		if(found) {
			System.out.println("User id exists. Please enter any other user id");
		}
		return found;
	}

	public void updatePassword(User user) {
		System.out.println("Enter new password : ");
		String password=sc.nextLine();
		user.setPassword(password);
	}

	public void updateName(User user) {
		System.out.println("Enter new name : ");
		String name=sc.nextLine();
		user.setName(name);
	}

	public void updatePhoneNo(User user) {
		System.out.println("Enter new Phone no. : ");
		String phoneNo=sc.nextLine();
		user.setPhoneNo(phoneNo);
	}

	public void updateEmail(User user) {
		System.out.println("Enter new Email id : ");
		String emailId=sc.nextLine();
		user.setEmailId(emailId);
	}
	
	public void display() {
	    if (users.isEmpty()) {
	        System.out.println("No users found.");
	        return;
	    }

	    System.out.println("\n--------------------- USER LIST ---------------------");
	    for (User user : users) {
	        System.out.println("----------------------------------------------");
	        System.out.println("User ID     : " + user.getUserId());
	        System.out.println("Password    : " + user.getPassword());
	        System.out.println("Name        : " + user.getName());
	        System.out.println("Phone No    : " + user.getPhoneNo());
	        System.out.println("Email Id    : " + user.getEmailId());
	        System.out.println("User Type   : " + user.getType());
	        System.out.println("----------------------------------------------");
	    }
	}

}
