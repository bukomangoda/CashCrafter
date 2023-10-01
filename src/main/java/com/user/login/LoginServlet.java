package com.user.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		RequestDispatcher dispatcher = null;
		Connection con = null;
		String accountType = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root",
					"");

			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
			stmt.setString(1, email);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			// Add all the necessary cookies before forwarding
			if (rs.next()) {
			    int userId = rs.getInt("id");
			    String userIdStr = Integer.toString(userId);

			    if (doesAccountExistForUserId(con, userId)) {
			        String accNum = getAccountNumberByUserId(con, userId);
			        accountType = getAccountTypeByUserId(con, userId);
			        Cookie accCookie = new Cookie("accountNumber", accNum);
			        Cookie accountTypeCookie = new Cookie("accountType", accountType);
			        accCookie.setMaxAge(30 * 60);
			        accountTypeCookie.setMaxAge(30 * 60);

			        response.addCookie(accCookie);
			        response.addCookie(accountTypeCookie);
			    }

			    // Authentication successful
			    String username = rs.getString("username");
			    Cookie loginCookie = new Cookie("email", email);
			    Cookie usernameCookie = new Cookie("username", username);
			    Cookie usernameId = new Cookie("userId", userIdStr);
			    loginCookie.setMaxAge(30 * 60);
			    usernameCookie.setMaxAge(30 * 60);
			    usernameId.setMaxAge(30 * 60);
			    response.addCookie(loginCookie);
			    response.addCookie(usernameCookie);
			    response.addCookie(usernameId);

			    // Fetch the accountType based on userId
			    if (accountType.equals("savings")) {
			        dispatcher = request.getRequestDispatcher("balance-inquiry.jsp");
			    } else if (accountType.equals("loans")) {
			        dispatcher = request.getRequestDispatcher("loan-payment.jsp");
			    } else if (accountType.equalsIgnoreCase("fd")) {
			        dispatcher = request.getRequestDispatcher("fixed-deposit.jsp");
			    } else if (accountType.equals("")) {
			        dispatcher = request.getRequestDispatcher("account-creation.jsp");
			    }
			    
	            Thread.sleep(2000);

			} else {
			    request.setAttribute("errorMessage", "Invalid email or password");
			    dispatcher = request.getRequestDispatcher("login.jsp");
			}

			// Forward after adding cookies
			dispatcher.forward(request, response);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			dispatcher = request.getRequestDispatcher("error.jsp");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dispatcher.forward(request, response);
	}

	// Helper method to fetch accountType by userId
	private String getAccountTypeByUserId(Connection con, int userId) throws SQLException {
		String accountType = null;
		String sql = "SELECT accountType FROM accounts WHERE user_id = ?";
		try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				accountType = resultSet.getString("accountType");
			}
		}
		return accountType;
	}

	// Helper method to fetch account_number by userId
	private String getAccountNumberByUserId(Connection con, int userId) throws SQLException {
		String accountNumber = null;
		String sql = "SELECT account_number FROM accounts WHERE user_id = ?";
		try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				accountNumber = resultSet.getString("account_number");
			}
		}
		return accountNumber;
	}

	// Helper method to check if an account exists for a given userId
	private boolean doesAccountExistForUserId(Connection con, int userId) {
	    String sql = "SELECT COUNT(*) AS count FROM accounts WHERE user_id = ?";
	    
	    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	        preparedStatement.setInt(1, userId);
	        
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt("count");
	                return count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


}
