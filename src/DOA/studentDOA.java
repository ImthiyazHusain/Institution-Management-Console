package DOA;

import Service.EmailSender;
import view.*;
import java.util.*;
import java.sql.*;

public class studentDOA {
    public static boolean addStudent(int Stud_ID,String name,int age,int hsc,String email,int CID,String CName,int amtPaid,int bal,int fee,String gender){
        String attendanceTable = "INSERT INTO attendance (Stud_ID,Stud_Name,CID,Course_Name) VALUES (?,?,?,?)";
        String Mark = "INSERT INTO Marks (Stud_ID,Stud_Name,CID,Course_Name) VALUES (?,?,?,?)";
        String qry = "INSERT INTO students (Stud_ID, Stud_Name, Stud_Age, Stud_HSC_Mark, Email, Course_ID, Course_Name, Amt_Paid, Amt_Balance, Tot_Fee, Gender,Password) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        int rs;
        try(Connection con = DbConnection.getConnection(); )
        {
            String pass = "123456789";
            PreparedStatement pst = con.prepareStatement(qry);
            PreparedStatement pst2 = con.prepareStatement(attendanceTable);
            PreparedStatement pst3 = con.prepareStatement(Mark);
            con.setAutoCommit(false);
            pst.setInt(1, Stud_ID);
            pst.setString(2, name);
            pst.setInt(3, age);
            pst.setInt(4, hsc);
            pst.setString(5, email);
            pst.setInt(6, CID);
            pst.setString(7, CName);
            pst.setInt(8, amtPaid);
            pst.setInt(9, bal);
            pst.setInt(10, fee);
            pst.setString(11, gender);
            pst.setString(12,pass);

            pst2.setInt(1,Stud_ID);
            pst2.setString(2,name);
            pst2.setInt(3,CID);
            pst2.setString(4,CName);

            pst3.setInt(1,Stud_ID);
            pst3.setString(2,name);
            pst3.setInt(3,CID);
            pst3.setString(4,CName);

            rs = pst.executeUpdate();
            if(rs>0){
                rs = pst2.executeUpdate();
                if(rs>0){
                    rs = pst3.executeUpdate();
                }
            }
            if(rs>0){
                con.commit();
            }else con.rollback();
            return rs != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int getLastStudentId() throws SQLException {
        String qry = "SELECT MAX(Stud_ID) FROM Students";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry);ResultSet rs = pst.executeQuery();
        ){
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
    }
    public static void displayAllDOA() throws SQLException {
        String qry = "SELECT * FROM students";
        try(Connection con = DbConnection.getConnection();Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(qry);
            viewAdmin.displayAll(rs);
        }
    }
    public static boolean removeStudent(int id) throws SQLException {
        String attendance = "DELETE FROM attendance where Stud_ID = ?";
        String mark = "DELETE FROM Marks where Stud_ID = ?";
        String qry = "DELETE FROM students where Stud_ID = ?";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            PreparedStatement pst2 = con.prepareStatement(attendance);
            PreparedStatement pst3 = con.prepareStatement(mark);

            pst.setInt(1,id);
            pst2.setInt(1,id);
            pst3.setInt(1,id);
            int row = pst.executeUpdate();
            if(row>0){
                row = pst2.executeUpdate();
            }
            return row>0;
        }
    }
    public static boolean displayFeePendingStudents() throws SQLException {
        String qry = "SELECT * FROM students where Amt_Balance  != 0";
        try(Connection con = DbConnection.getConnection();Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(qry);
            viewAdmin.displayFeePending(rs);
            return viewAdmin.askToSendMail();
        }
    }
    public static boolean search(int id) throws SQLException {
        String qry = "SELECT * FROM students where Stud_Id = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setInt(1,id);
            ResultSet rs = pst.executeQuery();
            return viewAdmin.displayAll(rs);
        }
    }
    public static boolean search(String name) throws SQLException {
        String qry = "SELECT * FROM students where Stud_Name = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            return viewAdmin.displayAll(rs);
        }
    }
    public static boolean sendMailFeePending()  throws SQLException{
        String qry = "SELECT Stud_Name,Email,Course_Name,Amt_Balance FROM students where Amt_Balance != 0";
        try(Connection con = DbConnection.getConnection();){
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            viewAdmin.updateStatus("Sending Mails...");
            int f = 0;
            while(rs.next()){
                f=1;
                String name = rs.getString(1);
                String email = rs.getString(2);
                String Course = rs.getString(3);
                int balanceFee = rs.getInt(4);
                EmailSender.sendEmail(email, "Fee Reminder", "Your son/daughter \""+name+"\" Studying in "+Course+" has a pending Fee Amount of "+balanceFee+"/- make sure to pay the balance on time,\n\nThank you",false);
            }
            return f != 0;
        }
    }
    public static boolean updateStudent(int id,String change,String element) throws SQLException {
        String qry = "update students set "+change+" = ? where Stud_ID = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setString(1,element);
            pst.setInt(2,id);
            int rs = pst.executeUpdate();
            return rs > 0;
        }
    }
    public static boolean updateStudent(int id,String change,int element) throws SQLException {
        String qry = "update students set "+change+" = ? where Stud_ID = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setInt(1,element);
            pst.setInt(2,id);
            int rs = pst.executeUpdate();
            return rs > 0;
        }
    }
    public static boolean updateStudentAmount(int id,String change,int element) throws SQLException {
        String qry = "update students set "+change+" = ? where Stud_ID = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setInt(1,element);
            pst.setInt(2,id);
            int rs = pst.executeUpdate();
            if( rs > 0){
                qry = "select Tot_Fee from students where Stud_ID = ?";
                PreparedStatement pst1 = con.prepareStatement(qry);
                pst1.setInt(1,id);
                ResultSet resultSet = pst1.executeQuery();
                if(resultSet.next()) {
                    int totalAmount = resultSet.getInt(1);
                    int balance = totalAmount - element;
                    qry = "update students set Amt_Balance = ? where Stud_ID = ?";
                    PreparedStatement pst2 = con.prepareStatement(qry);
                    pst2.setInt(1, balance);
                    pst2.setInt(2, id);
                    rs = pst2.executeUpdate();
                    return rs > 0;
                }else return false;
            }
            else return false;
        }
    }
    public static String getPass(String name){
        String qry = "select Password from students where Stud_Name = ?";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            pst.setString(1,name);
            ResultSet r = pst.executeQuery();
            if(r.next()){
                return r.getString(1);
            }else{
                return "";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void attendanceView(String name) {
        String query = "SELECT * FROM attendance where Stud_Name = ?";

        try (Connection con = DbConnection.getConnection())
        {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            List<String[]> rows = new ArrayList<>();

            String[] headers = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                headers[i - 1] = rsmd.getColumnName(i);
            }
            rows.add(headers);

            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i) == null ? "NULL" : rs.getString(i);
                }
                rows.add(row);
            }

            int[] colWidths = new int[columnCount];
            for (String[] row : rows) {
                for (int i = 0; i < columnCount; i++) {
                    colWidths[i] = Math.max(colWidths[i], row[i].length());
                }
            }

            StringBuilder border = new StringBuilder("+");
            for (int w : colWidths) {
                border.append("-".repeat(w + 2)).append("+");
            }

            System.out.println(border);
            viewStudent.printRow(headers, colWidths);
            System.out.println(border);
            for (int i = 1; i < rows.size(); i++) {
                viewStudent.printRow(rows.get(i), colWidths);
            }
            System.out.println(border);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void markView(String name) {
        String query = "SELECT * FROM Marks where Stud_Name = ?";

        try (Connection con = DbConnection.getConnection())
        {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            List<String[]> rows = new ArrayList<>();

            String[] headers = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                headers[i - 1] = rsmd.getColumnName(i);
            }
            rows.add(headers);

            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i) == null ? "NULL" : rs.getString(i);
                }
                rows.add(row);
            }

            int[] colWidths = new int[columnCount];
            for (String[] row : rows) {
                for (int i = 0; i < columnCount; i++) {
                    colWidths[i] = Math.max(colWidths[i], row[i].length());
                }
            }

            StringBuilder border = new StringBuilder("+");
            for (int w : colWidths) {
                border.append("-".repeat(w + 2)).append("+");
            }

            System.out.println(border);
            viewStudent.printRow(headers, colWidths);
            System.out.println(border);
            for (int i = 1; i < rows.size(); i++) {
                viewStudent.printRow(rows.get(i), colWidths);
            }
            System.out.println(border);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean updatePass(String name ,String pass){
        String qry = "update students set Password = ? where Stud_Name = ?";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            pst.setString(1,pass);
            pst.setString(2,name);
            int r = pst.executeUpdate();
            return r>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
