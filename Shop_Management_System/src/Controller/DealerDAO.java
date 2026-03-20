package Controller;

import java.io.*;
import java.util.*;

import Model.Dealer;

public class DealerDAO {

    // Attributes
    private List<Dealer> dealers = new ArrayList<>();

    // Constructor
    public DealerDAO() {
        if (dealers.isEmpty()) {
            // Hardcoded dealer entries
            dealers.add(new Dealer("Adidas Dealer", "Pune","9876543215"));
            dealers.add(new Dealer("Nike Sports Dealer", "Mumbai", "9123456786"));
            dealers.add(new Dealer("RayBan Outlet", "Delhi", "9988776654"));
            dealers.add(new Dealer("Samsung Distributor", "Bangalore", "9001122335"));
            dealers.add(new Dealer("Puma Dealer", "Hyderabad","9090909099"));

            saveToFile();
        } else {
            loadFromFile();
        }
    }

    public DealerDAO(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    // Getters and Setters
    public List<Dealer> getAllDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    // Save to file
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Dealers.txt"))) {

            oos.writeObject(dealers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load from file
    public void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Dealers.txt"))) {

            dealers = (List<Dealer>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore if file is empty at start
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Add dealer
    public void addDealer(Dealer dealer) {
        dealers.add(dealer);
        saveToFile();
    }

    // Remove dealer by name
    public void removeDealer(String dealerName) {
        Iterator<Dealer> itr = dealers.iterator();
        while (itr.hasNext()) {
            Dealer d = itr.next();
            if (d.getName().equalsIgnoreCase(dealerName)) {
                itr.remove();
            }
        }
        saveToFile();
    }

    // Display all dealers
    public void displayAllDealers() {
        for (Dealer d : dealers) {
            System.out.println(d);
        }
    }
    
    public Dealer verifyDealer(String name) {
    	for(Dealer dealer:dealers) {
    		if(dealer.getName().equalsIgnoreCase(name)) {
    			return dealer;
    		}
    	}
    	return null;
    }
}
