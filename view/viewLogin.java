package view;

import java.util.Scanner;

public class viewLogin {
    private Scanner scanner = new Scanner(System.in);

    public int showLoginMenu() {
        System.out.println("\n\t\tWelcome to Institution Management System!!!\n");
        System.out.println("Login as:");
        System.out.println("1. Admin");
        System.out.println("2. Teacher");
        System.out.println("3. Student");
        System.out.println("4. Exit");
        System.out.print("\nEnter choice: ");
        return Integer.parseInt(scanner.nextLine());
    }
}