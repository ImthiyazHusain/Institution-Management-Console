package view;
import static Util.colors.*;
import static Util.utilities.*;
import static controller.Input.*;
import java.sql.*;
import java.util.regex.Matcher;

public class viewAdmin {
    public static boolean getPass() {
        System.out.print("Enter your Password : ");
        return ADMIN_PASSWORD.equals(getString());
    }

    public static int getOption() {
        System.out.print(BRIGHT_CYAN + "1. Add Students\n2. Remove Students\n3. Update Student Data\n4. Display All Students\n" + "5. Fee Pending Students list\n" + RED + "6. Exit" + RESET + "\nEnter Your Choice : ");
        int opt = getInt();
        while (opt <= 0 || opt > 6) {
            System.out.print(RED + "Invalid Option!!!❌\nTry Again : " + RESET);
            opt = getInt();
        }
        return opt;
    }

    public static String getName() {
        System.out.print("Enter the Student Name : ");
        return getString();
    }

    public static int getAge() {
        System.out.print("Enter the Student Age : ");
        int age;
        while(true){
            age = getInt();
            if(age<18 || age>50){
                System.out.print(RED+"Not a Valid Age!!!,"+RESET+" Try Again : ");
            }else{
                break;
            }
        }
        return age;
    }

    public static int getHsc() {
        System.out.print("Enter Student's HSC Mark (For 600) : ");
        int HSC;
        while(true){
            HSC = getInt();
            if(HSC<0 || HSC>600){
                System.out.print(RED+"Not a Valid Mark!!!,"+RESET+" Try Again : ");
            }else{
                break;
            }
        }
        return HSC;
    }

    public static void printCourse(ResultSet rs) throws SQLException {
        System.out.println(GREEN + "\n---Courses Available---" + RESET);
        System.out.printf("%-5s %-35s %-10s\n", "S.no", "Course Name", "Fee");
        System.out.println("_____________________________________________________");
        while (rs.next()) {
            System.out.printf("%-5d %-35s fee: %-10d\n", rs.getInt(1), rs.getString(2), rs.getInt(3));
        }
        System.out.println("_____________________________________________________");
    }

    public static int getCourseID(){
        System.out.print("Select By, course ID : ");
        int id = getInt();
        while (id < 1 || id > MAX_COURSE_ID) {
            System.out.println("Invalid Selection");
            System.out.print("Select By, course ID : ");
            id = getInt();
        }
        return id;
    }

    public static void printSelectedCourse(ResultSet rs) throws SQLException {
        if(rs.next()){
            System.out.println("Selected Course : "+rs.getString(2));
        }else{
            System.out.println(RED+"No Available Course"+RESET);
        }
    }

    public static boolean valid() {
        System.out.print("Do you want to change Anything ? " + GREEN + "(y/n)" + RESET + " : ");
        String yesNo = getString().toLowerCase();
        return yesNo.equals("y");
    }

    public static int validWhat() {
        System.out.println("1. Name\n2. Age\n3. HSC Mark\n4. Course");
        System.out.println("What do you want to Change? : ");
        return getInt();
    }

    public static int getPaidFee() {
        System.out.println("Enter the Amount Paid : ");
        return getInt();
    }

    public static String getGender() {
        System.out.print("Gender : ");
        return getString();
    }

    public static String getMail() {
        System.out.print("Enter the Student Mail : ");
        while (true) {
            String mail = getString();
            Matcher m = GMAIL_PATTERN.matcher(mail);
            if (m.matches()) {
                return mail;
            } else {
                System.out.print(RED + "Wrong mail, " + RESET + "Try Again : ");
            }
        }
    }

    public static boolean verifyOtp(String genOtp) {
        System.out.print("Enter the OTP: ");
        boolean match = genOtp.equals(getString());
        if (!match) {
            System.out.println(RED + "Wrong OTP!" + RESET);
        }
        return match;
    }

    public static void mailVerified() {
        System.out.println(GREEN + "✅ Mail Verified" + RESET);
    }

    public static void invalid() {
        System.out.println(RED + "Invalid Option!!!" + RESET);
    }

    public static void emailSentSuccessfully(){
        System.out.println(GREEN+"✅ Email sent successfully!"+RESET);
    }

    private static void printStudentTableHeader() {
        System.out.println("----|--------------------|-----|------------------------------|-------------|");
        System.out.printf("%-5s %-20s %-5s %-30s %-7s\n", "ID", "Name", "Age", "Department", "Balance Fee");
        System.out.println("----|--------------------|-----|------------------------------|-------------|");
    }

    public static int displayAll(ResultSet rs) throws SQLException {
        int f =0;
        printStudentTableHeader();
        while(rs.next()){
            f=1;
            printStudentDetails(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(7), rs.getInt(9));
        }
        System.out.println("----|--------------------|-----|------------------------------|-------------|");
        return f;
    }

    public static void printStudentDetails(int id, String name, int age ,String dept, int bal_fee ){
        System.out.printf("%-5d %-20s %-5d %-30s %-7d \n",id,name,age,dept,bal_fee);
    }

    public static void updateStatus(int rs) {
        System.out.println((rs!=1)?RED + "Error Occurred" + RESET:GREEN + "---Student Added Successfully---" + RESET);
    }

    public static void updateStatus(String status) {
        System.out.println(status);
    }
    public static void updateStatus(String colStrt, String status, String colEnd) {
        System.out.println(colStrt+status+colEnd);
    }

    public static void displayFeePending(ResultSet rs) throws SQLException {
        System.out.println("\nStudent List");
        int f = 0;
        printStudentTableHeader();
        while (rs.next()) {
            f = 1;
            System.out.printf("%-5d %-20s %-5d %-30s %-7d \n", rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(7), rs.getInt(9));

        }
        System.out.println("----|--------------------|-----|------------------------------|-------------|");

        if (f == 0) {
            System.out.println(RED + "---No Student Available---" + RESET);
        }
    }

    public static boolean askToSendMail(){
        System.out.print("Do you want to send them a reminder? (y/n) : ");
        String option = getString();
        return option.equalsIgnoreCase("y");
    }
}
