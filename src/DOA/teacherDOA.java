package DOA;

import model.courses;
import view.viewTeacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Util.colors.*;

public class teacherDOA {
    public static int getLastTeacherId() throws SQLException {
        String qry = "SELECT MAX(Teach_ID) FROM teacherLogin";
        try(Connection con = DbConnection.getConnection(); PreparedStatement pst = con.prepareStatement(qry); ResultSet rs = pst.executeQuery();
        ){
            if (rs.next()) {
                int id = rs.getInt(1);
                if(id==0) return 1000;
                else return id;
            } else {
                return 1000;
            }
        }
    }
    public static boolean addTeacher(int id, String name, String email){
        String qry = "INSERT INTO teacherLogin (Teach_ID, Teach_Name, Mail, Teach_Pass) VALUES (?,?,?,?)";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            String pass = "1234";
            pst.setInt(1,id);
            pst.setString(2,name);
            pst.setString(3,email);
            pst.setString(4,pass);
            int r = pst.executeUpdate();return r>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getPass(int id){
        String qry = "select Teach_Pass from teacherLogin where Teach_ID = ?";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            pst.setInt(1,id);
            ResultSet r = pst.executeQuery();
            if(r.next()){
                return r.getString(1);
            }else{
                return "";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean updatePass(int id ,String pass){
        String qry = "update teacherLogin set Teach_Pass = ? where Teach_ID = ?";
        try(Connection con = DbConnection.getConnection()){
            PreparedStatement pst = con.prepareStatement(qry);
            pst.setString(1,pass);
            pst.setInt(2,id);
            int r = pst.executeUpdate();
            return r>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createDate(String date){
        try (Connection conn = DbConnection.getConnection()) {
            if (!doesColumnExist(conn, "attendance", "D"+date)) {
                addColumn(conn, "attendance", date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean doesColumnExist(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, columnName)) {
            return rs.next();
        }
    }
    private static void addColumn(Connection conn, String tableName, String columnName) throws SQLException {
        columnName = "D"+columnName;
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN `" + columnName + "` VARCHAR(20)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Column added: " + columnName);
        }
    }
    public static List<courses> displayCourses() throws SQLException {
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

    public static void createSub(String subs){
        try (Connection conn = DbConnection.getConnection()) {
            if (!doesColumnExist(conn, "Marks",subs )) {
                addColumn2(conn, "Marks", subs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void addColumn2(Connection conn, String tableName, String columnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN `" + columnName + "` int";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Column added: " + columnName);
        }
    }
    public static void updateAttendance(int courseId, String dateColumn) {
        if (!dateColumn.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Invalid column name: " + dateColumn);
        }
        dateColumn = "D"+dateColumn;
        String selectQry = "SELECT Stud_ID, Stud_Name FROM attendance WHERE CID = ?";
        String updateQry = "UPDATE attendance SET " + dateColumn + " = ? WHERE Stud_ID = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(selectQry);
             PreparedStatement updatePst = con.prepareStatement(updateQry)) {

            pst.setInt(1, courseId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int currentStud = rs.getInt("Stud_ID");
                String name = rs.getString("Stud_Name");
                String status;

                while (true) {
                    int n = viewTeacher.currentStudentA(name);
                    if (n == 1) {
                        status = "Present";
                        break;
                    } else if (n == 0) {
                        status = "Absent";
                        break;
                    } else {
                        viewTeacher.updateStatus(RED, "Not Valid, Try Again\n", RESET);
                    }
                }

                updatePst.setString(1, status);
                updatePst.setInt(2, currentStud);
                updatePst.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateMarks(int courseId, String subCol) {
        String selectQry = "SELECT Stud_ID, Stud_Name FROM Marks WHERE CID = ?";
        String updateQry = "UPDATE Marks SET " + subCol + " = ? WHERE Stud_ID = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(selectQry);
             PreparedStatement updatePst = con.prepareStatement(updateQry)) {

            pst.setInt(1, courseId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int currentStud = rs.getInt("Stud_ID");
                String name = rs.getString("Stud_Name");
                int mark = viewTeacher.currentStudentM(name);
                updatePst.setInt(1, mark);
                updatePst.setInt(2, currentStud);
                updatePst.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayMarks(int id) {
        String query = "SELECT * FROM Marks where CID = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<String[]> rows = new ArrayList<>();
            String[] headers = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                headers[i - 1] = rsmd.getColumnName(i);
            }
            rows.add(headers);
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i) == null ? "NULL" : rs.getString(i);
                }
                rows.add(row);
            }
            int[] colWidths = new int[columnCount];
            for (String[] row : rows) {
                for (int i = 0; i < columnCount; i++) {
                    colWidths[i] = Math.max(colWidths[i], row[i].length());
                }
            }
            StringBuilder border = new StringBuilder("+");
            for (int w : colWidths) {
                border.append("-".repeat(w + 2)).append("+");
            }
            System.out.println(border);
            viewTeacher.printRow(headers, colWidths);
            System.out.println(border);
            for (int i = 1; i < rows.size(); i++) {
                viewTeacher.printRow(rows.get(i), colWidths);
            }
            System.out.println(border);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
