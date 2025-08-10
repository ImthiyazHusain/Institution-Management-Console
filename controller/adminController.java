package controller;
import model.*;
import view.EmailSender;
import view.viewAdmin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;

import static controller.Input.*;

public class adminController {
    public static boolean verify(){
        viewAdmin view = new viewAdmin();
        return view.getPass();
    }
    public static int displayOptions() throws SQLException {
        viewAdmin view = new viewAdmin();
        int option = view.getOption();
        if(option==1) {addStudent();return 1;}
        else if(option==2){ removeStudents(); return 1;}
        else if(option==3){ System.out.println("This feature is not yet enabled..."); return 1;}
        else if(option==4){view.displayAll(0);return 1;}
        else if(option==5){view.feePending();return 1;}
        else return 0;
    }
    public static void addStudent() throws SQLException {
        viewAdmin view = new viewAdmin();
        String name = view.getName();
        int age = view.getAge();
        int hsc = view.getHsc();
        view.showCourseList();
        courses c = view.select();
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
                    view.showCourseList();
                    c = view.select();
                    break;
                default:
                    view.invalid();
            }
        }
        int amtPaid = view.getPaidFee();
        int bal = c.getFee()-amtPaid;
        int Stud_ID = view.getLastCourseId()+1;
        view.updateTable(Stud_ID,name,age,hsc,email,c.getId(),c.getCourseName(),amtPaid,bal,c.getFee());
    }
    public static void removeStudents() throws SQLException {
        viewAdmin view = new viewAdmin();
        view.displayAll(1);
    }
    public static void sendMailFeePending() throws SQLException {
        String qry = "SELECT Stud_Name,Email,Course_Name,Amt_Balance FROM students where Amt_Balance != 0";
        Connection con = DbConnection.getConnection();
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(qry);
        System.out.println("Sending Mails...");
        while(rs.next()){
            String name = rs.getString(1);
            String email = rs.getString(2);
            String Course = rs.getString(3);
            int balanceFee = rs.getInt(4);
            EmailSender.FeePending(email, "Fee Remainder", "Your son/daughter Studying in "+Course+" has a pending Fee Amount of "+balanceFee+"/- make sure to pay the balance on time,\n\nThank you");
        }
        System.out.println("✅ Mails Successfully Sent");
    }
}
