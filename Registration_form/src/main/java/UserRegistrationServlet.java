import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/UserRegistrationServlet")
public class UserRegistrationServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String otherName = request.getParameter("other_name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String phoneNumber = request.getParameter("phone_number");
		String password = request.getParameter("password");
		String comment = request.getParameter("comment");
		String passwordHash = hashPassword(password);
		String[] strInput = { firstName, lastName, otherName, email, address, phoneNumber, password, comment };

		if (allInputPassed(strInput, response)) {
			try {
				Class.forName("org.postgresql.Driver");
				String url = "jdbc:postgresql://localhost:5432/students_record";
				String user = "postgres";
				String dbPassword = "996374";

				Connection conn = DriverManager.getConnection(url, user, dbPassword);
				System.out.println("Connected to the database.");
				String sql = "INSERT INTO students (first_name, last_name, other_name, email, address, phone_number, password, comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, otherName);
				ps.setString(4, email);
				ps.setString(5, address);
				ps.setString(6, phoneNumber);
				ps.setString(7, passwordHash);
				ps.setString(8, comment);
				ps.executeUpdate();
				conn.close();
				response.sendRedirect("success.jsp");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				response.getWriter().println("An error occurred: " + e.getMessage());

			} catch (SQLException e) {
				e.printStackTrace();
				response.getWriter().println("An error occurred: " + e.getMessage());

			}

		} else {
			response.getWriter().println("An error occurred: Suspected malicious input detected");

		}
	}

	public static String hashPassword(String password) {
		try {
			// Create a MessageDigest instance with the SHA-256 algorithm (or use a
			// different one if preferred)
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Hash the password
			byte[] hashedPassword = md.digest(password.getBytes());

			// Convert the byte array to a hexadecimal string
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedPassword) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isValidSql(String inputText) {
		String[] sqlKeywords = { "UPDATE", "SELECT", "DELETE", "DROP", "TRUNCATE", "INSERT", "ALTER", "CREATE", "GRANT",
				"EXEC", "EXECUTE", "MERGE" };
		// You can add more SQL keywords as needed

		// Check for the presence of SQL keywords but not in a complete SQL statement
		for (String keyword : sqlKeywords) {
			Pattern pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputText);

			if (matcher.find()) {
				// SQL keyword found, check if it's part of a SQL statement
				if (isPotentialSQLStatement(inputText, matcher.start())) {
					return true; // Potential complete SQL statement detected, input is unsafe
				}
			}
		}

		return false; // No potential complete SQL statement found
	}

	// Check if the matched SQL keyword represents a potential complete SQL
	// statement
	private static boolean isPotentialSQLStatement(String inputText, int index) {
		// Simplified check for potential SQL statement structure based on common SQL
		// clauses
		// Adjust this method based on more accurate indicators in your specific case
		String potentialStatement = inputText.substring(index);
		String[] commonClauses = { "SELECT", "FROM", "WHERE", "GROUP BY", "ORDER BY", "LIMIT", "UPDATE", "DELETE",
				"SET", "INSERT INTO" };

		for (String clause : commonClauses) {
			if (potentialStatement.contains(clause)) {
				return true; // Contains a common SQL clause, might be a potential complete SQL statement
			}
		}

		return false;
	}

	public static boolean isValidJavaScriptStatement(String input) {
		// Complex check for common JavaScript statement structure
		// This example uses a more sophisticated approach

		// Regular expressions to match different JavaScript syntax constructs
		String commonJSOperators = "[\\+\\-\\*/%&\\|\\^!~<>=]";
		String commonJSKeywords = "\\b(let|var|const|function|if|else|while|for|do|switch|case|break|continue|return)\\b";
		String commonJSBuiltInObjects = "\\b(Math|Date|Array|String|Object|Number)\\b";

		// Check for common JavaScript constructs
		if (input.matches(".*(" + commonJSOperators + "|" + commonJSKeywords + "|" + commonJSBuiltInObjects + ").*")) {
			return true;
		}

		// Advanced checks for more complex syntax structures
		// For example, checking for function definitions or variable assignments
		if (input.matches(".*\\bfunction\\s+\\w+\\s*\\(.*\\)\\s*\\{.*\\}.*")
				|| input.matches(".*\\b\\w+\\s*=\\s*.*;")) {
			return true;
		}

		return false;
	}

	public static boolean isValidPHPStatement(String input) {
		// Complex check for common PHP statement structure

		// Regular expressions to match different PHP syntax constructs
		String commonPHPOperators = "[\\+\\-\\*/%&\\|\\^!~<>=]";
		String commonPHPKeywords = "\\b(abstract|class|function|if|else|while|for|do|switch|case|break|continue|return|echo)\\b";
		String commonPHPBuiltInFunctions = "\\b(strlen|count|empty|json_encode|preg_match)\\b";

		// Check for common PHP constructs
		if (input.matches(
				".*(" + commonPHPOperators + "|" + commonPHPKeywords + "|" + commonPHPBuiltInFunctions + ").*")) {
			return true;
		}

		// Advanced checks for more complex syntax structures
		// For example, checking for function definitions or variable assignments
		if (input.matches(".*\\bfunction\\s+\\w+\\s*\\(.*\\)\\s*\\{.*\\}.*")
				|| input.matches(".*\\$\\w+\\s*=\\s*.*;")) {
			return true;
		}

		return false;
	}

	public static boolean isValidPythonScript(String input) {
		// Complex check for common Python script structure

		// Regular expressions to match different Python script constructs
		String commonPythonOperators = "[\\+\\-\\*/%&\\|\\^!~<>=]";
		String commonPythonKeywords = "\\b(if|else|while|for|def|return|import|from|class)\\b";
		String commonPythonBuiltInFunctions = "\\b(print|len|range|zip|enumerate)\\b";

		// Check for common Python constructs
		if (input.matches(".*(" + commonPythonOperators + "|" + commonPythonKeywords + "|"
				+ commonPythonBuiltInFunctions + ").*")) {
			return true;
		}

		// Advanced checks for more complex syntax structures
		// For example, checking for function definitions or variable assignments
		if (input.matches(".*\\bdef\\s+\\w+\\s*\\(.*\\)\\s*:\\s*.*") || input.matches(".*\\w+\\s*=\\s*.*")) {
			return true;
		}

		return false;
	}

	public static boolean allInputPassed(String[] inputs, HttpServletResponse response) throws IOException {
		for (int i = 0; i < inputs.length; i++) {
			System.out.println("check: " + inputs[i]);
			if (isValidSql(inputs[i]) == true) {
				System.out.println("SQL detected");
				return false;
			} else if (isValidJavaScriptStatement(inputs[i]) == true) {
				System.out.println("JavaScript detected");
				return false;
			} else if (isValidPHPStatement(inputs[i]) == true) {
				System.out.println("PHP detected");
				return false;
			} else if (isValidPythonScript(inputs[i]) == true) {
				System.out.println("Python detected");
				return false;
			}
		}
		System.out.println("check: passed");

		return true;
	}

}
