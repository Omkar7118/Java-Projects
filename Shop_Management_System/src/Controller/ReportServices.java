package Controller;

import java.util.Scanner;

import Model.Product;

public class ReportServices {
	
	Scanner sc=new Scanner(System.in);
	ReportDAO reportDAO=new ReportDAO();
	ProductDAO productDAO=new ProductDAO();
	
	public void addReport() {
		System.out.println("Enter Model : ");
		String model=sc.nextLine();
		Product product=productDAO.searchByModel(model);
		if(product!=null) {
		System.out.println("Billing Details");
		System.out.print("Size : ");
		String size=sc.nextLine();
		System.out.print("Quantity : ");
		int quantity=sc.nextInt();
		System.out.print("Price : ");
		double price=sc.nextDouble();
		sc.nextLine();
		reportDAO.addReport(product,size, quantity, price);
		}

	}
	
	public void removeProduct() {
		System.out.println("Enter ReportId : ");
		int reportId=sc.nextInt();
		reportDAO.removeReport(reportId);
	}
}
