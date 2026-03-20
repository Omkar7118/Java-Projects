package Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import Model.Category;

public class CategoryDAO {
	
//	Attributes

	ArrayList<Category> categories=new ArrayList<Category>();

//	Constructors
	
	public CategoryDAO() {
		if(categories.isEmpty()) {
        categories.add(new Category("Shoes"));
        categories.add(new Category("Clothing"));
        categories.add(new Category("Accessories"));
        categories.add(new Category("Electronics"));
        categories.add(new Category("Furniture"));
        categories.add(new Category("Sports Equipment"));
        categories.add(new Category("Watches"));
        categories.add(new Category("Bags"));
		}
		else 
		{
			loadFromFile();
		}
		saveToFile();
	}
	
	public CategoryDAO(ArrayList<Category> categories) {
		this.categories = categories;	
	}

	
//	Setters and Getters
	
	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}
	
//	Methods
	
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Category.txt"))) {
            oos.writeObject(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("C://Users//LENOVO//Desktop//Personal Projects//Shop_Management_System//Database//Category.txt"))) {
        	categories = (ArrayList<Category>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found is fine initially
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void addCategory(String name) {
    	categories.add(new Category(name));
    	saveToFile();
    }
    
    public void removeCategory(String name) {
    	Iterator<Category> itr=categories.iterator();
    	while(itr.hasNext()) {
    		Category c=itr.next();
    		if(name.equalsIgnoreCase(c.getName())) {
    			itr.remove();
    		}
    	}
    	saveToFile();
    }
    
    
}//Class CategoryDAO ends here
