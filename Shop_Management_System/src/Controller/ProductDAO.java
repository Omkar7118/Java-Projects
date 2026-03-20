package Controller;

import java.io.*;
import java.util.*;

import Model.Product;
import Model.SizeInventory;
import Model.Category;

public class ProductDAO {

    List<Product> products = new ArrayList<>();
    CategoryService categoryService=new CategoryService();
    Scanner sc=new Scanner(System.in);

    public ProductDAO() {
        if (products.isEmpty()) {
        	loadFromFile();
           
        }
        else {
        	 loadFromFile();
        	 // Adidas
             Map<String, SizeInventory> adidasInventory = new HashMap<>();
             adidasInventory.put("7", new SizeInventory(10, 5000));
             adidasInventory.put("8", new SizeInventory(15, 5200));
             adidasInventory.put("9", new SizeInventory(20, 5300));
             adidasInventory.put("10", new SizeInventory(5, 5400));

             // Nike
             Map<String, SizeInventory> nikeInventory = new HashMap<>();
             nikeInventory.put("S", new SizeInventory(20, 3500));
             nikeInventory.put("M", new SizeInventory(25, 3600));
             nikeInventory.put("L", new SizeInventory(20, 3700));
             nikeInventory.put("XL", new SizeInventory(15, 3800));

             // RayBan
             Map<String, SizeInventory> raybanInventory = new HashMap<>();
             raybanInventory.put("52mm", new SizeInventory(10, 5000));
             raybanInventory.put("54mm", new SizeInventory(20, 5500));

             // Samsung
             Map<String, SizeInventory> samsungInventory = new HashMap<>();
             samsungInventory.put("128GB", new SizeInventory(40, 80000));
             samsungInventory.put("256GB", new SizeInventory(35, 85000));
             samsungInventory.put("512GB", new SizeInventory(25, 90000));

             // Puma
             Map<String, SizeInventory> pumaInventory = new HashMap<>();
             pumaInventory.put("6", new SizeInventory(10, 4800));
             pumaInventory.put("7", new SizeInventory(15, 4900));
             pumaInventory.put("8", new SizeInventory(20, 5000));
             pumaInventory.put("9", new SizeInventory(10, 5100));
             pumaInventory.put("10", new SizeInventory(5, 5200));

             // Add all products
             products.add(new Product("Adidas Dealer", "Adidas", adidasInventory, "Ultraboost 23","Shoes"));
             products.add(new Product("Nike Sports Dealer", "Nike", nikeInventory, "Air Max Jacket","Clothing"));
             products.add(new Product("RayBan Outlet", "RayBan", raybanInventory, "RB2132 Classic", "Accessories"));
             products.add(new Product("Samsung Distributor", "Samsung", samsungInventory, "Galaxy S24","Electronics"));
             products.add(new Product("Puma Dealer", "Puma", pumaInventory, "Puma RS-X", "Shoes"));

             saveToFile();
            
        }
    }

    // Save to file
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Products.txt"))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load from file
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Products.txt"))) {
            products = (ArrayList<Product>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found is fine initially
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
//    Add Method
    
    public void addProduct(Product p) {
    	products.add(p);
    	saveToFile();
    	System.out.println("Product added successfully");
    }
    
//    Remove Method
    
    public void removeProducts(String model) {
    	boolean found=false;
    	Iterator<Product>itr=products.iterator();
    	while(itr.hasNext()) {
    		Product p=itr.next();
    		if(p.getModel().equalsIgnoreCase(model)) {
    			itr.remove();
    			found=true;
    		}
    	}
    	saveToFile();
    	if(found) {
    		System.out.println(model+" remove successfully");
    	}
    	else {
    		System.out.println("Model not found");
    	}
    	
    }
    
//    Get Method
    
    public List<Product> getAllProducts() {
        return products;
    }
    
//    Display Method

    public void displayAllProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
    
//    Search Methods

	public Product searchByDealerName(String name) {
		boolean found=false;
		for (Product product : products) {
			if(product.getDealerName().equalsIgnoreCase(name)) {
				System.out.println(product);
				found=true;
				return product;
			}	
        }
		if(!found) {
			System.out.println("Dealer not found");
		}
		return null;
	}

	public Product searchByBrand(String brand) {
		boolean found=false;
		for (Product product : products) {
			if(product.getBrand().equalsIgnoreCase(brand)) {
				System.out.println(product);
				found=true;
				return product;
			}	
        }
		if(!found) {
			System.out.println("Brand not found");
		}
		return null;
	}

	public Product searchByType(String type) {
		boolean found=false;
		for (Product product : products) {
			if(product.getBrand().equalsIgnoreCase(type)) {
				System.out.println(product);
				found=true;
				return product;
			}	
        }
		if(!found) {
			System.out.println("Type not found");
		}
		return null;
	}

	public Product searchByModel(String model) {
		boolean found=false;
		for (Product product : products) {
			if(product.getModel().equalsIgnoreCase(model)) {
				System.out.println(product);
				found=true;
				return product;
			}	
        }
		if(!found) {
			System.out.println("Model not found");
		}
		return null;
	}
	
//	Update Methods
	
	public void updateDealerName(Product product) {
		System.out.print("Enter Dealer name : ");
		String dealerName=sc.nextLine();
		product.setDealerName(dealerName);
		saveToFile();	
	}

	public void updateBrand(Product product) {
		System.out.print("Enter Brand  : ");
		String brand=sc.nextLine();
		product.setBrand(brand);
		saveToFile();
	}

	public void updateType(Product product) {
		System.out.print("Enter Type : ");
		String type=sc.nextLine();
		product.setType(type);
		saveToFile();
	}

	public void updateModel(Product product) {
		System.out.print("Enter Model : ");
		String model=sc.nextLine();
		product.setModel(model);
		saveToFile();
	}
	
	public void updateSize(Product product) {
		Map<String, SizeInventory> inventory = new HashMap<String, SizeInventory>();
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
		product.setInventoryMap(inventory);
		saveToFile();
	}
	
}
    
