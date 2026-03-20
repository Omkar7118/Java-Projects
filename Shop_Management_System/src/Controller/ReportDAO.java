package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Model.Product;
import Model.Report;
import Model.SizeInventory;

public class ReportDAO {
	
//	Attributes

	List<Report> reports=new ArrayList<Report>();
	
//	Constructors
	
	public ReportDAO() {
		if(reports.isEmpty()) {
			loadFromFile();
		}
	}
	
//	Methods

	private void saveToFile() {
		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Reports.txt"))){
			oos.writeObject(reports);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadFromFile() {

	    File file = new File(
	        "C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Reports.txt"
	    );

	    // ✅ If file does not exist, create empty data and return
	    if (!file.exists()) {
	        reports = new ArrayList<>();
	        saveToFile();   // create empty file
	        return;
	    }

	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	        reports = (List<Report>) ois.readObject();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public void addReport(Product product,String size,int quantity,double sellingPrice) {
		Report report=new Report(product,size,quantity,sellingPrice);
		reports.add(report);
		Map<String, SizeInventory> inventoryMap=product.getInventoryMap();
		SizeInventory sizeInventory=inventoryMap.get(size);
		sizeInventory.reduceQuantity(quantity);
		System.out.println("Report added successfully");
		saveToFile();
	}
	
	public void removeReport(int reportId) {
		Iterator itr=reports.iterator();
		while(itr.hasNext()) {
			Report report=(Report) itr.next();
			if(report.getReportId()==reportId) {
				itr.remove();
			}
		}
		System.out.println("Report removed successfully");
		saveToFile();
	}
	
}
