package view;
import static Util.colors.*;
import static controller.Input.*;

import model.courses;
import controller.*;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class viewAdmin {
    public boolean getPass() {
        System.out.print("Enter your Password : ");
        String crtPass = "Admin123";
        String entPass = getString();
        return crtPass.equals(entPass);
    }
    public int getOption() {
        System.out.print(BRIGHT_CYAN + "1. Add Students\n2. Remove Students\n3. Update Student Data\n4. Display All Students\n" + "5. Fee Pending Students list\n" + RED + "6. Exit" + RESET + "\nEnter Your Choice : ");
        int opt = getInt();
        while (opt <= 0 || opt > 6) {
            System.out.print(RED + "Invalid Option!!!❌\nTry Again : " + RESET);
            opt = getInt();
        }
        return opt;
    }

    public int getID() {
        System.out.print("Enter the Student ID : ");
        return getInt();
    }
    public String getName() {
        System.out.print("Enter the Student Name : ");
        return getString();
    }
    public int getAge() {
        System.out.print("Enter the Student Age : ");
        return getInt();
    }
    public int getHsc() {
        System.out.print("Enter Student's HSC Mark (For 600) : ");
        return getInt();
    }
    public courses ChooseCourse() throws SQLException {
        String qry = "SELECT * FROM courses";
        String qry2 = "SELECT * FROM courses where CID = ?";
        Connection con = DbConnection.getConnection();
        Statement st = con.createStatement();
        PreparedStatement pst = con.prepareStatement(qry2);
        ResultSet rs = st.executeQuery(qry);
        System.out.println(GREEN + "\n---Courses Available---" + RESET);
        System.out.printf("%-5s %-35s %-10s\n", "S.no", "Course Name", "Fee");
        System.out.println("_____________________________________________________");
        while (rs.next()) {
            System.out.printf("%-5d %-35s fee: %-10d\n", rs.getInt(1), rs.getString(2), rs.getInt(3));
        }
        System.out.println("_____________________________________________________");
        System.out.print("Select By, course ID : ");
        int id = getInt();
        while (id <= 0 || id > 5) {
            System.out.println("Invalid Selection");
            System.out.print("Select By, course ID : ");
            id = getInt();
        }
        pst.setInt(1, id);
        rs = pst.executeQuery();
        if (rs.next()) {
            System.out.println("Selected course : " + GREEN + rs.getString(2) + RESET);
            return new courses(rs.getInt(1), rs.getString(2), rs.getInt(3));
        } else {
            System.out.println(RED + "Course with ID " + id + " does not exist." + RESET);
            return null;
        }
    }
    public boolean valid() {
        System.out.print("Do you want to change Anything ?" + GREEN + " y/n" + RESET + " : ");
        String yesNo = getString().toLowerCase();
        return yesNo.equals("y");
    }
    public int validWhat() {
        System.out.println("1. Name\n2. Age\n3. HSC Mark\n4. Course");
        System.out.println("What do u want to Change? : ");
        return getInt();
    }
    public int getPaidFee() {
        System.out.println("Enter the Amount Paid : ");
        return getInt();
    }
    public String getGender() {
        System.out.print("Gender : ");
        return getString();
    }
    public String getMail() {
        System.out.print("Enter the Student Mail : ");
        while (true) {
            String mail = getString();
            Pattern p = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
            Matcher m = p.matcher(mail);
            if (m.matches()) {
                return mail;
            } else {
                System.out.print(RED + "Wrong mail, " + RESET + "Try Again : ");
            }
        }
    }
    public String verifyOtp(String GenOtp) {
        System.out.print("Enter the OTP : ");
        String otp = getString();
        if (GenOtp.equals(otp)) {
            return "true";
        }
        System.out.println(RED + "Wrong OTP!" + RESET);
        return "retry";
    }
    public void MailVerified() {
        System.out.println(GREEN + "✅ Mail Verified" + RESET);
    }

    public void invalid() {
        System.out.println(RED + "Invalid Option!!!" + RESET);
    }

    public void updateStatus(int rs) throws SQLException {
        if (rs != 1) {
            System.out.println(RED + "Error Occurred" + RESET);
        } else {
            System.out.println(GREEN + "---Student Added Successfully---" + RESET);
        }
    }
    public void updateStatus(String status) throws SQLException {
        System.out.println(status);
    }



    public void feePending() throws SQLException {
        String qry = "SELECT * FROM students where Amt_Balance  != 0";
        Connection con = DbConnection.getConnection();
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
        }
        System.out.println("Do you want to send then a remainder? y/n : ");
        String option = getString();
        if(option.toLowerCase().equals("y")){
            adminController.sendMailFeePending();
        }
    }
}
