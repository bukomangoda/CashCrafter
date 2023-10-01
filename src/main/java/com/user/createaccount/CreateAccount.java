package com.user.createaccount;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        Map<String, String> userInfo = getUserInfoFromCookies(request);
        
        String userEmail = userInfo.get("email");

        // Retrieve form parameters
        String accountType = request.getParameter("accountType");
        double depositAmount = Double.parseDouble(request.getParameter("deposit-amount"));

        if (userEmail != null) {
            Connection con = null;
		    RequestDispatcher dispatcher = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");

                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root", "");

                int userId = getUserIdByEmail(con, userEmail);

                if (userId > 0) {
                    try {
                        int accountNumber = generateUniqueAccountNumber();

                        // Insert a new record into the accounts table
                        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO accounts (user_id, accountType, balance, account_number, date) VALUES  (?, ?, ?, ?, NOW())");
                        preparedStatement.setInt(1, userId);
                        preparedStatement.setString(2, accountType);
                        preparedStatement.setDouble(3, depositAmount);
                        preparedStatement.setInt(4, accountNumber);
                        preparedStatement.executeUpdate();
                        
                        Cookie accCookie = new Cookie("accountNumber",String.valueOf(accountNumber));
                        accCookie.setMaxAge(30 * 60);
                        response.addCookie(accCookie);
                        
    					Cookie accountTypeCookie = new Cookie("accountType", accountType);
    					accountTypeCookie.setMaxAge(30 * 60);
    					response.addCookie(accountTypeCookie);

    		            dispatcher = request.getRequestDispatcher("balance-inquiry.jsp");
    		            
    		         // Check if the account type is "fd"
    		         if (accountType.equalsIgnoreCase("fd")) {
    		             String insertQuery = "INSERT INTO fixed_deposits (user_id, date, maturity_date, interest_rate, amount, account_number) VALUES (?, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), 5.5, ?, ?)";

    		             try {
    		                 PreparedStatement fdStatement = con.prepareStatement(insertQuery);

    		                 fdStatement.setInt(1, userId);
    		                 fdStatement.setDouble(2, depositAmount);
    		                 fdStatement.setString(3, String.valueOf(accountNumber));
    		                 fdStatement.executeUpdate();
    		                 fdStatement.close();
    		             } catch (SQLException e) {
    		                 e.printStackTrace();
    		             }
    		         }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
		            dispatcher = request.getRequestDispatcher("login.jsp");
                }
		        dispatcher.forward(request, response);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            PrintWriter out = response.getWriter();
            out.println("User email not found in cookies. Please login.");
        }
    }
    
    private Map<String, String> getUserInfoFromCookies(HttpServletRequest request) {
        Map<String, String> userInfo = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("email")) {
                userInfo.put("email", cookie.getValue());
            }
        }
        return userInfo;
    }

    // Helper method to retrieve user ID by email from the users table
    private int getUserIdByEmail(Connection con, String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        return -1;
    }

    private int generateUniqueAccountNumber() {
        int min = 10000000;
        int max = 99999999;
        return min + (int) (Math.random() * ((max - min) + 1));
    }

}