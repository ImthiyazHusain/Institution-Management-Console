package app;

import controller.mainController;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        mainController cntrl = new mainController();
        cntrl.start();
    }
}
