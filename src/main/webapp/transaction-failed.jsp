<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	
	<title>Transaction Success</title>
	
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

    String usernameCookieValue = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                usernameCookieValue = cookie.getValue();
            }  
        }
    }

    if (usernameCookieValue == null) {
    	 response.sendRedirect("login.jsp");
    }
    
%>
<jsp:include page="header.jsp" />
	<div class="main" style="background: #8ad0da69; padding-bottom: 20%">
		<!-- Account form -->
		<section class="signup">
			<div class="container" style="margin-top: 10%; background: #ff5f5f96;">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title" style="margin-top: 94px; text-align: center; font-size: 43px; color: white">Transaction couldn't be completed!</h2>
						<div class="form-group form-button" style="text-align: center;">
    						<a href="transaction-operations.jsp" class="form-submit">Back</a>
						</div>
					</div>
					<div class="signup-image">
						<figure>
							<img src="images/failed.png" alt="sing up image">
						</figure>
					</div>
				</div>
			</div>
		</section>
	</div>
</body>
</html>
