package controller;

import DOA.*;
import model.*;
import view.viewAdmin;

import static Service.verifyEmail.verifyEmailWithOTP;
import static Util.Input.*;
import java.sql.*;
import java.util.*;

import static Util.colors.*;


public class adminController {

    public static boolean verify(){
        return viewAdmin.getPass();
    }

    public static int displayOptions() throws SQLException {
        int option = viewAdmin.getOption();
        if(option==1) { addStudentOption();return 1;}
        else if(option==2){ removeStudentOption(); return 1;}
        else if(option==3){ updateStudentOption(); return 1;}
        else if(option==4){ studentDOA.displayAllDOA();return 1;}
        else if(option==5){ feePending();return 1;}
        else if(option==6){ searchOption();return 1;}
        else if(option==7){ addTeacher();return 1;}
        else return 0;
    }

    public static void addStudentOption() throws SQLException {
        String name = viewAdmin.getName();
        int age = viewAdmin.getAge();
        String gender = viewAdmin.getGender();
        int hsc = viewAdmin.getHsc();
        List<courses> coursesList = courseDOA.getAllCourses();
        viewAdmin.printCourse(coursesList);
        courses c ;
        while (true) {
            int selectedId = viewAdmin.getCourseID();
            c = courseDOA.getCourseById(selectedId);
            if (c != null) {
                viewAdmin.printSelectedCourse(c);
                break;
            } else {
                viewAdmin.updateStatus(RED, "Invalid Course ID!!!", RED);
            }
        }
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
                    viewAdmin.printCourse(coursesList);
                    while (true) {
                        int selectedId = viewAdmin.getCourseID();
                        c = courseDOA.getCourseById(selectedId);
                        if (c != null) {
                            viewAdmin.printSelectedCourse(c);
                            break;
                        } else {
                            viewAdmin.updateStatus(RED, "Invalid Course ID!!!", RED);
                        }
                    }
                    break;
                default:
                    viewAdmin.invalid();
            }
        }
        int amtPaid = viewAdmin.getPaidFee();
        int bal = c.getFee()-amtPaid;
        int Stud_ID = studentDOA.getLastStudentId()+1;

        boolean success = studentDOA.addStudent(Stud_ID,name,age,hsc,email,c.getId(),c.getCourseName(),amtPaid,bal,c.getFee(),gender);
        if(success){
           viewAdmin.updateStatus(GREEN+"Student Added Successfully..."+RESET);
        }else{
            viewAdmin.updateStatus(RED, "Failed to Add Student!", RESET);
        }
    }

    public static void removeStudentOption() throws SQLException {
        studentDOA.displayAllDOA();
        int id = getInt();
        if(studentDOA.removeStudent(id)){
            viewAdmin.updateStatus(GREEN+"Student Removed Successfully..."+RESET);
        }else{
            viewAdmin.updateStatus(GREEN+"Invalid ID!"+RESET);
        }
    }

    public static void updateStudentOption() throws SQLException {
        int id = viewAdmin.getUpdateStudentId();
        if(!studentDOA.search(id)){
            viewAdmin.printStudentNotExist();
            return;
        }
        do {
            int op = viewAdmin.updateWhat();
            switch (op) {
                case 1:
                    String name = viewAdmin.getName();
                    if (studentDOA.updateStudent(id, "Stud_Name", name)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 2:
                    int age = viewAdmin.getAge();
                    if (studentDOA.updateStudent(id, "Stud_Age", age)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 3:
                    int hsc = viewAdmin.getHsc();
                    if (studentDOA.updateStudent(id, "Stud_HSC_Mark", hsc)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 4:
                    List<courses> coursesList = courseDOA.getAllCourses();
                    viewAdmin.printCourse(coursesList);
                    courses c = null;
                    while (true) {
                        int selectedId = viewAdmin.getCourseID();
                        c = courseDOA.getCourseById(selectedId);
                        if (c != null) {
                            viewAdmin.printSelectedCourse(c);
                            break;
                        } else {
                            viewAdmin.updateStatus(RED, "Invalid Course ID!!!", RED);
                        }
                    }
                    if (courseDOA.updateStudent(id, c)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 5:
                    String mail = verifyEmailWithOTP();
                    if (studentDOA.updateStudent(id, "Email", mail)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 6:
                    String gender = viewAdmin.getGender();
                    if (studentDOA.updateStudent(id, "Gender", gender)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                case 7:
                    int amount = viewAdmin.getPaidFee();
                    if (studentDOA.updateStudentAmount(id, "Amt_Paid", amount)) {
                        viewAdmin.studentUpdated();
                    } else {
                        viewAdmin.studentUpdateError();
                    }
                    break;
                default:
                    viewAdmin.invalid();
            }
        }while(viewAdmin.valid());
    }

    public static void feePending() throws SQLException {
        if(studentDOA.displayFeePendingStudents()){
            if(studentDOA.sendMailFeePending()){
                viewAdmin.updateStatus(GREEN+"Mails Sent..."+RESET);
            }else{
                viewAdmin.updateStatus(RED+"Error in sending mails or no mails available!"+RESET);
            }
        }
    }

    public static void searchOption() throws SQLException {
        int opt = viewAdmin.searchBy();
        switch (opt){
            case 1:
                int id = viewAdmin.getUpdateStudentId();
                if(studentDOA.search(id)) viewAdmin.updateStatus(GREEN+"Student Exist..."+RESET);
                else viewAdmin.updateStatus(RED+"Student Not Exist..."+RESET);
                break;
            case 2:
                String name = viewAdmin.getName();
                if(studentDOA.search(name)) viewAdmin.updateStatus(GREEN+"Student Exist..."+RESET);
                else viewAdmin.updateStatus(RED+"Student Not Exist..."+RESET);
                break;
            default :
                viewAdmin.updateStatus(RED+"Invalid option!!!"+RESET);
        }
    }

    public static void addTeacher() throws SQLException {
        int id = teacherDOA.getLastTeacherId()+1;
        String name = viewAdmin.getTeacherName();
        String email = verifyEmailWithOTP();
        if( teacherDOA.addTeacher(id,name,email)){
            viewAdmin.updateStatus(GREEN+"Staff Added..."+RESET);
        }else{
            viewAdmin.updateStatus(RED+"Error Adding Staff!!!"+RESET);
        }

    }
}
