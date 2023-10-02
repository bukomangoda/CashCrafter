package com.user.loanpayment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class LoanPaymentServlet
 */
@WebServlet("/LoanPaymentServlet")
public class LoanPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
        Map<String, String> userInfo = getUserInfoFromCookies(request);
        
        String userId = userInfo.get("userId");
        String userAccountNumber = userInfo.get("accountNumber");
        
        Connection con = null;
	    RequestDispatcher dispatcher = null;
	    
	       try {
	            Class.forName("com.mysql.jdbc.Driver");

	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root", "");
                double amount = Double.parseDouble(request.getParameter("amount"));
                
                performLoanPayment(con, userId, userAccountNumber, amount);
	            dispatcher = request.getRequestDispatcher("balance-inquiry.jsp");
		        dispatcher.forward(request, response);
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
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
    
    private void performLoanPayment(Connection con, String userId, String userAccountNumber, double amount) {
        try (PreparedStatement depositStmt = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number = ?");
             PreparedStatement transactionStmt = con.prepareStatement("INSERT INTO transactions (user_id, account_number, transaction_type, transfer_from, date, amount) VALUES (?, ?, ?, ?, NOW(), ?)")) {

            depositStmt.setDouble(1, amount);
            depositStmt.setString(2, userAccountNumber);
            depositStmt.executeUpdate();
            
            transactionStmt.setString(1, userId);
            transactionStmt.setString(2, userAccountNumber);
            transactionStmt.setString(3, "loan-repayment");
            transactionStmt.setString(4, userAccountNumber);
            transactionStmt.setDouble(5, amount);
            transactionStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
