package app;

import controller.mainController;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        mainController cntrl = new mainController();
        cntrl.start();
    }
}
