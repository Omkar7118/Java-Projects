package Model;

import java.io.Serializable;

public class SizeInventory implements Serializable{

//	Attributes
	
	int quantity;
	double price;
	private static final long serialVersionUID = 1L;
	
//	Setters and Getters
	
	public SizeInventory(int quantity, double price) {
		this.quantity = quantity;
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}	

//	Methods

	public String toString() {
	    return "Qty: " + quantity + ", Price: " + price;
	}
	
	public void reduceQuantity(int quantity) {
		this.quantity=this.quantity-quantity;
	}

	
}//Class SizeInventory ends here
