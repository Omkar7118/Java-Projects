package employee.model;

import java.util.Comparator;

public class MyEmployeeSalaryComparator implements Comparator {

//	Methods 
	
	public int compare(Object o1, Object o2) {
		Employee e1=(Employee)o1;
		Employee e2=(Employee)o2;
		return (int) (e1.salary-e2.salary);
	}

}//class MyEmployeeSalaryComparator ends here
