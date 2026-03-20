package bank;

import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankEmployeesDetails employee = new BankEmployeesDetails();

        while (true) {   // loop for continuous login
            System.out.println("\n===== Login Menu =====");
            System.out.print("Enter Login ID (or 0 to exit): ");
            int id = sc.nextInt();
            sc.nextLine(); // consume newline

            if (id == 0) {
                System.out.println("Exiting system. Goodbye!");
                break; // exit program
            }

            System.out.print("Enter Password: ");
            String password = sc.next();

            boolean success = employee.verifyUser(id, password);

            if (!success) {
                System.out.println("\n❌ Login failed. Please try again.");
            }
        }
        sc.close();
    }
}