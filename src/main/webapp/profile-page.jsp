<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    
    <title>Account Information</title>
    
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
 <%
    HttpServletRequest newRequest = (HttpServletRequest) pageContext.getRequest();

    Cookie[] cookies = newRequest.getCookies();

    String usernameCookieName = "username";
 	String emailCookieName = "email";
 	String accountTypeCookieName = "accountType";
    String accountNumberCookieName = "accountNumber";

    String usernameCookieValue = null;
    String emailCookieValue = null;
    String accountTypeCookieValue = null;
    String accountNumber = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                usernameCookieValue = cookie.getValue();
            } 
            
            if (cookie.getName().equals(emailCookieName)) {
            	emailCookieValue = cookie.getValue();
            }
            
            if (cookie.getName().equals(accountTypeCookieName)) {
            	accountTypeCookieValue = cookie.getValue();
            }
            
            if (cookie.getName().equals(accountNumberCookieName)) {
            	accountNumber = cookie.getValue();
            }
        }
    }

    if (usernameCookieValue == null) {
    	 response.sendRedirect("login.jsp");
    }  

%>

<body>

    <jsp:include page="header.jsp" />
    <div class="main" style="background: #8ad0da69">
        <!-- Password change form -->
        <section class="signup">
            <div class="container" style="margin-top: 10%; background: #4b6e7942;">
                <div class="signup-content">
                    <div class="signup-form" style="margin-left: 50px; margin-right: 0px; padding-left: 0px;">
                    
                        <h2 class="form-title">Account Information</h2>
                        
                        <form method="post" class="register-form" id="register-form" action="UpdatePasswordServlet" style="background-color: #ffffff61;padding: 1% 0% 11% 5%;border-radius: 7px">                    
						
						<div class="form-group" style="margin-top: 25px">
						    <span for="username">Username : <%= usernameCookieValue %></span>
						</div>
						
						<div class="form-group" style="margin-top: 25px">
						    <span for="email">Email : <%= emailCookieValue %></span>
						</div>
						
						<div class="form-group" style="margin-top: 25px">
						    <span for="accountNumber">Account Number : <%= accountNumber %></span>
						</div>
						
						<div class="form-group" style="margin-top: 25px">
						    <span for="email">Account Type : <%= accountTypeCookieValue %></span>
						</div>
                            
                        </form>
                    </div>
                    <div class="signup-image" style="margin-top: 85px">
                        <figure>
                            <img src="images/profile-icon.png" alt="sign up image">
                        </figure>
                    </div>
                </div>
            </div>
        </section>
    </div>
</body>
</html>
