<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/Style.css">
    <title>Student Registration</title>
</head>
<body>
    <nav class="navbar">
        <ul>
            <li><a href="Index.jsp">Student Registration</a></li>
            <li><a href="StudentRecords.jsp">View Table</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Student Registration</h1>
        <form action="UserRegistrationServlet" method="post">
            <div class="form-group">
                <div class="name-group">
                    <div class="name-field pad">
                        <label for="first_name">First Name:</label>
                        <input type="text" id="first_name" name="first_name" required>
                    </div>
                    <div class="name-field">
                        <label for="last_name">Last Name:</label>
                        <input type="text" id="last_name" name="last_name" required>
                    </div>
                </div>
                <label for="other_name">Other Name:</label>
                <input type="text" id="other_name" name="other_name">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address">
            </div>
            <div class="form-group">
                <label for="phone_number">Phone Number:</label>
                <input type="text" id="phone_number" name="phone_number" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="comment">Comment:</label>
                <textarea id="comment" name="comment" rows="4"></textarea>
            </div>
            <button type="submit">Submit</button>
        </form>
    </div>
</body>
</html>
