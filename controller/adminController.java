package controller;

import model.*;
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
            System.out.println("Error Occurred");
        }
    }

    public static boolean verify(){
        return viewAdmin.getPass();
    }

    public static int displayOptions() throws SQLException {
        int option = viewAdmin.getOption();
        if(option==1) { addStudent();return 1;}
        else if(option==2){ removeStudents(); return 1;}
        else if(option==3){ viewAdmin.updateStatus("Feature Not Yet Enabled..."); return 1;}
        else if(option==4){ displayAllDOA(0);return 1;}
        else if(option==5){ feePending();return 1;}
        else return 0;
    }

    public static courses chooseCourse() throws SQLException {
        String qry = "SELECT * FROM courses";
        String qry2 = "SELECT * FROM courses where CID = ?";
        Statement st = con.createStatement();
        PreparedStatement pst = con.prepareStatement(qry2);
        ResultSet rs = st.executeQuery(qry);
        viewAdmin.printCourse(rs);
        int courseSelected = viewAdmin.getCourseID();
        pst.setInt(1,courseSelected);
        rs = pst.executeQuery();
        if(rs.next()) {
            viewAdmin.printSelectedCourse(rs);
            return new courses(rs.getInt(1), rs.getString(2), rs.getInt(3));
        }else{
            viewAdmin.updateStatus(RED,"Invalid Course ID!!!",RED);
            return chooseCourse();
        }
    }

    public static void addStudent() throws SQLException {
        String name = viewAdmin.getName();
        int age = viewAdmin.getAge();
        String gender = viewAdmin.getGender();
        int hsc = viewAdmin.getHsc();
        courses c = chooseCourse();
        String email = verifyEmailWithOTP();
        while(viewAdmin.valid()){
            int op = viewAdmin.validWhat();
            switch (op){
                case 1:
                    name = viewAdmin.getName();
                    break;
                case 2:
                    age = viewAdmin.getAge();
                    break;
                case 3:
                    hsc = viewAdmin.getHsc();
                    break;
                case 4:
                    c = chooseCourse();
                    break;
                default:
                    viewAdmin.invalid();
            }
        }
        int amtPaid = viewAdmin.getPaidFee();
        int bal = c.getFee()-amtPaid;
        int Stud_ID = getLastStudentId()+1;
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
        viewAdmin.updateStatus(rs);
    }

    public static void displayAllDOA(int n) throws SQLException {
        String qry = "SELECT * FROM students";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(qry);
        System.out.println("\nStudent List");
        int f = viewAdmin.displayAll(rs);
        if (f == 0) {
            viewAdmin.updateStatus(RED,"---No Student Available---",RESET);
        } else {
            if (n == 1) {
                viewAdmin.updateStatus("Enter the Student ID to remove : ");
                int id = getInt();
                removeStudent(id);
            }
        }
        System.out.println();
    }

    public static void removeStudents() throws SQLException {
        displayAllDOA(1);
    }

    public static void removeStudent(int id) throws SQLException {
        String qry = "DELETE FROM students where Stud_ID = ?";
        PreparedStatement pst = con.prepareStatement(qry);
        pst.setInt(1,id);
        int row = pst.executeUpdate();
        if (row != 0) {
            viewAdmin.updateStatus(GREEN,"Student Removed From the DB!!!",RESET);
        } else {
            viewAdmin.updateStatus(RED,"Invalid ID",RESET);
        }
    }

    public static void sendMailFeePending() throws SQLException {
        String qry = "SELECT Stud_Name,Email,Course_Name,Amt_Balance FROM students where Amt_Balance != 0";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(qry);
        viewAdmin.updateStatus("Sending Mails...");
        while(rs.next()){
            String name = rs.getString(1);
            String email = rs.getString(2);
            String Course = rs.getString(3);
            int balanceFee = rs.getInt(4);
            EmailSender.FeePending(email, "Fee Reminder", "Your son/daughter Studying in "+Course+" has a pending Fee Amount of "+balanceFee+"/- make sure to pay the balance on time,\n\nThank you");
        }
        viewAdmin.updateStatus("✅ Mails Successfully Sent");
    }

    private static String verifyEmailWithOTP() {
        while (true) {
            String email = viewAdmin.getMail();
            String otp = OTPgenerator.generateOTP();
            EmailSender.sendEmail(email, "Verification", "Your OTP is: " + otp);
            if (viewAdmin.verifyOtp(otp)) {
                viewAdmin.mailVerified();
                return email;
            }
        }
    }

    public static int getLastStudentId() throws SQLException {
        String qry = "SELECT MAX(Stud_ID) FROM Students";
        PreparedStatement pst = con.prepareStatement(qry);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

//    public static void update() throws SQLException {
//        displayAllDOA(0);
//        String name = viewAdmin.getName();
//        int age = viewAdmin.getAge();
//        int hsc = view.getHsc();
//        courses c =chooseCourse();
//        String email;
//        while (true) {
//            email = view.getMail();
//            String GenOtp = OTPgenerator.generateOTP();
//            EmailSender.sendEmail(email, "Verification", "Your OTP is: " + GenOtp);
//            boolean verifyOtp = view.verifyOtp(GenOtp);
//            if(!verifyOtp){
//                continue;
//            }else {
//                view.mailVerified();
//                break;
//            }
//        }
//        while(view.valid()){
//            int op = view.validWhat();
//            switch (op){
//                case 1:
//                    name = view.getName();
//                    break;
//                case 2:
//                    age = view.getAge();
//                    break;
//                case 3:
//                    hsc = view.getHsc();
//                    break;
//                case 4:
//                    c = chooseCourse();
//                    break;
//                default:
//                    view.invalid();
//            }
//        }
//        int amtPaid = view.getPaidFee();
//        int bal = c.getFee()-amtPaid;
//        int Stud_ID = getLastCourseId()+1;
//
//        String qry = "insert into students values(?,?,?,?,?,?,?,?,?,?)";
//        con.setAutoCommit(false);
//        PreparedStatement pst = con.prepareStatement(qry);
//        pst.setInt(1, Stud_ID);
//        pst.setString(2, name);
//        pst.setInt(3, age);
//        pst.setInt(4, hsc);
//        pst.setString(5, email);
//        pst.setInt(6, c.getId());
//        pst.setString(7, c.getCourseName());
//        pst.setInt(8, amtPaid);
//        pst.setInt(9, bal);
//        pst.setInt(10, c.getFee());
//        int rs = pst.executeUpdate();
//        view.updateStatus(rs);
//    }

    public static void feePending() throws SQLException {
        String qry = "SELECT * FROM students where Amt_Balance  != 0";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(qry);
        viewAdmin.displayFeePending(rs);
        if(viewAdmin.askToSendMail()) {
            sendMailFeePending();
        }
    }
}
