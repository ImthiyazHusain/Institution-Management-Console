package view;
import model.courses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Util.colors.*;
import static Util.Input.*;

public class viewTeacher {
    public static int getTeacherID(){
        System.out.print("Enter your ID : ");
        return getInt();
    }
    public static String getTeacherPass(){
        System.out.print("Enter your Password : ");
        return getString();
    }
    public static int getOption() {
        System.out.print(BRIGHT_CYAN + "\n1. Take Attendance \n2. Enter Marks"+"\n3. Change Password "+"\n4. DisplayMarks "+ RED + "\n5. Exit" + RESET + "\n\nEnter Your Choice : ");
        int opt = getInt();
        while (opt <= 0 || opt > 5) {
            System.out.print(RED + "Invalid Option!!!❌\nTry Again : " + RESET);
            opt = getInt();
        }
        return opt;
    }
    public static String getNewPass(){
        System.out.print("Enter your New Password : ");
        return getString();
    }
    public static void updateStatus(String color,String msg,String endCol){
        System.out.print(color+msg+endCol);
    }
    public static String getDate(){
        System.out.print("Enter the date (format dd_mm_yyyy) : ");
        return getString();
    }
    public static int getCID(){
        System.out.print("Enter the Course ID : ");
        return getInt();
    }
    public static void printCourse(List<courses> courseList) throws SQLException {
        System.out.printf("%-5s %-35s \n", "S.no", "Course Name");
        for (courses c : courseList) {
            System.out.printf("%-5d %-35s \n",
                    c.getId(),
                    c.getCourseName());
        }
    }

    public static int currentStudent(String name) {
        System.out.printf("%-20s Mark : ",name);
        return getInt();
    }

    public static String getSubjects() {
        System.out.print("Enter Subject Name  : ");
        return getString();
    }

    public static void printRow(String[] row, int[] colWidths) {
            StringBuilder sb = new StringBuilder("|");
            for (int i = 0; i < row.length; i++) {
                sb.append(" ").append(String.format("%-" + colWidths[i] + "s", row[i])).append(" |");
            }
            System.out.println(sb);
    }
}
