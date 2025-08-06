package view;
import model.courses;
import controller.DbConnection;
import java.sql.*;
import java.util.*;

public class viewAdmin{
    Scanner sc = new Scanner(System.in);
    public boolean getPass(){
        System.out.print("Enter your Password : ");
        String crtPass = "Admin123";
        String entPass = sc.nextLine();
        return crtPass.equals(entPass);
    }
    public int getOption(){
        System.out.print("1. Add Students\n2. Remove Students\n3. Update Student Data\n4. Display All Students\n5. Exit\nEnter Your Choice : ");
        int opt = sc.nextInt();
        while(opt<=0||opt>5){
            System.out.println("Invalid Option!!!\nTry Again");
            opt = sc.nextInt();
        }
        return opt;
    }
    public void showCourseList() throws SQLException {
        String qry = "SELECT * FROM courses";
        Connection con = DbConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(qry);
        System.out.println("\n---Courses Available---");
        System.out.printf("%-5s %-35s %-10s\n","S.no","Course Name","Fee");
        System.out.println("_____________________________________________________");
        while(rs.next()){
            System.out.printf("%-5d %-35s fee: %-10d\n",rs.getInt(1),rs.getString(2),rs.getInt(3));
        }
        System.out.println("_____________________________________________________");

    }
    public courses select() throws SQLException {
        String qry = "SELECT * FROM courses where CID = ?";
        Connection con = DbConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(qry);
        System.out.print("Select By, course ID : ");
        int id = sc.nextInt();
        while(id<=0 || id>5){
            System.out.println("Invalid Selection");
            System.out.print("Select By, course ID : ");
            id = sc.nextInt();
        }
        String cname = "";
        int fee = 0;
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            id = rs.getInt(1);
            cname = rs.getString(2);
            fee = rs.getInt(3);
            System.out.println("Selected course : "+rs.getString(2));
            return new courses(id, cname, fee);
        } else {
            System.out.println("Course with ID " + id + " does not exist.");
            return null;
        }
    }
    public String getName(){
        System.out.print("Enter the Student Name : ");
        return sc.nextLine();
    }
    public int getAge(){
        System.out.print("Enter the Student Age : ");
        return sc.nextInt();
    }
    public int getHsc(){
        System.out.print("Enter Student's HSC Mark (For 600) : ");
        return sc.nextInt();
    }
    public int getPaidFee(){
        System.out.println("Enter the Amount Paid : ");
        return sc.nextInt();
    }
    public boolean valid(){
        System.out.print("Do you want to change Anything ? y/n : ");
        sc.nextLine();
        String yesNo = sc.nextLine().toLowerCase();
        return yesNo.equals("y");
    }
    public int validWhat(){
        System.out.println("1. Name\n2. Age\n3. HSC Mark\n4. Course");
        System.out.println("What do u want to Change? : ");
        return sc.nextInt();
    }
    public void invalid(){
        System.out.println("Invalid Option!!!");
    }
    public int getLastCourseId() throws SQLException {
        String qry = "SELECT Stud_ID FROM Students ORDER BY Stud_ID DESC LIMIT 1";
        Connection con = DbConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(qry);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return -1;
        }
    }
    public void updateTable(int Stud_ID,String Stud_Name,int Stud_Age,int Stud_HSC_Mark,int Course_ID,String Course_Name,int Amt_Paid,int Amt_Balance,int Total_fee) throws SQLException {
        String qry = "insert into students values(?,?,?,?,?,?,?,?,?)";
        Connection con = DbConnection.getConnection();
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement(qry);
        pst.setInt(1,Stud_ID);
        pst.setString(2,Stud_Name);
        pst.setInt(3,Stud_Age);
        pst.setInt(4,Stud_HSC_Mark);
        pst.setInt(5,Course_ID);
        pst.setString(6,Course_Name);
        pst.setInt(7,Amt_Paid);
        pst.setInt(8,Amt_Balance);
        pst.setInt(9,Total_fee);
        int rs = pst.executeUpdate();
        if(rs!=1){
            System.out.println("Error Occurred");
            con.rollback();
        }else{
            System.out.println("Student Added Successfully");
        }
        con.commit();
    }
    public int getID(){
        System.out.print("Enter the Student ID : ");
        return sc.nextInt();
    }
    public void displayAll(int n) throws SQLException {
        String qry = "SELECT * FROM students";
        Connection con = DbConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(qry);
        System.out.println("\nStudent List");
        int f =0;
        while(rs.next()){
            f=1;
            System.out.printf("%-2d %-20s %-2d\n",rs.getInt(1),rs.getString(2),rs.getInt(3));

        }
        if(f==0){
            System.out.println("---No Student Available---");
        }else{
            if(n==1) {
                int id = getID();
                removeStudent(id);
            }
        }
        System.out.println();
    }
    public void removeStudent(int id) throws SQLException {
        String qry = "DELETE FROM students where Stud_ID = "+id;
        Connection con = DbConnection.getConnection();
        con.setAutoCommit(false);
        Statement st = con.createStatement();
        int row = st.executeUpdate(qry);
        if(row != 0) {
            System.out.println("Student Removed From the DB!!!");
            con.commit();
            return;
        }
        con.rollback();
    }
}
