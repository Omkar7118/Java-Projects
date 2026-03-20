package employee.view;

import java.util.Scanner;

import employee.controller.EmployeeDAO;
import employee.model.Admin;
import employee.model.Employee;
import employee.model.HR;
import employee.model.SalesManager;

public class MainMenu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmployeeDAO edao=new EmployeeDAO();
		Scanner sc=new Scanner(System.in);
		int choice;
		do {
			System.out.println("Enter");
			System.out.println("1.Add employee");
			System.out.println("2.Update employee");
			System.out.println("3.Remove employee");
			System.out.println("4.Search employee");
			System.out.println("5.Sort employee");
			System.out.println("6.Calculate salary ");
			System.out.println("7.Display employees");
			System.out.println("Enter 0 to exit");
			choice=sc.nextInt();
			

		switch(choice) {
			case 1:
				int empType;
				System.out.println("Enter");
				System.out.println("1.Add Admin");
				System.out.println("2.Add HR");
				System.out.println("3.Add Sales Manager");
				empType=sc.nextInt();
				Employee employee=null;
				switch(empType) {
				case 1 : 
					System.out.println("Admin");
					System.out.println("Enter employee id : ");
					int id=sc.nextInt();
					System.out.println("Enter employee name : ");
					String name=sc.next();
					System.out.println("Enter employee salary : ");
					double salary=sc.nextDouble();
					System.out.println("Enter employee allowance : ");
					double allowance=sc.nextDouble();
					employee = new Admin(id, name, salary, allowance);
		            break;

					
				case 2 : 
					System.out.println("HR");
					System.out.println("Enter employee id : ");
					int id1=sc.nextInt();
					System.out.println("Enter employee name : ");
					String name1=sc.next();
					System.out.println("Enter employee salary : ");
					double salary1=sc.nextDouble();
					System.out.println("Enter employee commision : ");
					double commission=sc.nextDouble();
					employee = new HR(id1, name1, salary1, commission);
		            break;

				
				case 3 : 
					System.out.println("Sales Manager");
					System.out.println("Enter employee id : ");
					int id11=sc.nextInt();
					System.out.println("Enter employee name : ");
					String name11=sc.next();
					System.out.println("Enter employee salary : ");
					double salary11=sc.nextDouble();
					System.out.println("Enter employee target : ");
					int target=sc.nextInt();
					System.out.println("Enter employee incentive : ");
					double incentive=sc.nextDouble();				
					employee=new SalesManager(id11,name11,salary11,target,incentive);
		            break;
				}
				edao.addEmp(employee);
				break;
				
			case 2:
				System.out.println("Enter employee id you want to update : ");
				int tempId=sc.nextInt();
				edao.updateEmp(tempId);
				break;
				
			case 3:
				System.out.println("Enter employee id you want to remove : ");
				int tempId1=sc.nextInt();
				edao.removeEmp(tempId1);
				break;
		
			case 4:
				System.out.println("Enter employee id you want to search : ");
				int tempId2=sc.nextInt();
				edao.searchEmp(tempId2);
				break;
				
			case 5:
				edao.sortEmp();
				break;
			
			case 6:
				System.out.println("Enter emp id : ");
				int tempId3=sc.nextInt();
				edao.calSalEmp(tempId3);
				break;
				
			case 7:
				edao.displayEmp();
				break;
			}
		}while(choice!=0);
	}

}
