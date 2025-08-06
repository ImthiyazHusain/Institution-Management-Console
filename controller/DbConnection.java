package controller;
import java.sql.*;

public class DbConnection {
    private static final String url = "jdbc:mysql://localhost:3306/institution";
    private static final String userName = "root";
    private static final String passWord = "Husain2005@&";
    DBC d = new DBC();
    String a = d.name;

    public static Connection getConnection() throws SQLException{
        return  DriverManager.getConnection(url,userName,passWord);
    }

}