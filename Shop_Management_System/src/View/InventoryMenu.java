package View;

import java.util.Scanner;

import Controller.ProductServices;

public class InventoryMenu {
	Scanner sc=new Scanner(System.in);
	ProductServices product=new ProductServices();

	public void productMenu() {
		int choice=0;
		do {
		System.out.println("\nOwner's Menu");
	 	System.out.println("1.Add Product");
		System.out.println("2.Remove Product");
		System.out.println("3.Display all products");
		System.out.println("4.Search Product");
		System.out.println("5.Update Product");
		System.out.println("0.Exit");
		choice=sc.nextInt();
	 	switch(choice) {
	 	case 1:
	 		product.addProduct();
	 		break;
	 		
	 	case 2:
	 		product.removeProduct();
	 		break;
	 		
	 	case 3:
	 		product.displayProduct();
	 		break;
	 		
	 	case 4:
	 		product.searchProduct();
	 		break;
	 		
	 	case 5:
	 		product.updateProduct();
	 		break;
	 		
	 	default :
	 		System.out.println("Invalid Choice");
	 		break;
	 		
	 	}
		}while(choice!=0);
	}
}
