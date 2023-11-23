<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Records</title>
    <link rel="stylesheet" href="css/Records.css">
</head>
<body>
    <nav class="navbar">
        <ul>
            <li><a href="Index.jsp"> Registration</a></li>
            <li><a href="StudentRecords.jsp">View Records</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Student Records</h1>
        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Other Name</th>
                <th>Email</th>
                <th>Address</th>
                <th>Phone Number</th>
                <th>Comment</th>
            </tr>
            <% 
                // JDBC code to fetch data from the database and display it in the table
                try {
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://localhost:5432/students_record";
                    String user = "postgres";
                    String dbPassword = "996374"; // Replace with your database password

                    Connection conn = DriverManager.getConnection(url, user, dbPassword);
                    String sql = "SELECT * FROM students";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("first_name") %></td>
                <td><%= rs.getString("last_name") %></td>
                <td><%= rs.getString("other_name") %></td>
                <td><%= rs.getString("email") %></td>
                <td><%= rs.getString("address") %></td>
                <td><%= rs.getString("phone_number") %></td>
                <td><%= rs.getString("comment") %></td>
            </tr>
            <%
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
        </table>
    </div>
</body>
</html>
