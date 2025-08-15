package controller;
import DOA.*;
import model.courses;
import view.viewAdmin;
import view.viewTeacher;
import static Util.colors.*;
import java.sql.SQLException;
import java.util.List;

public class teacherController {
    public static boolean verify(){
        int id = viewTeacher.getTeacherID();
        String pass = viewTeacher.getTeacherPass();
        return validateTeacher(id,pass);
    }
    public static boolean validateTeacher(int id,String pass){
        return teacherDOA.getPass(id).equals(pass);
    }
    public static int displayOptions() throws SQLException {
        int option = viewTeacher.getOption();
        if(option==1) { attendanceOption(); return 1;}
        else if(option==2){ enterMarkOption(); return 1;}
        else if(option==3){ updatePassOption() ;return 1;}
        else if(option==4){ displayMarksOption() ;return 1;}
        else return 0;
    }

    private static void displayMarksOption() throws SQLException {
        List<courses> coursesList = teacherDOA.displayCourses();
        viewTeacher.printCourse(coursesList);
        int Cid = viewTeacher.getCID();
        teacherDOA.displayMarks(Cid);
    }

    private static void enterMarkOption() throws SQLException {
        List<courses> coursesList = teacherDOA.displayCourses();
        viewTeacher.printCourse(coursesList);
        int Cid = viewTeacher.getCID();
        String subject = viewTeacher.getSubjects();
        teacherDOA.createSub(subject);
        teacherDOA.updateMarks(Cid,subject);

    }

    public static void updatePassOption(){
        int id = viewTeacher.getTeacherID();
        String newPass = viewTeacher.getNewPass();
        if(teacherDOA.updatePass(id,newPass)){
            viewTeacher.updateStatus(GREEN,"✅Teacher Password Updated",RESET);
        }else{
            viewTeacher.updateStatus(RED,"❌Error!!!",RESET);
        }
    }
    public static void attendanceOption() throws SQLException {
        List<courses> coursesList = teacherDOA.displayCourses();
        viewTeacher.printCourse(coursesList);
        int id = viewTeacher.getCID();
        String date = viewTeacher.getDate();
        teacherDOA.createDate(date);
        viewTeacher.updateStatus(GREEN,"1 - Present ",RESET);
        viewTeacher.updateStatus(RED,"0 - Absent\n",RESET);
        teacherDOA.updateAttendance(id,date);
        viewTeacher.updateStatus(GREEN,"Attendance Updated....",RESET);
    }

}
