package DOA;

import model.courses;

import java.sql.*;
import java.util.*;

public class courseDOA {
    public static List<courses> getAllCourses() throws SQLException {
        String qry = "SELECT * FROM courses";
        List<courses> list = new ArrayList<>();
        try (Connection con = DbConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(qry)) {
            while (rs.next()) {
                list.add(new courses(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }
        return list;
    }

    public static courses getCourseById(int courseId) throws SQLException {
        String qry = "SELECT * FROM courses WHERE CID = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(qry)) {
            pst.setInt(1, courseId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new courses(rs.getInt(1), rs.getString(2), rs.getInt(3));
                }
            }
        }
        return null;
    }
    public static boolean updateStudent(int id,courses c){
        String qry = "Update students set Course_ID = ? ,Course_Name = ? ,Tot_Fee = ? where Stud_ID = ?";
        try(Connection con = DbConnection.getConnection();PreparedStatement pst = con.prepareStatement(qry)){
            pst.setInt(1,c.getId());
            pst.setString(2,c.getCourseName());
            pst.setInt(3,c.getFee());
            pst.setInt(4,id);
            int rs = pst.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
