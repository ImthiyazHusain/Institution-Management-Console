package controller;

import DOA.studentDOA;
import view.*;
import java.sql.SQLException;

public class studentController {
    public static String name;
    public static boolean verify() {
        name = viewStudent.getStudentName();
        String pass = viewStudent.getStudentPass();
        return validateStudent(name,pass);
    }

    public static boolean validateStudent(String name,String pass){
        return studentDOA.getPass(name).equals(pass);
    }

    public static int displayOptions() throws SQLException {
        int option = viewStudent.getOption();
        if(option==1) { attendanceDetailsOption(); return 1;}
        else if(option==2){  markDetailsOption(); return 1;}
        else if(option==3){  ;return 1;}
        else if(option==4){  ;return 1;}
        else if(option==5){  ;return 1;}
        else return 0;
    }

    private static void attendanceDetailsOption() {
        studentDOA.attendanceView(name);
    }
    private static void markDetailsOption() {
        studentDOA.markView(name);
    }

    public static void greet() {
        viewStudent.greet(name);
    }
}
