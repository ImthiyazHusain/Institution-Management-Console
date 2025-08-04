package controller;
import view.viewLogin;

import java.sql.SQLException;

public class mainController {
    public void start() throws SQLException {
        viewLogin view = new viewLogin();
        view.welcome();
        int choice = 0;
        while(choice!=4) {
            choice = view.showLoginMenu();
            switch (choice) {
                case 1:
                    while (!adminController.verify()) {
                        view.error();
                    }
                    view.success();
                    int run = 1;
                    while (run == 1) {
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
}
