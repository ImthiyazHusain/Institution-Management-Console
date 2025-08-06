package view;

import java.util.Scanner;

public class viewLogin {
    private Scanner sc = new Scanner(System.in);
    public void welcome(){
        System.out.println("\n"+colors.BRIGHT_WHITE+"---Institution Management System---\n"+colors.RESET);
    }
    public void error(){
        System.out.println(colors.RED+"Wrong Credentials,Try Again"+colors.RESET);
    }
    public void success(){
        System.out.println(colors.GREEN+"---Login Successful---"+colors.RESET);
    }
    public int showLoginMenu() {

        System.out.println("Login as:"+colors.YELLOW);
        System.out.println("1. Admin");
        System.out.println("2. Teacher");
        System.out.println("3. Student");
        System.out.println(colors.RED+"4. Exit"+colors.RESET);
        int n = 0;
        while(true) {
            try {
                System.out.print("\nEnter choice: ");
                n = sc.nextInt();
                break;
            } catch (Exception e) {
                System.out.println(colors.RED+"ERROR:"+e+",Try Again!"+colors.RESET);
                sc.nextLine();
            }
        }
        return n;
    }
}
