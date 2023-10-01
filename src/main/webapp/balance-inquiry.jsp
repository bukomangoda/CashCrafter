<%@ page language="java" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    
    <title>Balance Inquiry</title>
    
    <!-- Include jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Include Bootstrap JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Font Icon -->
    <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">
        
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Main CSS -->
    <link rel="stylesheet" href="css/style.css">    
</head>

<body>
<%
    HttpServletRequest newRequest = (HttpServletRequest) pageContext.getRequest();

    Cookie[] cookies = newRequest.getCookies();

    String usernameCookieName = "username";
    String accountNumberCookieName = "accountNumber";

    String usernameCookieValue = null;
    String cookieValue = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                usernameCookieValue = cookie.getValue();
            }
            if (cookie.getName().equals(accountNumberCookieName)) {
                cookieValue = cookie.getValue();
            }
        }
    }

    if (usernameCookieValue == null) {
    	 response.sendRedirect("login.jsp");
    }
    
%>
        
<jsp:include page="header.jsp" />
<div class="main" style="background: #8ad0da69">
    <!-- Account form -->
    <section class="signup">
        <div class="container" style="margin-top: 10%; background: #efe5b14a;">
            <div class="signup-content">
                <div class="signup-form">
                    <h2 class="form-title" style="text-align: center;">Balance Inquiry</h2>
                    <form method="get" class="register-form" action="BalanceInquiryServlet" id="register-form" style="padding-top: 10%; text-align: center;">
                        <div class="form-group">
                            <p>Account number: <%= cookieValue %></p>
                        </div>
                        <div class="form-group">
                            <p>Account balance: <%= request.getAttribute("balance") %></p>
                        </div>    
                        <div class="form-group form-button">
                            <input type="submit" name="signin" id="signin" class="form-submit" value="Get Balance" />
                        </div>                
                    </form>
                </div>
                <div class="signup-image" style="margin-top: 80px; text-align: center;">
                    <figure>
                        <img src="images/balance-inquiry.png" alt="sign up image">
                    </figure>
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>
