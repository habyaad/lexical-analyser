import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

@SuppressWarnings("serial")
@WebServlet("/StudentRecordsServlet")
public class StudentRecordsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<StudentRecord> studentRecords = retrieveStudentRecordsFromDatabase();
        request.setAttribute("studentRecords", studentRecords);
        request.getRequestDispatcher("StudentRecords.jsp").forward(request, response);
    }

    private List<StudentRecord> retrieveStudentRecordsFromDatabase() {
        List<StudentRecord> studentRecords = new ArrayList<>();
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/students_record";
            String user = "postgres";
            String dbPassword = "996374";

            conn = DriverManager.getConnection(url, user, dbPassword);

            String sql = "SELECT * FROM students";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentRecord studentRecord = new StudentRecord(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("other_name"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getString("comment")
                );
                studentRecords.add(studentRecord);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return studentRecords;
    }
}
