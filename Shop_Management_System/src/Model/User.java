package Model;

import java.io.Serializable;

public class User implements Serializable {

//	attributes
	
	int userId;
	String password;
	String name;
	String phoneNo;
	String emailId;
	UserType type;
	private static final long serialVersionUID = 1L;
	
//	Constructors
	
	public User() {
		
	}
	
	public User(int userId, String password, String name, String phoneNo, String emailId, UserType type) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.type = type;
	}



//	Setters and Getters
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
	
//	Methods
	

	@Override
	public String toString() {
	    return "User [userId=" + userId 
	            + ", password=" + password 
	            + ", name=" + name 
	            + ", phoneNo=" + phoneNo
	            + ", emailId=" + emailId 
	            + ", type=" + type + "]";
	}

	
	
	
	
}//Class User ends here
