package DOA;
import Util.utilities;

import java.sql.*;

public class DbConnection {

    private static final String userName = utilities.userName;
    private static final String passWord = utilities.passWord;
    private static final String url = utilities.url;


    public static Connection getConnection() throws SQLException{
        return  DriverManager.getConnection(url,userName,passWord);
    }

}