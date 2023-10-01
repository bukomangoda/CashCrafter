package com.user.fixdeposit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
 * Servlet implementation class FixDepositServlet
 */
@WebServlet("/FixDepositServlet")
public class FixDepositServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    Map<String, String> userInfo = getUserInfoFromCookies(request);
	    String userId = userInfo.get("userId");
	    String userAccountNumber = userInfo.get("accountNumber");

	    Connection con = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    RequestDispatcher dispatcher = null;

	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root", "");

	        // SQL query to retrieve the maturity date and interest rate
	        String sql = "SELECT maturity_date, interest_rate FROM fixed_deposits WHERE user_id = ? AND account_number = ?";
	        preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, userId);
	        preparedStatement.setString(2, userAccountNumber);
	        resultSet = preparedStatement.executeQuery();
	        
            String balanceSql = "SELECT balance FROM accounts WHERE user_id = ? AND account_number = ?";
            PreparedStatement balanceStatement = con.prepareStatement(balanceSql);
            balanceStatement.setString(1, userId);
            balanceStatement.setString(2, userAccountNumber);
            ResultSet initialdeposit = balanceStatement.executeQuery();
	        
	        if (resultSet.next()) {	            
	            String maturityDateStr = resultSet.getString("maturity_date");
	            double interestRate = resultSet.getDouble("interest_rate");

	            java.sql.Date maturityDate = java.sql.Date.valueOf(maturityDateStr);
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            String formattedMaturityDate = dateFormat.format(maturityDate);

                request.setAttribute("maturity-date", formattedMaturityDate);
                dispatcher = request.getRequestDispatcher("fixed-deposit.jsp");
    	        
	            // Calculate the number of days until maturity
	            long daysUntilMaturity = (maturityDate.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);
	            double interestAmount = 0.00;
	            if (initialdeposit.next()) {
	                double initialDeposit = initialdeposit.getDouble("balance");
	                interestAmount = (initialDeposit * interestRate * daysUntilMaturity) / 36500.00;
	                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
	                String formattedInterest = decimalFormat.format(interestAmount);

	                request.setAttribute("interest", formattedInterest);
	                dispatcher.forward(request, response);
	            } else {
	                response.getWriter().write("No account found for the user and account number.");
	            }

                request.setAttribute("interest", interestAmount);

    	        dispatcher.forward(request, response);

	        } else {
	            response.getWriter().write("No fixed deposit found for the user and account number.");
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	        response.getWriter().write("An error occurred while processing your request.");
	    } finally {
	        if (resultSet != null) {
	            try {
	                resultSet.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (preparedStatement != null) {
	            try {
	                preparedStatement.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
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

}
