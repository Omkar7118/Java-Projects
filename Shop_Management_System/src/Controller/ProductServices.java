package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Model.Category;
import Model.Dealer;
import Model.Product;
import Model.SizeInventory;

public class ProductServices {
	
	DealerService dealerService=new DealerService();
	CategoryService categoryService=new CategoryService();
	Scanner sc=new Scanner(System.in);
	ProductDAO productDAO=new ProductDAO();
	
//	Methods
	
	public void addProduct() {	
		Map<String,SizeInventory> inventory=new HashMap<>();
		
		System.out.println("Enter following data of product");
		System.out.print("Dealer name : ");
		String dealerName=sc.nextLine();
		dealerService.verifyDealer(dealerName);
		
		System.out.print("Brand : ");
		String brand=sc.nextLine();
		
		System.out.print("Model : ");
		String model=sc.nextLine();
		
		System.out.print("Color : ");
		String color=sc.nextLine();
		
		System.out.print("Type : ");
		String type=sc.nextLine(); 
		categoryService.verifyCategory(type);
		
		System.out.print("Total sizes : ");
		int totalSizes=sc.nextInt();
		sc.nextLine();
		
		System.out.println("Inventory details");
		for(int i=0;i<totalSizes;i++) {
		
		System.out.print("Size : ");
		String size=sc.nextLine();
		
		System.out.print("Cost : ");
		double cost=sc.nextDouble();
		
		System.out.print("Quantity : ");
		int quantity=sc.nextInt();
		sc.nextLine();
		inventory.put(size, new SizeInventory(quantity,cost));
		}
		Product product=new Product(dealerName,brand,inventory,model,type);
		productDAO.addProduct(product);
	}
	
	public void removeProduct() {
		System.out.print("Enter Product Model : ");
		String model=sc.nextLine();
		productDAO.removeProducts(model);
	}

	public void displayProduct() {
		productDAO.displayAllProducts();
	}

	public void searchProduct() {
		System.out.println("Search by");
		System.out.println("1.Dealer name");
		System.out.println("2.Brand");
		System.out.println("3.Type");
		System.out.println("4.Model");
		int choice=sc.nextInt();
		sc.nextLine();
		switch(choice) {
		case 1:
			System.out.print("Enter Dealer name : ");
			String name=sc.nextLine();
			productDAO.searchByDealerName(name);
			break;
			
		case 2:
			System.out.print("Enter Brand : ");
			String brand=sc.nextLine();
			productDAO.searchByBrand(brand);
			break;
			
		case 3:
			System.out.print("Enter type : ");
			String type=sc.nextLine();
			productDAO.searchByType(type);
			break;
			
		case 4:
			System.out.print("Enter Model : ");
			String model=sc.nextLine();
			productDAO.searchByModel(model);
			break;
		}
			
	}

	public void updateProduct() {
		System.out.print("Enter model to update product : ");
		String model=sc.nextLine();
		Product product=productDAO.searchByModel(model);
		if(product!=null) {
		System.out.println("Update Product");
		System.out.println("1.Dealer name");
		System.out.println("2.Brand");
		System.out.println("3.Type");
		System.out.println("4.Model");
		System.out.println("5.Size");
		int choice=sc.nextInt();
		sc.nextLine();
		
		switch(choice) {
		case 1:
			productDAO.updateDealerName(product);
			break;
			
		case 2:
			productDAO.updateBrand(product);
			break;
			
		case 3:
			productDAO.updateType(product);
			break;
			
		case 4:
			productDAO.updateModel(product);
			break;
			
		case 5:
			productDAO.updateSize(product);
			break;
			
		}
		System.out.println("Product updated successfully");
		}
	}
}
