package controller;
import java.sql.*;

public class DbConnection {

    private static final String userName = DBC.userName;
    private static final String passWord = DBC.passWord;
    private static final String url = DBC.url;


    public static Connection getConnection() throws SQLException{
        return  DriverManager.getConnection(url,userName,passWord);
    }

}