package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {

    // Attributes
	
    String dealerName;
    String brand;
    Map<String, SizeInventory> inventoryMap = new HashMap<>();
    int quantity;
    String model;
    String color;
    String type;
    double totalCost;
    LocalDateTime dateAdded;
    private static final long serialVersionUID = 1L;

    // Constructors
    
    public Product() {
    	
    }

    public Product(String dealerName, String brand, Map<String, SizeInventory> inventoryMap, String model, String type) {
        this.dealerName = dealerName;
        this.brand = brand;
        this.inventoryMap = inventoryMap;
        this.model = model;
        this.type = type;
        this.dateAdded=LocalDateTime.now();
        calculateQuantity();
        getTotalCost();
    }

    // Getters and Setters
    
    public String getDealerName() { 
    	return dealerName; 
    	}
    
    public void setDealerName(String dealerName) { 
    	this.dealerName = dealerName; 
    	}

    public String getBrand() { 
    	return brand; 
    	}
    
    public void setBrand(String brand) { 
    	this.brand = brand; 
    	}

    public Map<String, SizeInventory> getInventoryMap() {
    	return inventoryMap; 
    	}
    
    public void setInventoryMap(Map<String, SizeInventory> inventoryMap) { 
    	this.inventoryMap = inventoryMap; 
    	}

    public int getQuantity() { 
    	return quantity; 
    	}
    
    public void setQuantity(int quantity) { 
    	this.quantity = quantity; 
    	}

    public String getModel() { 
    	return model; 
    	}
    
    public void setModel(String model) { 
    	this.model = model; 
    	}

    public String getType() { 
    	return type; 
    	}
    
    public void setType(String type) { 
    	this.type = type; 
    	}
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }


    // Methods

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------------------------------\n");
        sb.append("Dealer Name : ").append(dealerName).append("\n");
        sb.append("Brand       : ").append(brand).append("\n");
        sb.append("Model       : ").append(model).append("\n");
        sb.append("Type        : ").append(type).append("\n");

        String dateStr = (dateAdded != null) ? dateAdded.format(formatter) : "N/A";
        sb.append("Date Added  : ").append(dateStr).append("\n");

        sb.append("Total Qty   : ").append(quantity).append("\n");
        sb.append("Total Cost  : ").append(totalCost).append("\n");

        sb.append("Inventory   : \n");
        for (String size : inventoryMap.keySet()) {
            sb.append("   Size ").append(size)
              .append(" -> ").append(inventoryMap.get(size)).append("\n");
        }

        sb.append("--------------------------------------\n");
        return sb.toString();
    }

    public void calculateQuantity() {
        int tempQuantity=0;
        for(SizeInventory s: inventoryMap.values()) {
        	tempQuantity+=s.getQuantity();
        }
        this.quantity=tempQuantity;
    }

    public void getTotalCost() {
    	double tempCost=0;
    	for(SizeInventory s:inventoryMap.values()) {
    		tempCost+=(s.getQuantity()*s.getPrice());
    	}
    	this.totalCost=tempCost;
    }
    
}//Class Product ends here
