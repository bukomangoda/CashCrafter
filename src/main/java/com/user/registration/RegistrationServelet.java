package com.user.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrationServelet
 */
@WebServlet("/register")
public class RegistrationServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		    throws ServletException, IOException {
		    String username = request.getParameter("name");
		    String email = request.getParameter("email");
		    String password = request.getParameter("pass");

		    RequestDispatcher dispatcher = null;
		    Connection con = null;

		    try {
		        Class.forName("com.mysql.cj.jdbc.Driver");
		        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_management_db", "root", "");
		        
		        PreparedStatement checkUsernameStmt = con.prepareStatement("SELECT * FROM users WHERE username = ?");
		        checkUsernameStmt.setString(1, username);
		        ResultSet usernameResult = checkUsernameStmt.executeQuery();

		        PreparedStatement checkEmailStmt = con.prepareStatement("SELECT * FROM users WHERE email = ?");
		        checkEmailStmt.setString(1, email);
		        ResultSet emailResult = checkEmailStmt.executeQuery();


		        if (usernameResult.next() || emailResult.next()) {
		            request.setAttribute("status", "fail");
		            request.setAttribute("errorMessage", "Registration failed. Please try again.");
		            dispatcher = request.getRequestDispatcher("registration.jsp");
		        } else {
		            PreparedStatement pst = con.prepareStatement("INSERT INTO users(username, password, email) VALUES(?,?,?)");
		            pst.setString(1, username);
		            pst.setString(2, password);
		            pst.setString(3, email);

		            int rowCount = pst.executeUpdate();
		            if (rowCount > 0) {
		                request.setAttribute("status", "success");
		                dispatcher = request.getRequestDispatcher("login.jsp");
		            } else {
		                request.setAttribute("status", "fail");
		                request.setAttribute("errorMessage", "Registration failed. Please try again.");
			            dispatcher = request.getRequestDispatcher("registration.jsp");
		            }
		        }
		        dispatcher.forward(request, response);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
}