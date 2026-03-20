package Controller;

import java.util.List;
import java.util.Scanner;

import Model.Category;

public class CategoryService {

	CategoryDAO categoryDAO=new CategoryDAO();
	Scanner sc=new Scanner(System.in);
	
	public void verifyCategory(String type) {
		boolean found=false;
		List<Category>categories=categoryDAO.getCategories();
		for(Category category:categories) {
			if(category.getName().equalsIgnoreCase(type)){
				found=true;
			}
		}
		if(!found) {
			addCategory(type);
		}
	}

	public void addCategory(String name) {
		System.out.println("Category doen't exists");
		System.out.println("Category is Added");
		categoryDAO.addCategory(name);
	}
}
