package view;

import static Util.colors.*;
import controller.Input;

public class viewLogin {
    public void welcome(){
        System.out.println("-------------------------------------------");
        System.out.println(BRIGHT_GREEN+"\t---Institution Management System---"+RESET);
        System.out.println("-------------------------------------------");
    }

    public void error(){
        System.out.println(RED+"Wrong Credentials, Try Again"+RESET);
    }

    public void success(){
        System.out.println(BRIGHT_GREEN+"\n---Login Successful---"+RESET);
    }

    public int showLoginMenu() {
        System.out.println("Login as:");
        System.out.println(BRIGHT_CYAN+"1. Admin");
        System.out.println("2. Teacher");
        System.out.println("3. Student");
        System.out.println(RED+"4. Exit\n"+RESET);
        System.out.print("\nEnter your Choice : ");
        return Input.getInt();
    }
}
