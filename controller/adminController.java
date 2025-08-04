package controller;
import model.*;
import view.viewAdmin;

import java.sql.SQLException;

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
        else return 0;
    }
    public static void addStudent() throws SQLException {
        viewAdmin view = new viewAdmin();
        String name = view.getName();
        int age = view.getAge();
        int hsc = view.getHsc();
        view.showCourseList();
        courses c = view.select();
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
        view.updateTable(Stud_ID,name,age,hsc,c.getId(),c.getCourseName(),amtPaid,bal,c.getFee());
    }

    public static void removeStudents() throws SQLException {
        viewAdmin view = new viewAdmin();
        view.displayAll();
        int id = view.getID();
        view.removeStudent(id);
    }
}
