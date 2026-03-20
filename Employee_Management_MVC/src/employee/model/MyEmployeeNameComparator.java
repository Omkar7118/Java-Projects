package employee.model;

import java.util.Comparator;

public class MyEmployeeNameComparator implements Comparator {

//	Methods 
	
	public int compare(Object o1, Object o2) {
		Employee e1=(Employee)o1;
		Employee e2=(Employee)o2;
		return e1.name.compareTo(e2.name);
	}

}//class MyEmployeeNameComparator ends here
