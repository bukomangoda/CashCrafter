package com.user.transactions;

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

@WebServlet("/ServletTransactions")
public class ServletTransactions extends HttpServlet {
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

            if (isSavingAccount(con, userId)) {
                
                double amount = Double.parseDouble(request.getParameter("amount"));
                String transferType = request.getParameter("transerType");
                String accountNum = request.getParameter("account-number");

                if (transferType.equals("withdraw")) {
                    if (canWithdraw(con, userId, amount)) {
                        performWithdrawal(con, userId, userAccountNumber, amount);
                        request.setAttribute("statusCode", 200);
                        request.setAttribute("message", "Transaction successful!");
                    
    		            dispatcher = request.getRequestDispatcher("transaction-success.jsp");
                    } else {
                        request.setAttribute("message", "Transaction successful!");
    		            dispatcher = request.getRequestDispatcher("transaction-failed.jsp");
                    }
                }
                else if(transferType.equals("deposit")) {
                	if(isAccountNumberAvailable(con, accountNum)) {
                		performDeposit(con, userId, userAccountNumber, accountNum, amount);
                		response.getWriter().write("Transaction successful!");

    		            dispatcher = request.getRequestDispatcher("transaction-success.jsp");
                	}
                }
            } else {
                response.getWriter().write("You do not have a saving account.");
            }
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

    private boolean isSavingAccount(Connection con, String userId) {
        // Check if the user's account type is 'saving' in your database
        try (PreparedStatement stmt = con.prepareStatement("SELECT accountType FROM accounts WHERE user_id = ?")) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String accountType = rs.getString("accountType");
                    return "savings".equalsIgnoreCase(accountType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean canWithdraw(Connection con, String userId, double amount) {
        // Check if the withdrawal amount can be subtracted from the account balance
        try (PreparedStatement stmt = con.prepareStatement("SELECT balance FROM accounts WHERE user_id = ?")) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double accountBalance = rs.getDouble("balance");
                    return amount <= accountBalance;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void performWithdrawal(Connection con, String userId, String userAccountNumber, double amount) {
        try (PreparedStatement withdrawalStmt = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number = ?");
             PreparedStatement transactionStmt = con.prepareStatement("INSERT INTO transactions (user_id, account_number, transaction_type, transfer_from, date, amount) VALUES (?, ?, ?, ?, NOW(), ?)")) {
            
            withdrawalStmt.setDouble(1, amount);
            withdrawalStmt.setString(2, userAccountNumber);
            withdrawalStmt.executeUpdate();
            
            transactionStmt.setString(1, userId);
            transactionStmt.setString(2, userAccountNumber);
            transactionStmt.setString(3, "withdrawal");
            transactionStmt.setString(4, userAccountNumber);
            transactionStmt.setDouble(5, amount);
            transactionStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void performDeposit(Connection con, String userId, String userAccountNumber, String accountNum, double amount) {
        try (PreparedStatement depositStmt = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
             PreparedStatement transactionStmt = con.prepareStatement("INSERT INTO transactions (user_id, account_number, transaction_type, transfer_from, date, amount) VALUES (?, ?, ?, ?, NOW(), ?)")) {

            depositStmt.setDouble(1, amount);
            depositStmt.setString(2, accountNum);
            depositStmt.executeUpdate();
            
            transactionStmt.setString(1, userId);
            transactionStmt.setString(2, accountNum);
            transactionStmt.setString(3, "deposit");
            transactionStmt.setString(4, userAccountNumber);
            transactionStmt.setDouble(5, amount);
            transactionStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private boolean isAccountNumberAvailable(Connection con, String accountNum) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT account_number FROM accounts WHERE account_number = ?")) {
            stmt.setString(1, accountNum);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
