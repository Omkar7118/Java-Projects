package Model;

import java.io.Serializable;

public class Dealer implements Serializable{
	
//	Attributes
	
	String name;
	String address;
	String phoneNo;
	private static final long serialVersionUID = 1L;
	
//	Constructors
	
	public Dealer() {

	}
	
	public Dealer(String name, String address, String phoneNo) {
		this.name = name;
		this.address = address;
		this.phoneNo = phoneNo;
	}

//	Setters and Getters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	

	
	
}//Class Product ends here
