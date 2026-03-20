package Model;

import java.io.Serializable;

public class Category implements Serializable {
	
//	Attributes
	
	String name;
	private static final long serialVersionUID = 1L;
	
//	Constructors
	
	public Category() {
	}
	
	public Category(String name) {
		this.name = name;
	}

//	Setters and Getters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}//Class Category ends here
