package com.user.balance;

import java.io.IOException;
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

/**
 * Servlet implementation class BalanceInquiryServlet
 */
@WebServlet("/BalanceInquiryServlet")
public class BalanceInquiryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> userInfo = getUserInfoFromCookies(request);
        String userAccountNumber = userInfo.get("accountNumber");
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root", "");

            if (userAccountNumber != null) {
                double balance = getBalance(con, userAccountNumber);
                String balanceStr = String.valueOf(balance);
                response.setContentType("text/plain");
                response.getWriter().write(balanceStr);
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("Account number not found in cookies.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("An error occurred while processing your request.");
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to retrieve balance based on account number
    private double getBalance(Connection con, String accountNumber) {
    	
        double balance = 0.0;
        try (PreparedStatement statement = con.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?")) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
    
    private Map<String, String> getUserInfoFromCookies(HttpServletRequest request) {
        Map<String, String> userInfo = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userId")) {
                userInfo.put("userId", cookie.getValue());
            } else if (cookie.getName().equals("accountNumber")) {
                userInfo.put("accountNumber", cookie.getValue());
            }
        }
        return userInfo;
    }
}
