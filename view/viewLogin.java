package view;

import java.util.Scanner;

public class viewLogin {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    private Scanner scanner = new Scanner(System.in);
    public void welcome(){
        System.out.println("\n\t\tWelcome to Institution Management System!!!\n");
    }
    public void error(){
        System.out.println("Wrong Credentials\nTry Again");
    }
    public void success(){
        System.out.println("---Login Successful---");
    }
    public int showLoginMenu() {

        System.out.println("Login as:"+YELLOW);
        System.out.println("1. Admin");
        System.out.println("2. Teacher");
        System.out.println("3. Student");
        System.out.println("4. Exit"+RESET);
        System.out.print("\nEnter choice: ");
        return Integer.parseInt(scanner.nextLine());
    }
}
