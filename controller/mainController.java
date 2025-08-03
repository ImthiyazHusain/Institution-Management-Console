package controller;
import view.viewLogin;

import java.sql.SQLException;

public class mainController {
    public void start() throws SQLException {
        viewLogin view = new viewLogin();
        int choice = view.showLoginMenu();
        switch (choice) {
            case 1:
                while(!adminController.verify()){
                    System.out.println("Wrong Credentials\nTry Again");
                }
                System.out.println("---Login Successful---");
                int run = 1;
                while(run==1){
                    run = adminController.displayOptions();
                }
                break;
            case 2:
                System.out.println("Teacher login coming soon...");
                break;
            case 3:
                System.out.println("Student login coming soon...");
                break;
            case 4:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
