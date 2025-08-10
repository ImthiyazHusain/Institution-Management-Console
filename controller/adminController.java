package controller;

import model.*;
import view.EmailSender;
import view.viewAdmin;
import static controller.Input.*;
import java.sql.*;
import static Util.colors.*;


public class adminController {
    public static Connection con;

    static {
        try {
            con = DbConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(){
        viewAdmin view = new viewAdmin();
        return view.getPass();
    }
    public static int displayOptions() throws SQLException {
        viewAdmin view = new viewAdmin();
        int option = view.getOption();
        if(option==1) {addStudent();return 1;}
        else if(option==2){ removeStudents(); return 1;}
        else if(option==3){ update(); return 1;}
        else if(option==4){displayAll(0);return 1;}
        else if(option==5){view.feePending();return 1;}
        else return 0;
    }
    public static void addStudent() throws SQLException {
        viewAdmin view = new viewAdmin();
        String name = view.getName();
        int age = view.getAge();
        String gender = view.getGender();
        int hsc = view.getHsc();
        courses c = view.ChooseCourse();
        String email;
        while (true) {
            email = view.getMail();
            String GenOtp = OTPgenerator.generateOTP();
            EmailSender.sendEmail(email, "Verification", "Your OTP is: " + GenOtp);
            String verifyOtp = view.verifyOtp(GenOtp);
            if(verifyOtp.equals("retry")){
                continue;
            }else if(verifyOtp.equals("true")){
                view.MailVerified();
                break;
            }
        }
        while(view.valid()){
            int op = view.validWhat();
            switch (op){
                case 1:
                    name = view.getName();
                    break;
                case 2:
                    age = view.getAge();
                    break;
                case 3:
                    hsc = view.getHsc();
                    break;
                case 4:
                    c = view.ChooseCourse();
                    break;
                default:
                    view.invalid();
            }
        }
        int amtPaid = view.getPaidFee();
        int bal = c.getFee()-amtPaid;
        int Stud_ID = getLastCourseId()+1;
        String qry = "insert into students values(?,?,?,?,?,?,?,?,?,?,?)";;
        PreparedStatement pst = con.prepareStatement(qry);
        pst.setInt(1, Stud_ID);
        pst.setString(2, name);
        pst.setInt(3, age);
        pst.setInt(4, hsc);
        pst.setString(5, email);
        pst.setInt(6, c.getId());
        pst.setString(7, c.getCourseName());
        pst.setInt(8, amtPaid);
        pst.setInt(9, bal);
        pst.setInt(10, c.getFee());
        pst.setString(11, gender);
        int rs = pst.executeUpdate();
        view.updateStatus(rs);
    }
    public static void displayAll(int n) throws SQLException {
        String qry = "SELECT * FROM students";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(qry);
        System.out.println("\nStudent List");
        int f = 0;
        System.out.println("----+--------------------+-----+------------------------------+-----------+");
        System.out.printf("%-5s %-20s %-5s %-30s %-7s\n", "ID", "Name", "Age", "Department", "Balance Fee");
        System.out.println("----+--------------------+-----+------------------------------+-----------+");
        while (rs.next()) {
            f = 1;
            System.out.printf("%-5d %-20s %-5d %-30s %-7d \n", rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(7), rs.getInt(9));

        }
        System.out.println("----+--------------------+-----+------------------------------+-----------+");

        if (f == 0) {
            System.out.println(RED + "---No Student Available---" + RESET);
        } else {
            if (n == 1) {
                int id = getInt();
                removeStudent(id);
            }
        }
        System.out.println();
    }
    public static void removeStudents() throws SQLException {
        viewAdmin view = new viewAdmin();
        displayAll(1);
    }
    public static void removeStudent(int id) throws SQLException {
        String qry = "DELETE FROM students where Stud_ID = " + id;
        Statement st = con.createStatement();
        int row = st.executeUpdate(qry);
        if (row != 0) {
            System.out.println(GREEN + "Student Removed From the DB!!!" + RESET);
            return;
        } else {
            System.out.println(RED + "Invalid ID" + RESET);
        }
    }
    public static void sendMailFeePending() throws SQLException {
        viewAdmin view = new viewAdmin();
        String qry = "SELECT Stud_Name,Email,Course_Name,Amt_Balance FROM students where Amt_Balance != 0";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(qry);
        view.updateStatus("Sending Mails...");
        while(rs.next()){
            String name = rs.getString(1);
            String email = rs.getString(2);
            String Course = rs.getString(3);
            int balanceFee = rs.getInt(4);
            EmailSender.FeePending(email, "Fee Remainder", "Your son/daughter Studying in "+Course+" has a pending Fee Amount of "+balanceFee+"/- make sure to pay the balance on time,\n\nThank you");
        }
        view.updateStatus("✅ Mails Successfully Sent");
    }
    public static int getLastCourseId() throws SQLException {
        String qry = "SELECT Stud_ID FROM Students ORDER BY Stud_ID DESC LIMIT 1";
        PreparedStatement pst = con.prepareStatement(qry);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }
    public static void update() throws SQLException {
        displayAll(0);
        viewAdmin view = new viewAdmin();
        String name = view.getName();
        int age = view.getAge();
        int hsc = view.getHsc();
        courses c = view.ChooseCourse();
        String email;
        while (true) {
            email = view.getMail();
            String GenOtp = OTPgenerator.generateOTP();
            EmailSender.sendEmail(email, "Verification", "Your OTP is: " + GenOtp);
            String verifyOtp = view.verifyOtp(GenOtp);
            if(verifyOtp.equals("retry")){
                continue;
            }else if(verifyOtp.equals("true")){
                view.MailVerified();
                break;
            }
        }
        while(view.valid()){
            int op = view.validWhat();
            switch (op){
                case 1:
                    name = view.getName();
                    break;
                case 2:
                    age = view.getAge();
                    break;
                case 3:
                    hsc = view.getHsc();
                    break;
                case 4:
                    c = view.ChooseCourse();
                    break;
                default:
                    view.invalid();
            }
        }
        int amtPaid = view.getPaidFee();
        int bal = c.getFee()-amtPaid;
        int Stud_ID = getLastCourseId()+1;

        String qry = "insert into students values(?,?,?,?,?,?,?,?,?,?)";
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement(qry);
        pst.setInt(1, Stud_ID);
        pst.setString(2, name);
        pst.setInt(3, age);
        pst.setInt(4, hsc);
        pst.setString(5, email);
        pst.setInt(6, c.getId());
        pst.setString(7, c.getCourseName());
        pst.setInt(8, amtPaid);
        pst.setInt(9, bal);
        pst.setInt(10, c.getFee());
        int rs = pst.executeUpdate();
        view.updateStatus(rs);
    }
}
